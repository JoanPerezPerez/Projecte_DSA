package edu.upc.dsa.models;

public class GameCharacter {
    int ID;
    String name;
    int strength;
    int speed;
    double cost;
    String character_url;

    public GameCharacter(){}

    public GameCharacter(int stealth, int speed, int strength, String name, double cost, String url) {
        this.setSpeed(speed);
        this.setStrength(strength);
        this.setName(name);
        this.setCost(cost);
        this.setCharacter_url(url);
    }

    public String getCharacter_url() {
        return character_url;
    }

    public void setCharacter_url(String character_url) {
        this.character_url = character_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
