package ex4_java_client.GUI;

import ex4_java_client.Agent;
import ex4_java_client.MainClasses.DWGA;
import ex4_java_client.MainClasses.directeweightedgraph;
import ex4_java_client.Pokemon;
import ex4_java_client.api.DirectedWeightedGraph;
import ex4_java_client.api.EdgeData;
import ex4_java_client.api.NodeData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

//in this panel the game itself is displayed
public class panelGUI extends JPanel implements ActionListener {

    private directeweightedgraph graph;
    private LinkedList<Agent> agents;
    private LinkedList<Pokemon> pokemons;
    private Dimension screenSize;
    private int[] scaledX;
    private int[] scaledY;
    private DWGA dgwa;
    private HashMap<Integer, Point2D.Double> scales;
    private  HashMap<Integer, Point2D.Double> scalesAgents;
    private  HashMap<Integer, Point2D.Double> scalesPokemons;
    PriorityQueue<Integer> deletedNodes;


    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private double scaleY;
    private double scaleX;

    private double buffer;

    private Graphics2D g2D;
    private Path2D.Double q = new Path2D.Double();
    private boolean initial;

    panelGUI(DirectedWeightedGraph graph, LinkedList<Agent> agents, LinkedList<Pokemon> pokemons){
        super();
        this.dgwa = new DWGA();
        this.scales = new HashMap<Integer, Point2D.Double>();
        this.scalesAgents = new HashMap<Integer, Point2D.Double>();
        this.scalesPokemons = new HashMap<Integer, Point2D.Double>();
        this.graph = (directeweightedgraph) graph;
        this.agents = agents;
        this.pokemons = pokemons;
        this.dgwa.init(this.graph);
        this.initial = true;


        directeweightedgraph t = (directeweightedgraph)graph;


        this.scaledX = new int[this.graph.getMaxValueNodeIndex()];
        this.scaledY = new int[this.graph.getMaxValueNodeIndex()];


        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        deletedNodes = new PriorityQueue<Integer>();

    }

    private void scaling(){
        NodeData node;
        Iterator<NodeData> n = graph.nodeIter();
        if(n.hasNext()){
            node = n.next();
            this.minX = graph.getNode(node.getKey()).getLocation().x();
            this.minY = graph.getNode(node.getKey()).getLocation().y();
            this.maxX = graph.getNode(node.getKey()).getLocation().x();
            this.maxY = graph.getNode(node.getKey()).getLocation().y();
        }
        else{
            return;
        }



        while(n.hasNext()){
            node = n.next();
            if(node.getLocation().x() < this.minX){
                this.minX = node.getLocation().x();
            }
            if(node.getLocation().x() > this.maxX){
                this.maxX = node.getLocation().x();
            }
            if(node.getLocation().y() < this.minY){
                this.minY = node.getLocation().y();
            }
            if(node.getLocation().y() > this.maxY){
                this.maxY = node.getLocation().y();
            }

        }
        this.scaleX = (this.screenSize.width) / (this.maxX - this.minX) * 0.9;
        this.scaleY = (this.screenSize.height) / (this.maxY - this.minY) * 0.8;

        n = graph.nodeIter();
        while(n.hasNext()){
            node = n.next();
            scales.put(node.getKey(), new Point2D.Double((scaleX * (graph.getNode(node.getKey()).getLocation().x() - this.minX)),this.buffer + (scaleY * (graph.getNode(node.getKey()).getLocation().y() - this.minY))));
        }
        this.scaleAgents();
        for(int i = 0; this.pokemons != null &&  i < this.pokemons.size(); i++){
            scalesPokemons.put(i, new Point2D.Double((scaleX * (this.pokemons.get(i).getLocation().x() - this.minX)),this.buffer +  (scaleY * (this.pokemons.get(i).getLocation().y() - this.minY))));
        }
    }

    public void scaleAgents(){
        this.scalesAgents = new HashMap<Integer, Point2D.Double>();
        for(int i = 0; this.agents != null &&  i < this.agents.size(); i++){
            scalesAgents.put(this.agents.get(i).getId(), new Point2D.Double((scaleX * (this.agents.get(i).getLocation().x() - this.minX)),this.buffer +  (scaleY * (this.agents.get(i).getLocation().y() - this.minY))));
        }
    }

    public void setAgents(LinkedList<Agent> agents) {
        this.agents = agents;
        this.scaleAgents();
    }

    public void paint(Graphics g){
        g2D = (Graphics2D) g;
        InitialDisplay();

    }

    private void InitialDisplay(){
        NodeData node;
        Iterator<NodeData> n = graph.nodeIter();
        Iterator<EdgeData> u = graph.edgeIter();
        EdgeData e;
        scaling();

        while(u.hasNext()){
            e = u.next();
            this.drawArrow(e.getSrc(),e.getDest(), 255, 200,0);


        }
        //


        n = graph.nodeIter();
        int size;

        while(n.hasNext()){
            node = n.next();
            size = node.getKey() == 0 ? 0 : (int)Math.log10(node.getKey());
            g2D.setColor(Color.BLACK);
            g2D.fillOval((int)this.scales.get(node.getKey()).x, (int)this.scales.get(node.getKey()).y, 20, 20);

            g2D.setColor(Color.red);

            g2D.drawString(String.valueOf(node.getKey()),(int)this.scales.get(node.getKey()).x + 7 - (size * 5), (int)this.scales.get(node.getKey()).y + 14);
        }
        this.drawAgents();
        this.drawPokemons();
    }

    private void drawAgents(){
        g2D.setColor(Color.RED);
        for(int i = 0;this.agents!= null && i < this.agents.size(); i++){
            g2D.fillOval((int)this.scalesAgents.get(agents.get(i).getId()).x, (int)this.scalesAgents.get(agents.get(i).getId()).y, 20, 20);
        }
    }

    public void drawPokemons(){
        g2D.setColor(Color.BLUE);
        for(int i = 0;this.pokemons!= null && i < this.pokemons.size(); i++){
            if(this.pokemons.get(i).light){
                g2D.setColor(Color.YELLOW);
            }
            else{
                g2D.setColor(Color.BLUE);
            }
            g2D.fillOval((int)this.scalesPokemons.get(i).x, (int)this.scalesPokemons.get(i).y, 20, 20);
        }
    }

    private void drawArrow(int src, int dest, int r, int g, int b){

        g2D.setColor(new Color(r,g,b));
        g2D.setStroke(new BasicStroke(3));
        double theta;
        double thetaValueX = this.scales.get(src).x;
        double thetaValueY = this.scales.get(src).y;

        g2D.drawLine((int)scales.get(src).x + 10,(int)scales.get(src).y + 10,(int)scales.get(dest).x + 10, (int)scales.get(dest).y + 10);


        int x1;
        theta = Math.atan2(this.scales.get(dest).y - thetaValueY, this.scales.get(dest).x - thetaValueX);
        drawArrowHead(g2D, theta, this.scales.get(dest).x + 10, this.scales.get(dest).y + 10);
    }

    //arrow making function found online
    private void drawArrowHead(Graphics2D g2, double theta, double x0, double y0)
    {
        double barb = 20;
        double phi = Math.PI / 6;

        double x = x0 - barb * Math.cos(theta + phi);
        double y = y0 - barb * Math.sin(theta + phi);
        g2.draw(new Line2D.Double(x0, y0, x, y));
        x = x0 - barb * Math.cos(theta - phi);
        y = y0 - barb * Math.sin(theta - phi);
        g2.draw(new Line2D.Double(x0, y0, x, y));
    }




    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
