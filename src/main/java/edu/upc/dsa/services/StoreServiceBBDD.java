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

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/store", description = "Endpoint to Store Service with Data Base")
@Path("/store")
public class StoreServiceBBDD {
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    private CharacterManager cm;
    private SessionManager sesm;
    final static Logger logger = Logger.getLogger(StoreServiceBBDD.class);
    public StoreServiceBBDD() {
        this.im = new ItemManagerImplBBDD();
        this.sm = StoreManagerImplBBDD.getInstance();
        this.um = UserManagerImplBBDD.getInstance();
        this.cm = CharacterManagerImplBBDD.getInstance();
        this.sesm = SessionManager.getInstance();
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
            sesm.getSession(authToken);
            User u=SessionManager.getInstance().getSession(authToken);
            List<Item> items = sm.BuyItemUser(itemName,u.getName());
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
            @ApiResponse(code = 506, message = "User not logged in yet"),
    })
    @Path("Items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems(@CookieParam("authToken") String authToken) {
        try{
            User u = this.sesm.getSession(authToken);
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
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch (Exception ex){
            return Response.status(500).build();
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
            @ApiResponse(code = 505, message = "User has no more characters to buy"),
            @ApiResponse(code = 506, message = "User not logged in yet"),
    })
    @Path("/buyCharacters/{CharacterName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserBuysCharcter(@PathParam("CharacterName") String CharacterName, @CookieParam("authToken") String authToken) {
        if(CharacterName == null) return Response.status(500).build();
        try{
            this.sesm.getSession(authToken);
            User u=SessionManager.getInstance().getSession(authToken);
            List<GameCharacter> gameCharacters = sm.BuyCharacter(u.getName(),CharacterName);
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
        catch (UserHasNoCharacterException ex){
            return Response.status(505).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Characters of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = GameCharacter.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User has no Characters"),
            @ApiResponse(code = 506, message = "User not logged in yet"),

    })
    @Path("Characters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCharacters(@CookieParam("authToken") String authToken) {
        try{
            User u = this.sesm.getSession(authToken);
            List<GameCharacter> characters = this.sm.getCharacterUser(u.getName());
            GenericEntity<List<GameCharacter>> entity = new GenericEntity<List<GameCharacter>>(characters) {};
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
        catch (Exception ex){
            return Response.status(500).build();
        }

    }

    @GET
    @ApiOperation(value = "get all Characters a user can buy", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = GameCharacter.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "User has not enough Money"),
            @ApiResponse(code = 505, message = "User has no more characters to buy"),
            @ApiResponse(code = 506, message = "User not logged in yet"),
    })
    @Path("CharactersUserCanBuy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCharactersUserCanBuy(@CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            User u=SessionManager.getInstance().getSession(authToken);
            List<GameCharacter> gameCharacters = this.sm.getCharacterUserCanBuy(u);
            GenericEntity<List<GameCharacter>> entity = new GenericEntity<List<GameCharacter>>(gameCharacters) {};
            return Response.status(201).entity(entity).build();
        }
        catch(NotEnoughMoneyException ex){
            return Response.status(502).build();
        }
        catch(UserHasNoCharacterException ex){
            return Response.status(505).build();
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
    @ApiOperation(value = "get all Items a user can buy", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Item.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "User has not enough Money"),
            @ApiResponse(code = 503, message = "User has no items"),
            @ApiResponse(code = 506, message = "User not logged in yet"),

    })
    @Path("ItemsUserCanBuy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemssUserCanBuy(@CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            User u=SessionManager.getInstance().getSession(authToken);
            List<Item> items = this.sm.getItemsUserCanBuy(u);
            GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {};
            return Response.status(201).entity(entity).build();
        }
        catch(NotEnoughMoneyException ex){
            return Response.status(502).build();
        }
        catch(UserHasNoItemsException ex){
            return Response.status(503).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }
}
