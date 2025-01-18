package edu.upc.dsa.models;

public class PartidaActual {
    String UserName;
    String txt;
    int nivell;

    public PartidaActual(String userName, String txt, int nivell) {
        UserName = userName;
        this.txt = txt;
        this.nivell = nivell;
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
}
