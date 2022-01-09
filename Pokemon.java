package ex4_java_client;

import ex4_java_client.MainClasses.geolocation;


//a class to represent a pokemon
public class Pokemon implements Comparable {
    private double value;
    private int type;
    private geolocation pos;

    private int id;
    private int assignedTo; //place agent's id here (-1 if there arent any)

    public boolean light = false;

    public Pokemon(double value, int type, geolocation pos, int id){
        this.value = value;
        this.type = type;
        this.pos = new geolocation(pos.x(), pos.y(),pos.z());
        this.id = id;
        this.assignedTo = -1;
        this.light = false;
    }

    @Override
    public int compareTo(Object o) {
        if (this.getValue() - ((Pokemon)o).getValue() < 0){
            return 1;
        }
        else {
            return -1;
        }
    }

    public geolocation getLocation() {
        return this.pos;
    }
    public int getType(){
        return this.type;
    }

    public double getValue() {
        return value;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getAssignedTo() {
        return assignedTo;
    }
}
