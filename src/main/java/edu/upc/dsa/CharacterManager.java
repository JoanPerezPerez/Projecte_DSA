package edu.upc.dsa;

import edu.upc.dsa.exceptions.ItemNotFoundException;
import edu.upc.dsa.exceptions.ItemRepeatedException;
import edu.upc.dsa.models.GameCharacter;

import java.util.List;

public interface CharacterManager {

    public GameCharacter addCharacter(int stealth, int speed, int strength, String name, double cost, String url) throws ItemRepeatedException;
    public void clear();
    public int size();
    public GameCharacter getCharacter(String name);
    public List<GameCharacter> findAll();

}
