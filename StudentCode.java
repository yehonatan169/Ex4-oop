package ex4_java_client;
/**
 * @author AchiyaZigi
 * A trivial example for starting the server and running all needed commands
 */

import ex4_java_client.GUI.frameGUI;

import java.io.IOException;
import java.util.LinkedList;


//main game processor.
public class StudentCode {
    public static void main(String[] args) {
        Client client = new Client();
        DataLoad data = new DataLoad();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }


        GameServer game = new GameServer(client, data);
        frameGUI f = new frameGUI(game.getG(),game.getAgentsList(),game.getPokisList(), game);
        LinkedList<Agent> agents = null;
        client.start();

        //all game takes place within this loop
        while (client.isRunning().equals("true")) {

            if(game.moveOn == 0){
                client.move();
            }
            else{
                game.moveOn--;
            }
            game.update(client);
            agents = game.getAgentsList();


            for(Agent ag : agents){

                game.goToNextStation(ag);

                game.reportApproximatityToPokemon(ag);
            }

            f.setAgents(agents);
            f.repaint();
            f.presentTime(String.valueOf(Integer.parseInt(client.timeToEnd()) / 1000), String.valueOf(game.getGrade()),String.valueOf(game.getMoves()));
            try {
                Thread.sleep(100);
                if(game.endGame){
                    client.stop();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if(client.isRunning().equals("true")){
            client.stop();
        }

    }



}
