package edu.upc.dsa.services;
import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.GameCharacter;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.reflections.Store;

import javax.naming.Name;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/storeLocal", description = "Endpoint to Store Service")
@Path("/storeLocal")
public class StoreService {
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    private CharacterManager cm;
    private SessionManager sesm;
    final static Logger logger = Logger.getLogger(StoreService.class);
    public StoreService() {
        this.im = ItemManagerImpl.getInstance();
        this.sm = StoreManagerImpl.getInstance();
        this.um = UserManagerImpl.getInstance();
        this.cm = CharacterManagerImpl.getInstance();
        this.sesm = SessionManager.getInstance();
        if (im.size()==0) {
            this.im = ItemManagerImpl.getInstance();
            this.sm = StoreManagerImpl.getInstance();
            this.um = UserManagerImpl.getInstance();
            this.cm = CharacterManagerImpl.getInstance();
            if (im.size() == 0) {
                Item item1 = new Item("Cizalla","http://10.0.2.2:8080/itemsIcons/cizalla.png");
                Item item2 = new Item("Sierra Electrica","http://10.0.2.2:8080/itemsIcons/sierraelec.png");
                Item item3 = new Item("PelaCables2000","http://10.0.2.2:8080/itemsIcons/pelacables.png");
                Item item4 = new Item("Sierra","http://10.0.2.2:8080/itemsIcons/sierra.png");
                item1.setCost(5);
                item2.setCost(50);
                item3.setCost(500);
                item4.setCost(2000);
                this.im.addItem(item1);
                this.im.addItem(item2);
                this.im.addItem(item3);
                this.im.addItem(item4);
                this.sm.addAllItems(this.im.findAll());
                User u1 = new User("Blau", "Blau2002","emailBlau");
                User u2 = new User("Lluc", "Falco12","joan.lluc.fernandez@estudiantat.upc.edu");
                User u3 = new User("David", "1234","emailDavid");
                User u4 = new User("Marcel", "1234","marcel.guim@estudiantat.upc.edu");
                u1.setMoney(10);
                u2.setMoney(100);
                u3.setMoney(1000);
                u4.setMoney(5000);
                u1.setCobre(400);
                u2.setCobre(400);
                u3.setCobre(400);
                u4.setCobre(400);
                try{
                    this.um.addUser(u1);
                    this.um.addUser(u2);
                    this.um.addUser(u3);
                    this.um.addUser(u4);
                    this.sm.addAllUsers(this.um.findAll());
                    this.cm.addCharacter(1,1,1,"primer",10,"http://10.0.2.2:8080/itemsIcons/cizalla.png");
                    this.cm.addCharacter(1,1,1,"segon",60,"http://10.0.2.2:8080/itemsIcons/cizalla.png");
                    this.cm.addCharacter(1,1,1,"tercer",50,"http://10.0.2.2:8080/itemsIcons/cizalla.png");
                    this.sm.addAllCharacters(this.cm.findAll());
                }
                catch(UserRepeatedException ex){

                }
                catch(ItemRepeatedException ex){

                }
            }
        }
    }

    @POST
    @ApiOperation(value = "User buys an Item", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Item.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "Item Not Found"),
            @ApiResponse(code = 503, message = "Not enough Money"),
            @ApiResponse(code = 505, message = "User has no more items to buy"),
            @ApiResponse(code = 506, message = "User not logged in yet"),
    })
    @Path("/buyItem/{itemName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserBuys( @PathParam("itemName") String itemName,@CookieParam("authToken") String authToken) {
        if(itemName == null) return Response.status(500).build();
        try{
            User u=SessionManager.getInstance().getSession(authToken);
            User usuario = this.um.getUserFromUsername(u.getName());
            List<Item> items = sm.BuyItemUser(itemName,usuario.getName());
            GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(ItemNotFoundException ex){
            return Response.status(502).build();
        }
        catch (NotEnoughMoneyException ex){
            return Response.status(503).build();
        }
        catch (UserHasNoItemsException ex){
            return Response.status(505).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Items of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has no Items"),
            @ApiResponse(code = 503, message = "User not yet logged in"),
            @ApiResponse(code = 506, message = "User not logged in yet"),
    })
    @Path("myItems")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@CookieParam("authToken") String authToken) {
        try{
            User u=SessionManager.getInstance().getSession(authToken);
            List<Item> items = this.sm.getItemUser(u.getName());
            GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(501).build();
        }
        catch(UserHasNoItemsException ex){
            return Response.status(502).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not yet logged in");
            return Response.status(503).build();
        }
    }
    @POST
    @ApiOperation(value = "User buys an Character", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= GameCharacter.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "Character Not Found"),
            @ApiResponse(code = 503, message = "Not enough Money"),
            @ApiResponse(code = 506, message = "User not logged in yet")
    })
    @Path("/buyCharacters/{CharacterName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserBuysCharcter(@PathParam("CharacterName") String CharacterName, @CookieParam("authToken") String authToken) {
        try{
            User u=SessionManager.getInstance().getSession(authToken);
            User usuario = this.um.getUserFromUsername(u.getName());
            List<GameCharacter> gameCharacters = sm.BuyCharacter(usuario.getName(),CharacterName);
            GenericEntity<List<GameCharacter>> entity = new GenericEntity<List<GameCharacter>>(gameCharacters) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(CharacterNotFoundException ex){
            return Response.status(502).build();
        }
        catch (NotEnoughMoneyException ex){
            return Response.status(503).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch(Exception ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(500).build();
        }

    }

    @GET
    @ApiOperation(value = "get all Characters of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = GameCharacter.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has no Characters"),
            @ApiResponse(code = 506, message = "User not logged in yet")
    })
    @Path("Characters/{NameUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCharacters(@PathParam("NameUser") String NameUser, @CookieParam("authToken") String authToken) {
        if(NameUser == null) return Response.status(500).build();
        try{
            this.sesm.getSession(authToken);
            List<GameCharacter> gameCharacters = this.sm.getCharacterUser(NameUser);
            GenericEntity<List<GameCharacter>> entity = new GenericEntity<List<GameCharacter>>(gameCharacters) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(501).build();
        }
        catch(UserHasNoCharacterException ex){
            return Response.status(502).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Characters a user can buy", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = GameCharacter.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 505, message = "User has no more characters to buy"),
            @ApiResponse(code = 506, message = "User not logged in yet"),
    })
    @Path("CharactersUserCanBuy/{NameUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCharactersUserCanBuy(@CookieParam("authToken") String authToken) {
        try{
            User u=SessionManager.getInstance().getSession(authToken);
            User usuario = this.um.getUserFromUsername(u.getName());
            List<GameCharacter> gameCharacters = this.sm.getCharacterUserCanBuy(usuario);
            GenericEntity<List<GameCharacter>> entity = new GenericEntity<List<GameCharacter>>(gameCharacters) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(UserHasNoCharacterException ex){
            return Response.status(505).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch (Exception e) {
            return Response.status(500).build();
        }

    }

    @GET
    @ApiOperation(value = "get all Items a user can buy", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 505, message = "User has no more items to buy"),
            @ApiResponse(code = 506, message = "User not logged in yet"),
    })
    @Path("/ItemsUserCanBuy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemsUserCanBuy(@CookieParam("authToken") String authToken) {
        try{
            User u=SessionManager.getInstance().getSession(authToken);
            User usuario = this.um.getUserFromUsername(u.getName());
            List<Item> items = this.sm.getItemsUserCanBuy(usuario);
            GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
            return Response.status(201).entity(entity).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(UserHasNoItemsException ex){
            return Response.status(505).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch (Exception e) {
            return Response.status(500).build();
        }
    }
}
