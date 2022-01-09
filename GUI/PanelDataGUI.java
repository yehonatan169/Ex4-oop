package ex4_java_client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//in here the info data is presented, including the stop button
public class PanelDataGUI extends JPanel implements ActionListener {

    private String time;
    private String score;
    private String moves;
    private Graphics2D g2D;


    public JButton myButton = new JButton("My Button");
    public PanelDataGUI(){
        time = "";
        score = "";
        moves = "";

    }

    public void paint(Graphics g){
        g2D = (Graphics2D) g;

        this.setBackground(new Color(10, 128, 94));
        super.paint(g);
        this.paintItems(g);

    }

    private void paintItems(Graphics g){
        float f=25.0f; // font size.
        int heightOfLetter = (this.getSize().height / 2) + 9;
        g2D.setFont(g.getFont().deriveFont(f));
        g2D.drawString("Time:",30,heightOfLetter);
        g2D.drawString(this.time,30 + (5*15),heightOfLetter);
        g2D.drawString("Score:",30 + (5*15) + 30 + (2*15) + 80,heightOfLetter);
        g2D.drawString(this.score,30 + (5*15) + 30 + (2*15) + 80 + (6*15),heightOfLetter);
        g2D.drawString("Moves:",30 + (5*15) + 30 + (2*15) + 100 + (6*15) + 30 + (2*15) + 100,heightOfLetter);
        g2D.drawString(this.moves,30 + (5*15) + 30 + (2*15) + 100 + (6*15) + 30 + (2*15) + 100 + (6*15),heightOfLetter);


        this.setLayout(null);
        this.myButton.setBounds(this.getSize().width - 150, (this.getSize().height / 2) - 15,80,30);
        this.myButton.setText("STOP");
        this.add(this.myButton);
    }


    public void displayTime(String time, String score, String moves){
        this.time = time;
        this.score = score;
        this.moves = moves;
        this.reMake();

    }

    private void reMake(){
        repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
