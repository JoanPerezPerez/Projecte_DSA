package edu.upc.dsa.services;
import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.ChangePassword;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.SessionBD;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/games", description = "Endpoint to Games Service")
@Path("/games")
public class GameService {
    //test
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    private CharacterManager cm;
    private SessionManager sesm;
    final static Logger logger = Logger.getLogger(UserService.class);
    public GameService() {
        this.im = new ItemManagerImplBBDD();
        this.sm = StoreManagerImplBBDD.getInstance();
        this.um = UserManagerImplBBDD.getInstance();
        this.cm = CharacterManagerImplBBDD.getInstance();
        this.sesm = SessionManager.getInstance();
    }
    @POST
    @ApiOperation(value = "save", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error")
    })
    @Path("/save")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response SaveGame(
            @CookieParam("authToken") String authToken,
            String level_actually// Directly accept the text body as a String
    ) {
        try {
            User u = this.sesm.getSession(authToken);

            // Now you can access the plain text data from the body
            System.out.println("Received level: " + level_actually);

            // Perform any updates or actions based on the state

            return Response.status(201).build();
        } catch (UserNotLoggedInException ex) {
            return Response.status(501).build();
        }
    }

    //PETICIO GET PARTIDA


    //SUMAR COBRE
    @GET
    @ApiOperation(value = "sumarcobre", notes = "hahaha")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Track not found"),
            @ApiResponse(code = 506, message = "User not logged in")
    })
    @Path("/addCobre/{Cobre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCobre(@PathParam("Cobre") String CobreAmount, @CookieParam("authToken") String authToken) {
        try{
            User u = this.sesm.getSession(authToken);
            int cobreAdd=Integer.parseInt(CobreAmount);
            logger.info(u.getName()+ " ha ganado: "+cobreAdd);
            //POSAR LOGICA DE SUMAR COBRE A L'actual

            return Response.status(201).build();
        }
        catch (UserNotLoggedInException ex){
            logger.warn("Attention, User not logged in yet");
            return Response.status(500).build();
        }
    }

    //GET COBRE JA ESTA IMPLEMENTAT A USERSERVICEBBDDD
}
