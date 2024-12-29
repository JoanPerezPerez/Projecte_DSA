package edu.upc.dsa.models;

public class Ranking {
    String username;
    double maxpuntuacion;

    public Ranking() {
    }

    public Ranking(String username, double maxpuntuacion) {
        this.username = username;
        this.maxpuntuacion = maxpuntuacion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getMaxpuntuacion() {
        return maxpuntuacion;
    }

    public void setMaxpuntuacion(double maxpuntuacion) {
        this.maxpuntuacion = maxpuntuacion;
    }
}
