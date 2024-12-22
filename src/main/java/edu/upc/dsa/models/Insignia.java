package edu.upc.dsa.models;

public class Insignia {
    int ID;
    String name;
    String url;

    public Insignia(){}
    public Insignia(String name, String avatar) {
        this.name = name;
        this.url = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
