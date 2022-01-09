package ex4_java_client;


import ex4_java_client.MainClasses.DWGA;
import ex4_java_client.MainClasses.DistanceReturnedData;
import ex4_java_client.MainClasses.Edgedata;
import ex4_java_client.MainClasses.directeweightedgraph;
import ex4_java_client.api.EdgeData;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class GameServer {
    //this class is the main game controller - with the relevant game functions.
    private int pokemons;
    private boolean is_logged_in;
    private int moves;
    private int grade;
    private int game_level;
    private int max_user_level;
    private int id;
    private String graph;
    private int agents;

    private Client c;
    private DataLoad data;
    private LinkedList<Pokemon> pokisList;
    private LinkedList<Agent> agentsList;
    private directeweightedgraph g;
    private DWGA g_algo;

    public int moveOn;
    public boolean endGame;



    public GameServer(Client c, DataLoad data){
        this.initilize(true, c, data);
        moveOn = 0;
        this.endGame = false;
    }

    public GameServer(){ // this one is for tests only

    }

    public void initilize(boolean withGraph, Client c, DataLoad data){
        this.c = c;
        this.data = data;
        this.data.loadData(c.getInfo(),this);
        if(withGraph){
            this.GraphInit();
        }
        int center = this.g_algo.center().getKey();
        this.pokisList = data.loadPokemon(this.c.getPokemons());
        Collections.sort(this.pokisList);
        for(int i = 0; i < this.agents; i++){
            Edgedata e = findPokemon(this.pokisList.get(i));
            if(e != null){
                c.addAgent("{\"id\":" + e.getSrc() + "}");
            }
            else{
                c.addAgent("{\"id\":" + center + "}");
            }

        }


        this.agentsList = data.loadAgent(this.c.getAgents());





    }

    public void update(Client c){

        this.c = c;
        this.data.loadData(c.getInfo(),this);
        LinkedList<Agent> updatedAgents = data.loadAgent(this.c.getAgents());
        for(int i = 0; i < this.agents; i++){
            this.agentsList.get(i).updateAgent(updatedAgents.get(i));
        }

        LinkedList<Pokemon> updatedPokemons = data.loadPokemon(this.c.getPokemons());
        for(int i = this.pokisList.size(); i < this.pokemons; i++){
            this.pokisList.add(updatedPokemons.get(i));
        }
    }

    private void GraphInit(){
        fileWriter w = new fileWriter();
        String graphStr = c.getGraph();
        w.write(graphStr, "data2/graphToLoad.json");
        this.g_algo = new DWGA();
        this.g_algo.load("data2/graphToLoad.json");
        g =(directeweightedgraph) this.g_algo.getGraph();
    }

    public void reportApproximatityToPokemon(Agent ag){
        double eps = 0.001;


        for(int i = 0; i < ag.getNextTargets().size(); i++){
            if(ag.getNextTargets().size() > 0 && (Math.abs(ag.getNextTargets().get(i).getLocation().x() - ag.getLocation().x()) < eps) && (Math.abs(ag.getNextTargets().get(i).getLocation().y() - ag.getLocation().y()) < eps)){
                this.moveOn++;
                c.move();
                this.pokisList.remove(ag.getNextTargets().get(i));
                ag.getNextTargets().remove(i);

            }
        }
    }

    public void goToNextStation(Agent ag){

        if(ag.getDest() == -1){
            this.agentOrders(ag);

        }
    }


    private void agentOrders(Agent ag){
        if(ag.getStations().size() == 0 ){
            this.setCourse(ag);
        }
        else{
            c.chooseNextEdge("{\"agent_id\":"+ ag.getId() +", \"next_node_id\":" + ag.getStations().get(0) + "}");
            ag.getStations().remove(0);
        }
    }

    private void setCourse(Agent ag){
        DistanceReturnedData currentData = null;
        Edgedata edgeToWorkWith = null;
        currentData = g_algo.getBothDistanceAndPath(ag.getSrc(), -1);
        double[] allDists = currentData.getAllDists();
        int[] allIndexes = currentData.getIndexesArray();
        double minimumDistance = -1;
        double tempDist = 0;
        Pokemon chosenPokemon = null;
        Edgedata chosenEdge = null;

        for(Pokemon pok : this.pokisList){
            edgeToWorkWith = this.findPokemon(pok);
            if(pok.getAssignedTo() != -1){
                continue;
            }
            if(minimumDistance == -1){
                minimumDistance = allDists[edgeToWorkWith.getSrc()] + edgeToWorkWith.getWeight();
                chosenPokemon = pok;
                chosenEdge = edgeToWorkWith;

            }
            else{
                tempDist = allDists[edgeToWorkWith.getSrc()] + edgeToWorkWith.getWeight();
                if(tempDist < minimumDistance){
                    minimumDistance = tempDist;
                    chosenPokemon = pok;
                    chosenEdge = edgeToWorkWith;
                }
            }
        }

        if(chosenPokemon != null){

            ag.getStations().add(chosenEdge.getDest());
            int nodeID = chosenEdge.getSrc();
            while(nodeID != ag.getSrc()){
                ag.getStations().add(0, nodeID);
                nodeID = allIndexes[nodeID];
            }

        }

        for(Pokemon pok : this.pokisList){
            Edgedata anotherEdge = findPokemon(pok);
            if(pok.getAssignedTo() == -1 && chosenEdge != null && anotherEdge.getSrc() == chosenEdge.getSrc() && anotherEdge.getDest() == chosenEdge.getDest()){
                pok.setAssignedTo(ag.getId());
                pok.light = true;
                ag.getNextTargets().add(pok);
            }
        }



    }

    private Edgedata findPokemon(Pokemon pok){

        double x = pok.getLocation().x();
        double y = pok.getLocation().y();
        double epsi = 0.000001;
        Edgedata temp;
        double slope;
        double n;
        Iterator<EdgeData> e = this.g.edgeIter();
        while(e.hasNext()){

            temp = (Edgedata)e.next();
            slope = (this.g.getNode(temp.getDest()).getLocation().y() - this.g.getNode(temp.getSrc()).getLocation().y())
                    / (this.g.getNode(temp.getDest()).getLocation().x() - this.g.getNode(temp.getSrc()).getLocation().x());
            n = this.g.getNode(temp.getDest()).getLocation().y() - (slope * this.g.getNode(temp.getDest()).getLocation().x());
            if(epsi > Math.abs(y - ((x*slope) + n))){
                if((pok.getType() > 0 && temp.getSrc() < temp.getDest()) || (pok.getType() < 0 && temp.getSrc() > temp.getDest())){
                    return temp;

                }
            }
        }
        return null;
    }


    public directeweightedgraph getG() {
        return g;
    }

    public int getId() {
        return id;
    }

    public LinkedList<Agent> getAgentsList() {
        return agentsList;
    }

    public LinkedList<Pokemon> getPokisList() {
        return pokisList;
    }

    public void setAgents(int agents) {
        this.agents = agents;
    }

    public void setGame_level(int game_level) {
        this.game_level = game_level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setMax_user_level(int max_user_level) {
        this.max_user_level = max_user_level;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void setPokemons(int pokemons) {
        this.pokemons = pokemons;
    }

    public void setIs_logged_in(boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public int getMoves(){
        return this.moves;
    }

    public int getGrade(){
        return this.grade;
    }


}
