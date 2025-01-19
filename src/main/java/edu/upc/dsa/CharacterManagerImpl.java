package edu.upc.dsa;

import edu.upc.dsa.models.GameCharacter;

import java.util.LinkedList;
import java.util.List;

public class CharacterManagerImpl implements CharacterManager {
    private static CharacterManager instance;
    protected List<GameCharacter> gameCharacters;

    private CharacterManagerImpl() {
        this.gameCharacters = new LinkedList<>();
    }

    public static CharacterManager getInstance() {
        if (instance==null) instance = new CharacterManagerImpl();
        return instance;
    }

    public List<GameCharacter> getAllCharacters(){
      return gameCharacters;
    };
    public GameCharacter addCharacter(int stealth, int speed, int strength, String name, double cost, String url){
        GameCharacter gameCharacter1 = new GameCharacter(stealth, speed, strength, name, cost, url);
        this.gameCharacters.add(gameCharacter1);
        return gameCharacter1;
    };

    public GameCharacter getCharacter(String name){
        for (GameCharacter c: gameCharacters)
            if(c.getName().equals(name))
                return c;
        return null;
    };


    public void clear(){
        this.gameCharacters.clear();
    };
    public int size(){
      return this.gameCharacters.size();
    };

    public List<GameCharacter> findAll(){
        return gameCharacters;
    }
}
