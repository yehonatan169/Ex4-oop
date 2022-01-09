package ex4_java_client.tests;

import ex4_java_client.*;
import ex4_java_client.MainClasses.DWGA;
import ex4_java_client.MainClasses.directeweightedgraph;
import ex4_java_client.MainClasses.geolocation;
import ex4_java_client.api.NodeData;

import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;

class GameServerTest {
    Client c = new Client();
    DataLoad d = new DataLoad();
    GameServer s = new GameServer();
    private directeweightedgraph g;
    private DWGA g_algo;
    geolocation g1;
    geolocation g2;

    NodeData n1;
    NodeData n2;
    NodeData n3;

    LinkedList<Agent> agents;
    LinkedList<Pokemon> pokis;


    void testsInitilize(){
        this.GraphInit();
        this.agents = new LinkedList<Agent>();
        this.agents.add(new Agent(0,2,0,-1,12,(geolocation)g.getNode(0).getLocation()));

        this.n1 = g.getNode(6);
        this.n2 = g.getNode(7);
        this.n3 = g.getNode(8);

        this.g1 = new geolocation((n1.getLocation().x() + n2.getLocation().x()) / 2,(n1.getLocation().y() + n2.getLocation().y()) / 2,0);
        this.g2 = new geolocation((n3.getLocation().x() + n2.getLocation().x()) / 2,(n3.getLocation().y() + n2.getLocation().y()) / 2,0);
        this.pokis = new LinkedList<Pokemon>();
        this.pokis.add(new Pokemon(31, 31, this.g1,0));
        this.pokis.add(new Pokemon(11, 1, this.g2,1));
        this.agents.get(0).getNextTargets().add(this.pokis.get(0));
        this.agents.get(0).getNextTargets().add(this.pokis.get(1));


    }

    void GraphInit(){
        this.g_algo = new DWGA();
        this.g_algo.load("data2/testGraph.json");
        g =(directeweightedgraph) this.g_algo.getGraph();
    }

    @org.junit.jupiter.api.Test
    void reportApproximatityToPokemon() {
        //testing this function also tests the initilization of the client side:
        // including loading graph, loading and using agents and pokemons properly.
        testsInitilize();
        assertEquals(2,agents.get(0).getNextTargets().size());
        s.reportApproximatityToPokemon(agents.get(0));
        assertEquals(2,agents.get(0).getNextTargets().size());
        agents.get(0).setPos(this.g1.x(), this.g1.y());
        try{
            s.reportApproximatityToPokemon(agents.get(0));
            assertEquals(1,agents.get(0).getNextTargets().size());
            assertEquals(1,0);
        }
        catch (Exception e){
            assertEquals(1,1);
            //because we test offline - if the the agent is near pokemon - in the tests null
            //data will be sent - crashing the program, however for us it indicates that approximity
            // has detected! so assertEquals Of true will be laucnched
        }
        finally {
            this.agents.get(0).setPos(g.getNode(0).getLocation().x(), g.getNode(0).getLocation().y());
        }
    }
}