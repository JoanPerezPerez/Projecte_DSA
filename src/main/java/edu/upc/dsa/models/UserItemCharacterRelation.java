package edu.upc.dsa.models;

public class UserItemCharacterRelation {
    int ID_User;
    int ID_GameCharacter;
    int ID_Item;

    public UserItemCharacterRelation(){};

    public UserItemCharacterRelation(int ID_User, int ID_GameCharacter, int ID_Item) {
        this.ID_User = ID_User;
        this.ID_GameCharacter = ID_GameCharacter;
        this.ID_Item = ID_Item;
    }

    public int getID_User() {
        return ID_User;
    }

    public void setID_User(int ID_User) {
        this.ID_User = ID_User;
    }

    public int getID_GameCharacter() {
        return ID_GameCharacter;
    }

    public void setID_GameCharacter(int ID_GameCharacter) {
        this.ID_GameCharacter = ID_GameCharacter;
    }

    public int getID_Item() {
        return ID_Item;
    }

    public void setID_Item(int ID_Item) {
        this.ID_Item = ID_Item;
    }
}
