package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.GameCharacter;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.useritemcharacterrelation;
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
        session = FactorySession.openSession();
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

    public List<Item> BuyItemUser(String ItemName, String nameUser) throws UserNotFoundException, ItemNotFoundException, NotEnoughMoneyException {
        User u = (User)session.get(User.class, "name", nameUser);
        if (u==null) throw new UserNotFoundException();
        Item i = (Item)session.get(Item.class, "name", ItemName);
        if (i == null) throw new ItemNotFoundException();
        if(u.getMoney()>=i.getCost()){
            session.buy("name",nameUser,"name",ItemName,null,null);
            u.setMoney(u.getMoney()-i.getCost());
            session.update(u,"name",u.getName());
            logger.info(u.getName()+" HA COMPRADO "+i.getName());
            return (List<Item>)session.getRelaciones(Item.class,"name",nameUser);
        }
        else throw new NotEnoughMoneyException();
    };

    public List<GameCharacter> BuyCharacter(String nameUser, String nameCharacter) throws UserNotFoundException, CharacterNotFoundException, NotEnoughMoneyException {
        GameCharacter c = (GameCharacter)session.get(GameCharacter.class,"name",nameCharacter);
        if (c== null) throw new CharacterNotFoundException();
        User u = (User)session.get(User.class, "name", nameUser);
        if (u == null) throw new UserNotFoundException();
        if(u.getMoney()>=c.getCost()){
            session.buy("name",nameUser,null,null,"name",nameCharacter);
            u.setMoney(u.getMoney()-c.getCost());
            session.update(u,"name",u.getName());
            return (List<GameCharacter>)session.getRelaciones(GameCharacter.class,"name",nameUser);
        }
        else throw new NotEnoughMoneyException();
    }

    public List<Item> getItemUser(String userName) throws UserNotFoundException, UserHasNoItemsException{
        List<Item> respuesta = (List<Item>)session.getRelaciones(Item.class,"name",userName);
        if(respuesta ==null) throw new UserNotFoundException();
        if(respuesta.isEmpty()) throw new UserHasNoItemsException();
        return respuesta;
    };

    public List<GameCharacter> getCharacterUser(String userName) throws UserNotFoundException, UserHasNoCharacterException{
        List<GameCharacter> respuesta = (List<GameCharacter>)session.getRelaciones(GameCharacter.class,"name",userName);
        if(respuesta ==null) throw new UserNotFoundException();
        if(respuesta.isEmpty()) throw new UserHasNoCharacterException();
        return respuesta;
    }
    public void clear(){
        session.deleteAll(Item.class);
        session.deleteAll(User.class);
        session.deleteAll(GameCharacter.class);
        session.deleteAll(useritemcharacterrelation.class);
    }

    public List<Item> getItemsUserCanBuy(User u) throws NotEnoughMoneyException{
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put("cost <",String.valueOf(u.getMoney()));
        List<Item> itemsUserCanBuy = (List<Item>)session.findAllWithConditions(Item.class, conditions);
        if(itemsUserCanBuy.isEmpty()) throw new NotEnoughMoneyException();
        return itemsUserCanBuy;
    };
    public List<GameCharacter> getCharacterUserCanBuy(User u) throws NotEnoughMoneyException{
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put("cost <",String.valueOf(u.getMoney()));
        List<GameCharacter> charatersUserCanBuy = (List<GameCharacter>)session.findAllWithConditions(GameCharacter.class, conditions);
        if(charatersUserCanBuy.isEmpty()) throw new NotEnoughMoneyException();
        return charatersUserCanBuy;
    };

}