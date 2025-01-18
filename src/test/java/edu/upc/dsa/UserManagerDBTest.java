package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.SessionBD;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserManagerDBTest {
    StoreManager sm;
    ItemManager im;
    UserManager um;
    CharacterManager cm;

    @Before
    public void setUp() {
        this.im = ItemManagerImpl.getInstance();
        this.sm = StoreManagerImpl.getInstance();
        this.um = UserManagerImplBBDD.getInstance();
        this.cm = CharacterManagerImpl.getInstance();
    }
    @After
    public void tearDown() {
        // És un Singleton
        this.sm.clear();
        this.cm.clear();
        this.um.clear();
        this.im.clear();
    }

    @Test
    public void FindUserWithUserName(){
        try{
            User u1 = new User("Marcell", "12345","marcel.guim@estudiantat.upc.edu");
            SessionBD session = FactorySession.openSession(); //url, user, password);
            session.save(u1);
            User u2 = this.um.getUserFromUsername("Marcell");
            session.delete(User.class,"name",u1.getName());
            Assert.assertEquals(u1.getName(),u2.getName());
        }
        catch(UserNotFoundException ex){

        }
    }

    @Test
    public void FindAllUsers(){
        List<User> users = this.um.findAll();
        Assert.assertEquals(6,users.size());
    }

    @Test
    public void DeleteAUser(){
        try{
            SessionBD session = FactorySession.openSession(); //url, user, password);
            User u1 = new User("Marcell", "12345","marcel.guiHRm@estudiantat.upc.edu");
            int K = session.findAll(User.class).size();
            this.um.addUser(u1);
            Assert.assertEquals(K+1,session.findAll(User.class).size());
            this.um.deleteUser(u1.getName());
            Assert.assertEquals(K,session.findAll(User.class).size());
        }
        catch(UserRepeatedException ex){

        }
        catch(UserNotFoundException ex){

        }
    }

    @Test
    public void UpdateAUser() {
        try {
            User u1 = this.um.getUserFromUsername("Marcel");
            u1.setMoney(54);
            this.um.updateUser(u1);
            User u2 = this.um.getUserFromUsername("Marcel");
            Assert.assertEquals(u1.getMoney(), u2.getMoney(),0);
        } catch (UserNotFoundException ex) {

        }
    }

    @Test
    public void UpdateCobre() {
        try {
            User u1 = this.um.getUserFromUsername("Marcel");
            double cobre = u1.getCobre();
            this.um.updateCobre(800,u1);
            User u2 = this.um.getUserFromUsername("Marcel");
            Assert.assertEquals(cobre+800, u2.getCobre(),0);
        } catch (UserNotFoundException ex) {

        }
    }

    @Test
    public void ClearAllUsers(){
        this.um.clear();
        Assert.assertEquals(0,this.um.findAll().size());
    }

    @Test
    public void TestSize(){
        Assert.assertEquals(8,this.um.size());
    }

    @Test
    public void TestIfAUserWithRepeatedEMailGoesIn(){
        try{
            User u1 = new User("Marcelll", "12345","marceljet.guim@estudiantat.upc.edu");
            this.um.addUser(u1);
            Assert.assertThrows(
                    UserRepeatedException.class,
                    () -> this.um.addUser(u1)
            );
        }
        catch (UserRepeatedException ex){

        }
    }

    @Test
    public void TestUpdatePassword(){
        try{
            User u1 = new User("Marcelll", "12345678","marceljet.guim@estudiantat.upc.edu");
            this.um.addUser(u1);
            this.um.changePassword(u1,"newPassword");
            User u2 = this.um.getUserFromUsername("Marcelll");
            Assert.assertTrue(u2.getPassword().equals("newPassword"));
        } catch (UserRepeatedException e) {

        }
        catch (UserNotFoundException ex){

        }
    }

    @Test
    public void TestRecoverPassword(){
        try{
            User u4 = new User("Marcel", "1234","marcel.guim@estudiantat.upc.edu");
            this.um.addUser(u4);
            this.um.RecoverPassword(u4);
        }
        catch(UserRepeatedException ex){

        }
        catch(Exception ex){

        }
    }

    @Test
    public void TestGetChatIndividual(){
        List<ChatIndividual> chats = this.um.getChatsIndividuales("Marcel","Lluc");
        int k = 12;
    }

    @Test
    public void TestGetChatIndividualDeUsuario(){
        List<User> usuarios = this.um.dameUsuariosConLosQueMantengoChatIndividual("Marcel");
        int k = 12;
    }

    @Test
    public void PonComentarioEnChatIndividual(){
        ChatIndividual chat = new ChatIndividual("Lluc","Marcel","Hola tu 6");
        List<ChatIndividual> chats = this.um.ponComentarioEnChatPrivado(chat);
        int k = 12;
    }

    @Test
    public void DameInsigniasUser(){
        User u1 = new User("Marcel", "12345","marcel.guim@estudiantat.upc.edu");
        List<Insignia> respuesta = this.um.getAllInsignias(u1);
    }

    @Test
    public void GuardaInsigniaUsuario(){
        Insignia i1 = new Insignia("El Misha","https://cdn.diariodeavisos.com/wp-content/uploads/2023/06/cats-26-1024x750.jpg");
        User u1 = new User("Marcel", "12345","marcel.guim@estudiantat.upc.edu");
        this.um.ponInsigniaParaUsuario(i1,u1);
    }

    @Test
    public void GuardarPartidaActual(){
        PartidaActual partida = new PartidaActual("Maaaarcel","9999-----------------------------------------BBBBBBBBBBBBBBBBBBBBBBBBBDDDDDDDDDDDDDDDDD--BBBBBBBBBBBBBBBBBBBBBBBBBDDDDDDDDDDDDDDDDD-FEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEF-BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB------------------CCCCCCCCCCCCCCCCCCCCCCCCCC------------------CCCCCCCCCCCCCCCCCCCCCCCCCC------------------CCCCCCCCCCCCCCCCCCCCCCCCCC------------------CCCCCCCCCCCCCCCCCCCCCCCCCC-FEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEF-BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB---------------------------------------------",2,0,0);
        this.um.guardarPartidaActual(partida);
    }

    @Test
    public void GetPartidaActual(){
        User u1 = new User("Marrcecel","1343143","jna");
        PartidaActual partida = this.um.getPartidaActual(u1);
        int k = 12;
    }

}