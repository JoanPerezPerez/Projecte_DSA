package edu.upc.dsa.models;

public class InsigniaRelaciones {
    int ID_User;
    int ID_Insignia;

    public InsigniaRelaciones(){}

    public InsigniaRelaciones(int ID_User, int ID_Insignia) {
        this.ID_User = ID_User;
        this.ID_Insignia = ID_Insignia;
    }

    public int getID_User() {
        return ID_User;
    }

    public void setID_User(int ID_User) {
        this.ID_User = ID_User;
    }

    public int getID_Insignia() {
        return ID_Insignia;
    }

    public void setID_Insignia(int ID_Insignia) {
        this.ID_Insignia = ID_Insignia;
    }
}
