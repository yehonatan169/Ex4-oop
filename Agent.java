package ex4_java_client;

import ex4_java_client.MainClasses.geolocation;

import java.util.LinkedList;


//a class to represent an agent
public class Agent {
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private geolocation pos;

    private LinkedList<Integer> stations;
    private LinkedList<Pokemon> nextTargets;

    public Agent(int id, double value, int src, int dest, double speed, geolocation pos){
        this.id = id;
        this.value = value;
        this.src = src;
        this.dest = dest;
        this.speed = speed;
        this.pos = new geolocation(pos.x(), pos.y(),pos.z());

        if(this.stations == null){
            this.stations = new LinkedList<Integer>();
        }


        this.nextTargets = new LinkedList<Pokemon>();
    }

    public void updateAgent(Agent a){
        this.id = a.id;
        this.value = a.value;
        this.src = a.src;
        this.dest = a.dest;
        this.speed = a.speed;
        this.pos = new geolocation(a.pos);
    }

    public int getId(){
        return this.id;
    }

    public ex4_java_client.api.GeoLocation getLocation() {
        return this.pos;
    }

    public void setPos(double x, double y){
        this.pos.setX(x);
        this.pos.setY(y);

    }

    public int getDest(){
        return this.dest;
    }

    public void setDest(int dest){
        this.dest = dest;
    }

    public LinkedList<Integer> getStations() {
        return stations;
    }

    public int getSrc() {
        return src;
    }


    public LinkedList<Pokemon> getNextTargets() {
        return nextTargets;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", value=" + value +
                ", src=" + src +
                ", dest=" + dest +
                ", speed=" + speed +
                ", pos=" + pos +
                '}';
    }
}
