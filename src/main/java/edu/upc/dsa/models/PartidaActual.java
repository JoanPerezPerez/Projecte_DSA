package edu.upc.dsa.models;

public class PartidaActual {
    String UserName;
    String txt;
    int nivell;
    int cobreActual;
    int cobreTotal;

    public PartidaActual(String userName, String txt, int nivell,int cobreActual,int cobreTotal) {
        UserName = userName;
        this.txt = txt;
        this.nivell = nivell;
        this.cobreActual=cobreActual;
        this.cobreTotal=cobreTotal;
    }
    public PartidaActual() {};

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getNivell() {
        return nivell;
    }

    public void setNivell(int nivell) {
        this.nivell = nivell;
    }

    public int getCobreActual() {
        return cobreActual;
    }

    public void setCobreActual(int cobreActual) {
        this.cobreActual = cobreActual;
    }

    public int getCobreTotal() {
        return cobreTotal;
    }

    public void setCobreTotal(int cobreTotal) {
        this.cobreTotal = cobreTotal;
    }
}
