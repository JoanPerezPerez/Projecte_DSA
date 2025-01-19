package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import edu.upc.dsa.util.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StoreManagerTest {
    StoreManager sm;
    ItemManager im;
    UserManager um;
    CharacterManager cm;


    @After
    public void tearDown() {
        // És un Singleton
        this.sm.clear();
        this.cm.clear();
        this.um.clear();
        this.im.clear();
    }
    @Test
    public void listofUsers(){
//        List<User> users  = sm.listAllUsers();
//        Assert.assertEquals(4, users.size());
//        Assert.assertEquals("Blau", users.get(0).getName());
//        Assert.assertEquals("David", users.get(1).getName());
//        Assert.assertEquals("Lluc", users.get(2).getName());

    }
    @Test
    public void ItemUser(){
        List<Item> itemsUser;
        List<Item> itemsUser2;
        try{
            itemsUser = this.sm.BuyItemUser("s123", "Andreu");
            Assert.assertEquals(1, itemsUser.size());
            Assert.assertEquals("s123", itemsUser.get(0).getName());
            itemsUser2 = this.sm.BuyItemUser("s124", "Andreu");
            Assert.assertEquals(2, itemsUser2.size());
            Assert.assertEquals("s124", itemsUser2.get(1).getName());
        }
        catch(UserNotFoundException e){

        }
        catch(ItemNotFoundException ex){

        }
        catch(NotEnoughMoneyException ex){

        }
        catch(UserHasNoItemsException ex){

        }
    }
}
