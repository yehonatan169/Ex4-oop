package ex4_java_client;

import ex4_java_client.MainClasses.geolocation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;


//this class is the loader function for the game
public class DataLoad {
    public LinkedList<Agent> loadAgent(String file) {
        LinkedList<Agent> listOfAgents = new LinkedList<Agent>();
        try {
            JSONObject format = new JSONObject(file);
            JSONArray agents = format.getJSONArray("Agents");
            for(int i = 0; i < agents.length(); i++) {
                JSONObject PObject = (JSONObject) agents.get(i);
                JSONObject t = (JSONObject)PObject.get("Agent");

                int id = (t.getInt("id"));
                double value = (t.getDouble("value"));
                int src = (t.getInt("src"));
                int dest = (t.getInt("dest"));
                double speed = (t.getDouble("speed"));
                String pos = t.getString("pos");

                String[] geoloc = pos.split(",");
                geolocation loc = new geolocation(Double.parseDouble(geoloc[0]), Double.parseDouble(geoloc[1])
                        , Double.parseDouble(geoloc[2]));
                Agent agent = new Agent(id, value,src,dest,speed,loc);
                listOfAgents.add(agent);
            }
            return listOfAgents;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("failed");
            return null;
        }
    }

    public LinkedList<Pokemon> loadPokemon(String file) {
        LinkedList<Pokemon> listOfPokemons = new LinkedList<Pokemon>();
        try {
            JSONObject format = new JSONObject(file);
            JSONArray pokemons = format.getJSONArray("Pokemons");
            for(int i = 0; i < pokemons.length(); i++) {
                JSONObject PObject = (JSONObject) pokemons.get(i);
                JSONObject t = (JSONObject)PObject.get("Pokemon");
                double value = t.getDouble("value");
                int type = t.getInt("type");
                String pos = t.getString("pos");

                String[] geoloc = pos.split(",");
                geolocation loc = new geolocation(Double.parseDouble(geoloc[0]), Double.parseDouble(geoloc[1])
                        , Double.parseDouble(geoloc[2]));
                Pokemon pokemon = new Pokemon(value, type,loc, i);
                listOfPokemons.add(pokemon);
            }
            return listOfPokemons;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadData(String file, GameServer stats){
        GameServer data;
        try {
            JSONObject format = new JSONObject(file);
            JSONObject currentData = format.getJSONObject("GameServer");
            stats.setPokemons(currentData.getInt("pokemons"));
           stats.setIs_logged_in(currentData.getBoolean("is_logged_in"));
            stats.setMoves(currentData.getInt("moves"));
            stats.setGrade(currentData.getInt("grade"));
            stats.setGame_level(currentData.getInt("game_level"));
           stats.setMax_user_level(currentData.getInt("max_user_level"));
            stats.setId(currentData.getInt("id"));
            stats.setGraph(currentData.getString("graph"));
            stats.setAgents(currentData.getInt("agents"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("failed");
        }
    }
}
