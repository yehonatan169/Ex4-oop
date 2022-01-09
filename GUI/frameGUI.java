package ex4_java_client.GUI;

import ex4_java_client.Agent;
import ex4_java_client.GameServer;
import ex4_java_client.Pokemon;
import ex4_java_client.api.DirectedWeightedGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

//main frame class - in here the data is display - including 2 panels - game panel and data panel
public class frameGUI extends JFrame implements ActionListener {
    private panelGUI panel;
    private PanelDataGUI panelData;
    private  GameServer game;

    private DirectedWeightedGraph g;
    LinkedList<Agent> agents;
    LinkedList<Pokemon> pokemons;

    public frameGUI(DirectedWeightedGraph dg, LinkedList<Agent> agents, LinkedList<Pokemon> pokemons, GameServer game){
        super();
        g = dg;
        this.agents = agents;
        this.pokemons = pokemons;
        panel = new panelGUI(g, agents,pokemons);
        panelData = new PanelDataGUI();
        this.setResizable(false);
        this.setVisible(true);

        this.setTitle("Graph Task");
        this.getContentPane().setBackground(new Color(14, 187, 132));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension ww = Toolkit.getDefaultToolkit().getScreenSize();
        panelData.setBounds(0,0,(int)ww.getWidth(), (int)(ww.getHeight() * 0.1));
        panel.setBounds(0,(int)(ww.getHeight() * 0.1),(int)ww.getWidth(), (int)(ww.getHeight() * 0.9));
        this.game = game;



        this.add(panelData);
        this.add(panel);
        this.setSize((int)ww.getWidth(),(int)ww.getHeight());
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.panelData.myButton.addActionListener(this);

    }

    public void setAgents(LinkedList<Agent> agents) {

        this.agents = agents;
        this.panel.setAgents(agents);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == panelData.myButton){
            this.end();
        }
    }

    public void end(){
        //stop function
        this.dispose();
        this.game.endGame = true;

    }

    public void presentTime(String time, String score, String moves){
        this.panelData.displayTime(time, score, moves);
    }
}
