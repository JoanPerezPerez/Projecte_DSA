package edu.upc.dsa.models;

public class Partidas {
    int ID;
    int ID_Jugador;
    double PuntuacionMax;

    public Partidas() {
    }

    public Partidas(int ID, int ID_Jugador, double puntuacionMax) {
        this.ID = ID;
        this.ID_Jugador = ID_Jugador;
        PuntuacionMax = puntuacionMax;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getPuntuacionMax() {
        return PuntuacionMax;
    }

    public void setPuntuacionMax(double puntuacionMax) {
        PuntuacionMax = puntuacionMax;
    }

    public int getID_Jugador() {
        return ID_Jugador;
    }

    public void setID_Jugador(int ID_Jugador) {
        this.ID_Jugador = ID_Jugador;
    }
}
