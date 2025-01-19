package edu.upc.dsa;

import edu.upc.dsa.exceptions.ItemRepeatedException;
import edu.upc.dsa.models.GameCharacter;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.SessionBD;

import java.util.LinkedList;
import java.util.List;

public class CharacterManagerImplBBDD implements CharacterManager {
    private static CharacterManager instance;
    SessionBD session;


    private CharacterManagerImplBBDD() {
    }

    public static CharacterManager getInstance() {
        if (instance==null) instance = new CharacterManagerImplBBDD();
        return instance;
    }

    public List<GameCharacter> getAllCharacters(){
        session = FactorySession.openSession();
        List<GameCharacter> respuesta = (List<GameCharacter>)session.findAll(GameCharacter.class);
        session.close();
        return respuesta;
    };
    public GameCharacter addCharacter(int stealth, int speed, int strength, String name, double cost, String url) throws ItemRepeatedException {
        session = FactorySession.openSession();
        GameCharacter gameCharacter1 = new GameCharacter(stealth, speed, strength, name, cost, url);
        if(session.get(GameCharacter.class,"name", name) != null) throw new ItemRepeatedException();
        session.save(gameCharacter1);
        session.close();
        return gameCharacter1;
    };

    public GameCharacter getCharacter(String name){
        session = FactorySession.openSession();
        GameCharacter respuesta = (GameCharacter)session.get(GameCharacter.class, "name", name);
        session.close();
        return respuesta;
    };


    public void clear(){
        session = FactorySession.openSession();
        session.deleteAll(GameCharacter.class);
        session.close();
    };
    public int size(){
        session = FactorySession.openSession();
        int respuesta = session.findAll(GameCharacter.class).size();
        session.close();
        return respuesta;
    };

    public List<GameCharacter> findAll(){
        session = FactorySession.openSession();
        List<GameCharacter> respuesta = (List<GameCharacter>)session.findAll(GameCharacter.class);
        session.close();
        return respuesta;
    }
}
