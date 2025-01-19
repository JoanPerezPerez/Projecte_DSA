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

@Api(value = "/usersLocal", description = "Endpoint to Users Service")
@Path("/usersLocal")
public class UserService {
    //test
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    private CharacterManager cm;
    private SessionManager sesm;
    final static Logger logger = Logger.getLogger(UserService.class);
    public UserService() {
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
                User u1 = new User("Blau", "Blau2002","maria.blau.camarasa@estudiantat.upc.edu");
                User u2 = new User("Lluc", "Falco12","joan.lluc.fernandez@estudiantat.upc.edu");
                User u3 = new User("David", "1234","david.arenas.romero@estudiantat.upc.edu");
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

    //PART AUTENT
    @POST
    @ApiOperation(value = "Login a new User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "Wrong Password"),
            @ApiResponse(code = 502, message = "User Not Found"),
    })
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response LoginUser(User user) {
        if (user.getName() == null || user.getPassword() == null) {
            return Response.status(500).build();
        }
        try {
            // Verificar si la contraseña es correcta
            if (user.getPassword().equals(this.um.getUserFromUsername(user.getName()).getPassword())) {

                // Crear una cookie con un identificador aleatorio o un token de sesión
                String cookieValue = generateRandomSessionId();
                NewCookie authCookie = new NewCookie(
                        "authToken",    // Nombre de la cookie
                        cookieValue,          // Valor de la cookie (puede ser un token generado o sesión)
                        "/",                  // Path donde la cookie es válida ("/" para toda la aplicación)
                        null,                 // Dominio de la cookie (null para el dominio actual)
                        "Autenticación",      // Comentario (opcional)
                        60 * 60 * 24,         // Expiración en segundos (aquí es 1 día)
                        false,                // Si debe ser solo para HTTPS (aquí false para desarrollo)
                        true                  // Hacer la cookie accesible solo en HTTP (no por JS)
                );
                sesm.createSession(cookieValue,this.um.getUserFromUsername(user.getName()));


                // Devolver la respuesta con la cookie de autenticación
                return Response.status(201)
                        .entity(user)  // Enviar el objeto `user` en la respuesta
                        .cookie(authCookie)  // Añadir la cookie a la respuesta
                        .build();
            } else {
                return Response.status(501).build();  // Contraseña incorrecta
            }
        } catch (UserNotFoundException ex) {
            return Response.status(502).build();  // Usuario no encontrado
        }
    }

    @GET
    @ApiOperation(value = "Check Session Validity", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "AUTHORIZED"),
            @ApiResponse(code = 501, message = "UNAUTHORIZED"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),

    })
    @Path("/sessionCheck")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession(@CookieParam("authToken") String authToken) {
        try{
            if (SessionManager.getInstance().getSession(authToken)==null)
                return Response.status(501).build() ;
            else
                return Response.status(201).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "LogOut", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),

    })
    @Path("/sessionOut")
    @Produces(MediaType.APPLICATION_JSON)
    public Response quitSession(@CookieParam("authToken") String authToken) {
        try{
            sesm.getSession(authToken);
            sesm.removeSession(authToken);
            return Response.status(201).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    //PART USERS MANAGER
    @DELETE
    @ApiOperation(value = "delete a User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),

    })
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@CookieParam("authToken") String authToken) {
        try{
            User u = sesm.getSession(authToken);
            User u1 = this.um.getUserFromUsername(u.getName());
            this.um.deleteUser(u1.getName());
            SessionManager.getInstance().removeSession(authToken);
            return Response.status(201).build();
        } catch (UserNotFoundException ex) {
            return Response.status(404).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "create a new User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "User Exists"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(User user,@CookieParam("authToken") String authToken) {

        if (user.getName()==null || user.getPassword()==null || user.getCorreo()==null)  return Response.status(500).entity(user).build();
        //user.setRandomId();
        try{
            this.sesm.getSession(authToken);
            this.um.addUser(user);
            this.sm.addUser(user);
            return Response.status(201).entity(user).build();
        }
        catch(UserRepeatedException ex){
            return Response.status(501).entity(user).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "get all Users", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),

    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            List<User> users = this.um.findAll();
            GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
            return Response.status(201).entity(entity).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @PUT
    @ApiOperation(value = "Modifica user", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),

    })
    @Path("/")
    public Response updateUser(User user, @CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            User u = this.um.updateUser(user);
            return Response.status(201).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(404).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "Get stats of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),

    })
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetStatsUser(@CookieParam("authToken") String authToken) {
        try{
            User u =SessionManager.getInstance().getSession(authToken);
            User usuario = this.um.getUserFromUsername(u.getName());
            return Response.status(201).entity(usuario).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch (Exception ex){
            return Response.status(500).build();
        }
    }

    @GET
    @ApiOperation(value = "User Gets Multiplicador", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
    })
    @Path("/GetMultiplicadorForCobre")
    @Produces(MediaType.APPLICATION_JSON)
    public Response UserGetsMultiplicador(@CookieParam("authToken") String authToken) {
        try{
            User u =SessionManager.getInstance().getSession(authToken);
            User usuario = this.um.getUserFromUsername(u.getName());
            Double precio = this.um.damePrecioCobre(usuario);
            return Response.status(201).entity(String.valueOf(precio)).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch(Exception ex)
        {
            return Response.status(500).build();
        }
    }
    @POST
    @ApiOperation(value = "User Updates Cobre", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= double.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),


    })
    @Path("/updateCobre/{NameUser}/{Cobre}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserUpdatesCobre(@PathParam("NameUser") String NameUser, @PathParam("Cobre") double Cobre,  @CookieParam("authToken") String authToken) {
        if(NameUser == null || Cobre == 0) return Response.status(500).build();
        try{
            this.sesm.getSession(authToken);
            User u = this.um.getUserFromUsername(NameUser);
            this.um.updateCobre(Cobre, u);
            return Response.status(201).entity(String.valueOf(u.getCobre())).build();
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "User sells all Cobre", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= User.class),
            @ApiResponse(code = 500, message = "Error, se quiere vender mas cobre del que el usuario tiene"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "User wants to sell 0 cobre"),
            @ApiResponse(code = 503, message = "User has no multiplicador"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/sellCobre/{KilosCobre}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response UserSellsCobre(@PathParam("KilosCobre") Double Cobre, @CookieParam("authToken") String authToken) {
        if(Cobre == 0) return Response.status(502).build();
        try{
            User u =SessionManager.getInstance().getSession(authToken);
            User usuario = this.um.getUserFromUsername(u.getName());
            this.um.updateMoney(usuario,Cobre);
            User useractualizado = this.um.getUserFromUsername(u.getName());
            return Response.status(201).entity(useractualizado).build();
        }
        catch(UserNotEnoughCobreException ex){
            return Response.status(500).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(501).build();
        }
        catch(UserHasNoMultiplicadorException ex){
            return Response.status(503).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @PUT
    @ApiOperation(value = "User wants to change the password", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "Actual password incorrect"),
            @ApiResponse(code = 503, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/ChangePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserChangePassword(ChangePassword passwords, @CookieParam("authToken") String authToken) {
        try{
            User u = sesm.getSession(authToken);
            String pass = this.um.getUserFromUsername(u.getName()).getPassword();
            if (pass.equals(passwords.getActualPassword())){
                this.um.changePassword(u,passwords.getNewPassword());
                return Response.status(201).build();
            }
            else {
                return Response.status(502).build();
            }
        }
        catch (UserNotFoundException ex)
        {
            return Response.status(503).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }

    }

    @GET
    @ApiOperation(value = "Recover Password", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 502, message = "Error sending the e-mail"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/RecoverPassword/{UserName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response RecoverPassword( @PathParam("UserName") String UserName) {
        if(UserName == null)  return Response.status(500).build();
        try{
            User u = this.um.getUserFromUsername(UserName);
            this.um.RecoverPassword(u);
            return Response.status(201).build()  ;
        }
        catch(UserNotFoundException ex)
        {
            return Response.status(501).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch (Exception ex)
        {
            return Response.status(502).build();
        }
    }

    @PUT
    @ApiOperation(value = "User wants to change the mail", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 503, message = "User has the wrong code"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/ChangeMail/{NewCorreo}/{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserChangeMail(@PathParam("NewCorreo") String NewCorreo, @PathParam("code") String code, @CookieParam("authToken") String authToken) {
        try {
            User u = sesm.getSession(authToken);
            this.um.changeCorreo(u, NewCorreo, code);
            return Response.status(201).build();
        } catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        } catch (WrongCodeException ex) {
            logger.warn("Attention, user has the wrong code");
            return Response.status(503).build();
        }
    }

    @GET
    @ApiOperation(value = "Get Code", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "Error sending the e-mail"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/GetCode")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCode( @CookieParam("authToken") String authToken) {
        try{
            User u = this.sesm.getSession(authToken);
            this.um.getCodeForCorreoChange(u);
            return Response.status(201).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch (Exception ex)
        {
            return Response.status(502).build();
        }
    }

    private String generateRandomSessionId() {
        return java.util.UUID.randomUUID().toString();  // Genera un UUID aleatorio como token de sesión
    }
}
