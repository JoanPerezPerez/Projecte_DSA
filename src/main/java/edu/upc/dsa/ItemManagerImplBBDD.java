package edu.upc.dsa;

import edu.upc.dsa.exceptions.ItemNotFoundException;
import edu.upc.dsa.exceptions.UserRepeatedException;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.SessionBD;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class ItemManagerImplBBDD implements ItemManager {
    private static ItemManager instance;
    final static Logger logger = Logger.getLogger(ItemManagerImplBBDD.class);
    SessionBD session;
    public ItemManagerImplBBDD() {
    }

    public int size() {
        session = FactorySession.openSession();
        int ret = session.findAll(Item.class).size();
        logger.info("size " + ret);
        session.close();
        return ret;
    }

    public Item addItem(Item i) {
        session = FactorySession.openSession();
        logger.info("new Item " + i);
        if(session.get(i.getClass(),"name",i.getName()) == null)
        {
            session.save(i);
            logger.info("new Item added");
            session.close();
            return i;
        }
        else{
            logger.warn("Item already exists with that name");
            session.close();
            return null;
        }
    }

    public Item addItem(String name,String url) {
        return this.addItem(new Item(name,url));
    }

    public Item getItem(String Name)throws ItemNotFoundException {
        session = FactorySession.openSession();
        logger.info("getItem("+Name+")");
        Item i = (Item)session.get(Item.class,"name",Name);
        session.close();
        if(i!=null)return i;
        logger.warn("not found " + Name);
        throw new ItemNotFoundException();
    }


    public List<Item> findAll() {
        session = FactorySession.openSession();
        List<Item> respuesta = session.findAll(Item.class);
        session.close();
        return respuesta;
    }

    @Override
    public void deleteItem(String Name) throws ItemNotFoundException {
        session = FactorySession.openSession();
        Item i = (Item)session.get(Item.class, "name",Name);
        if (i==null) {
            logger.warn("Item = " + Name + " not found");
        }
        else logger.info(i+" deleted ");
        session.delete(Item.class, "name",Name);
        session.close();
    }

    @Override
    public Item updateItem(Item i)  throws ItemNotFoundException {
        session = FactorySession.openSession();
        Item t = (Item) session.get(i.getClass(),"name",i.getName());
        if (t!=null) {
            logger.info(i+" rebut!!!! ");
            session.update(i,"name", i.getName());
            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+i);
        }
        session.close();
        return i;
    }

    public void clear() {
        session = FactorySession.openSession();
        session.deleteAll(Item.class);
        session.close();
    }

}