package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.GameCharacter;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.UserItemCharacterRelation;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.SessionBD;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreManagerImplBBDD implements StoreManager {
    private static StoreManager instance;
    protected HashMap<String,List<Item>> itemsOfUsers; //Key=user name
    protected HashMap<String,List<User>> usersOfItems; //Key=item id
    protected HashMap<String,List<GameCharacter>> charactersOfUsers; //Key=user name
    protected HashMap<String,List<User>> usersOfCharacter; //Key=character name
    protected List<GameCharacter> gameCharacters;
    protected List<User> users;
    protected List<Item> items;
    final static Logger logger = Logger.getLogger(StoreManagerImplBBDD.class);
    private UserManager um;
    private ItemManager im;
    private CharacterManager cm;
    SessionBD session;

    private StoreManagerImplBBDD() {
        this.usersOfItems = new HashMap<>();
        this.itemsOfUsers = new HashMap<>();
        this.charactersOfUsers = new HashMap<>();
        this.usersOfCharacter = new HashMap<>();
        this.gameCharacters = new ArrayList<>();
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.im = ItemManagerImpl.getInstance();
        this.um = UserManagerImpl.getInstance();
        this.cm = CharacterManagerImpl.getInstance();
    }

    public static StoreManager getInstance() {
        if (instance==null) instance = new StoreManagerImplBBDD();
        return instance;
    }
    public void addAllUsers(List<User> u)
    {

    }
    public void addAllItems(List<Item> i)
    {

    }
    public void addAllCharacters(List<GameCharacter> c){

    }

    public void addUser(User user){

    };
    public void addItem(Item item){

    };

    public List<Item> BuyItemUser(String ItemName, String nameUser) throws UserNotFoundException, ItemNotFoundException, NotEnoughMoneyException, UserHasNoItemsException {
        session = FactorySession.openSession();
        User u = (User)session.get(User.class, "name", nameUser);
        if (u==null) throw new UserNotFoundException();
        Item i = (Item)session.get(Item.class, "name", ItemName);
        if (i == null) throw new ItemNotFoundException();
        if(u.getMoney()>=i.getCost()){
            session.save(new UserItemCharacterRelation(u.getID(),0,i.getID()));
            u.setMoney(u.getMoney()-i.getCost());
            session.update(u,"name",u.getName());
            session.close();
            logger.info(u.getName()+" HA COMPRADO "+i.getName());
            try{
                return getItemsUserCanBuy(u);
            } catch (UserHasNoItemsException e) {
                throw new UserHasNoItemsException();
            }
        }
        else {
            session.close();
            throw new NotEnoughMoneyException();
        }
    };

    public List<GameCharacter> BuyCharacter(String nameUser, String nameCharacter) throws UserNotFoundException, CharacterNotFoundException, NotEnoughMoneyException, UserHasNoCharacterException {
        session = FactorySession.openSession();
        GameCharacter c = (GameCharacter)session.get(GameCharacter.class,"name",nameCharacter);
        if (c== null) throw new CharacterNotFoundException();
        User u = (User)session.get(User.class, "name", nameUser);
        if (u == null) throw new UserNotFoundException();
        if(u.getMoney()>=c.getCost()){
            session.save(new UserItemCharacterRelation(u.getID(),c.getID(),0));
            u.setMoney(u.getMoney()-c.getCost());
            session.update(u,"name",u.getName());
            session.close();
            try{
                return getCharacterUserCanBuy(u);
            } catch (UserHasNoCharacterException e) {
                throw new UserHasNoCharacterException();
            }
        }
        else {
            session.close();
            throw new NotEnoughMoneyException();
        }
    }

    public List<Item> getItemUser(String userName) throws UserNotFoundException, UserHasNoItemsException{
        session = FactorySession.openSession();
        List<Item> respuesta = (List<Item>)session.getRelaciones(Item.class,"name",userName);
        session.close();
        if(respuesta ==null) throw new UserNotFoundException();
        if(respuesta.isEmpty()) throw new UserHasNoItemsException();
        return respuesta;
    };

    public List<GameCharacter> getCharacterUser(String userName) throws UserNotFoundException, UserHasNoCharacterException{
        session = FactorySession.openSession();
        List<GameCharacter> respuesta = (List<GameCharacter>)session.getRelaciones(GameCharacter.class,"name",userName);
        session.close();
        if(respuesta ==null) throw new UserNotFoundException();
        if(respuesta.isEmpty()) throw new UserHasNoCharacterException();
        return respuesta;
    }
    public void clear(){
        session = FactorySession.openSession();
        session.deleteAll(Item.class);
        session.deleteAll(User.class);
        session.deleteAll(GameCharacter.class);
        session.deleteAll(UserItemCharacterRelation.class);
        session.close();
    }

    public List<Item> getItemsUserCanBuy(User u) throws UserHasNoItemsException{
        session = FactorySession.openSession();
        List<Item> itemsUserCanBuy = (List<Item>)session.findObjectNotBoughtForUser(Item.class,"name",u.getName());
        session.close();
        if(itemsUserCanBuy.isEmpty()){
            throw new UserHasNoItemsException();
        }
        else {
            for (Item i : itemsUserCanBuy) {
                logger.info("Item not bought by user: " + i.getName());
            }
            return itemsUserCanBuy;
        }

    };
    public List<GameCharacter> getCharacterUserCanBuy(User u) throws UserHasNoCharacterException{
        session = FactorySession.openSession();
        List<GameCharacter> CharacterUserCanBuy = (List<GameCharacter>)session.findObjectNotBoughtForUser(GameCharacter.class,"name",u.getName());
        session.close();
        if(CharacterUserCanBuy.isEmpty()) {
            throw new UserHasNoCharacterException();
        }
        else{
            for (GameCharacter c : CharacterUserCanBuy) {
                logger.info("Characters not bought by user: " + c.getName());
            }
            return CharacterUserCanBuy;
        }
    };

}
