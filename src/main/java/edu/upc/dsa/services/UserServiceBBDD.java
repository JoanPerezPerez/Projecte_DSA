package edu.upc.dsa.services;

import edu.upc.dsa.*;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Api(value = "/users", description = "Endpoint to Users Service with Data Base")
@Path("/users")
public class UserServiceBBDD {
    //test
    private ItemManager im;
    private StoreManager sm;
    private UserManager um;
    private CharacterManager cm;
    private SessionManager sesm;
    final static Logger logger = Logger.getLogger(UserService.class);
    public UserServiceBBDD() {
        this.im = new ItemManagerImplBBDD();
        this.sm = StoreManagerImplBBDD.getInstance();
        this.um = UserManagerImplBBDD.getInstance();
        this.cm = CharacterManagerImplBBDD.getInstance();
        this.sesm = SessionManager.getInstance();
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
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/sessionCheck")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSession(@CookieParam("authToken") String authToken) {
        try{
            if (sesm.getSession(authToken)==null)
                return Response.status(501).build() ;
            else
                return Response.status(201).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
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
            SessionManager.getInstance().removeSession(authToken);
            return Response.status(201).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
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
    public Response deleteUser(@CookieParam("authToken") String authToken) {
        try{
            User user = this.sesm.getSession(authToken);
            this.um.deleteUser(user.getName());
            return Response.status(201).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(404).build();
        }
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "create a new User", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Validation Error"),
            @ApiResponse(code = 501, message = "User Exists")
            })
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(User user) {

        if (user.getName()==null || user.getPassword()==null)  return Response.status(500).entity(user).build();
        try{
            this.um.addUser(user);
            return Response.status(201).entity(user).build();
        }
        catch(UserRepeatedException ex){
            return Response.status(501).entity(user).build();
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
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
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
        catch(UserNotLoggedInException ex){
            logger.warn("Attention, User not yet logged");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "Get stats of user", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 505, message = "User not logged"),
            @ApiResponse(code = 506, message = "User not logged"),
    })
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetStatsUser(@CookieParam("authToken") String authToken) {
        try{
            User u = this.sesm.getSession(authToken);
            User u1 = this.um.getUserFromUsername(u.getName());
            return Response.status(201).entity(u1).build();
        }
        catch(UserNotFoundException ex){
            return Response.status(505).build();
        }
        catch (UserNotLoggedInException ex){
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "User Gets Multiplicador", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 501, message = "User not found"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/GetMultiplicadorForCobre")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserGetsMultiplicador(@CookieParam("authToken") String authToken) {
        try{
            User u =  this.sesm.getSession(authToken);
            Double precio = this.um.damePrecioCobre(u);
            return Response.status(201).entity(String.valueOf(precio)).build();
        }
        catch(UserNotLoggedInException ex)
        {
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
            User us = this.um.getUserFromUsername(u.getName());
            this.um.updateMoney(us,Cobre);
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
    @ApiOperation(value = "User Wants to change password", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 506, message = "User Not logged in yet")
    })
    @Path("/ChangePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response UserChangesPassword(ChangePassword passwords, @PathParam("UserName") String UserName, @CookieParam("authToken") String authToken) {
        try{
            User u = sesm.getSession(authToken);
            String pass = u.getPassword();
            if (pass.equals(passwords.getActualPassword())){
                this.um.changePassword(u,passwords.getNewPassword());
                return Response.status(201).build();
            }
            else {
                return Response.status(502).build();
            }
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
            @ApiResponse(code = 502, message = "Error sending the e-mail")
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

    @POST
    @ApiOperation(value = "Get Forum Messages", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Forum.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "No Forum Messages"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/GetForum")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForum( @CookieParam("authToken") String authToken) {
        try{
            User u = this.sesm.getSession(authToken);
            List<Forum> lista = this.um.dameComentariosDelForum();
            if(lista.isEmpty()) return Response.status(502).build();
            GenericEntity<List<Forum>> entity = new GenericEntity<List<Forum>>(lista) {};
            return Response.status(201).entity(entity).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch (Exception ex)
        {
            return Response.status(500).build();
        }
    }


    @POST
    @ApiOperation(value = "Post A Forum Messages", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful",response = Forum.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/PostInForum")
    @Produces(MediaType.APPLICATION_JSON)
    public Response PostInForum( @CookieParam("authToken") String authToken, Forum forum) {
        try{
            User u = this.sesm.getSession(authToken);
            this.um.ponComentarioEnForum(u, forum.getComentario());
            List<Forum> lista = this.um.dameComentariosDelForum();
            GenericEntity<List<Forum>> entity = new GenericEntity<List<Forum>>(lista) {};
            return Response.status(201).entity(entity).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch (Exception ex)
        {
            return Response.status(500).build();
        }
    }

    @POST
    @ApiOperation(value = "Get Pepople With Whom I Have Private Messages", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "No Forum Messages"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/PrivateNames")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivateMessagesNames( @CookieParam("authToken") String authToken) {
        try {
            User u = this.sesm.getSession(authToken);
            List<User> users = this.um.dameUsuariosConLosQueMantengoChatIndividual(u.getName());
            GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
            return Response.status(201).entity(entity).build();
        }
        catch (UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "Get Private Messages With Someone", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "No Forum Messages"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/PrivateMessagesWith/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivateMessages( @CookieParam("authToken") String authToken,@PathParam("name") String name) {
        try {
            User u = this.sesm.getSession(authToken);
            List<ChatIndividual> chatIndividual = this.um.getChatsIndividuales(u.getName(),name);
            GenericEntity<List<ChatIndividual>> entity = new GenericEntity<List<ChatIndividual>>(chatIndividual) {};
            return Response.status(201).entity(entity).build();
        }
        catch (UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "Post Private Messages", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= String.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "Error in the creation of the chat"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/PrivateChat/Post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPrivateMessages(@CookieParam("authToken") String authToken, ChatIndividual chat) {
        try{
            User u = this.sesm.getSession(authToken);
            List<ChatIndividual>respuesta = this.um.ponComentarioEnChatPrivado(chat);
            if(respuesta == null) return Response.status(502).build();
            GenericEntity<List<ChatIndividual>> entity = new GenericEntity<List<ChatIndividual>>(respuesta) {};
            return Response.status(201).entity(entity).build();
        }
        catch (UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }
    @POST
    @ApiOperation(value = "Get Insignias", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Insignia.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "No Forum Messages"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/{NameUser}/badges") //El identificador de usuario es la propia Cookie
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetInsignias(@CookieParam("authToken") String authToken, @PathParam("NameUser") String NameUser) {
        try{
            User u = this.sesm.getSession(authToken);
            List<Insignia> respuesta = this.um.getAllInsignias(u);
            GenericEntity<List<Insignia>> entity = new GenericEntity<List<Insignia>>(respuesta) {};
            return Response.status(201).entity(entity).build();
        }
        catch (UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @POST
    @ApiOperation(value = "Post Insignias", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= Insignia.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "No Forum Messages"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/{NameUser}/save/badges") //El identificador de usuario es la propia Cookie
    @Produces(MediaType.APPLICATION_JSON)
    public Response PostInsignias(@CookieParam("authToken") String authToken, @PathParam("NameUser") String NameUser, Insignia insignia) {
        try{
            User u = this.sesm.getSession(authToken);
            this.um.ponInsigniaParaUsuario(insignia,u);
            List<Insignia> respuesta = this.um.getAllInsignias(u);
            GenericEntity<List<Insignia>> entity = new GenericEntity<List<Insignia>>(respuesta) {};
            return Response.status(201).entity(entity).build();
        }
        catch (UserNotLoggedInException ex){
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
    }

    @GET
    @ApiOperation(value = "Get Max Puntuacion", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = String.class),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "No data available"),
            @ApiResponse(code = 508, message = "Error in query"),
    })
    @Path("/GetMaxPuntuacion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMaxPuntuacion( @CookieParam("authToken") String authToken) {
        try{
            User u = this.sesm.getSession(authToken);
            String p = this.um.getPartidasMaxPuntuacion(u);
            return Response.status(201).entity(p).build();
        }
        catch (QueryErrorException ex) {
            logger.warn("Attention, error in query");
            return Response.status(508).build();
        }
        catch (NoDataException ex) {
            logger.warn("Attention, no data available");
            return Response.status(502).build();
        }
        catch (Exception ex)
        {
            return Response.status(500).build();
        }
    }

    @GET
    @ApiOperation(value = "Get Ranking", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Ranking.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "No data available"),
            @ApiResponse(code = 508, message = "Error in query"),

    })
    @Path("/GetRanking")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRanking( @CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            List<Ranking> respuesta = this.um.getRanking();
            GenericEntity<List<Ranking>> entity = new GenericEntity<List<Ranking>>(respuesta) {};
            return Response.status(201).entity(entity).build();
        }
        catch (QueryErrorException ex) {
            logger.warn("Attention, error in query");
            return Response.status(508).build();
        }
        catch (NoDataException ex) {
            logger.warn("Attention, no data available");
            return Response.status(502).build();
        }
        catch (Exception ex)
        {
            return Response.status(500).build();
        }
    }

    @GET
    @ApiOperation(value = "Get Mail ranking", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 506, message = "User Not logged in yet"),
    })
    @Path("/GetMailRanking")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMailRanking( @CookieParam("authToken") String authToken) {
        try{
            User u = this.sesm.getSession(authToken);
            this.um.SendRanking(u);
            return Response.status(201).build();
        }
        catch (UserNotLoggedInException ex) {
            logger.warn("Attention, user not logged in yet");
            return Response.status(506).build();
        }
        catch (Exception ex)
        {
            return Response.status(500).build();
        }
    }

    @GET
    @ApiOperation(value = "get all media", notes = "hello")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Video.class, responseContainer="List"),
            @ApiResponse(code = 500, message = "Error"),
            @ApiResponse(code = 502, message = "No data available"),
            @ApiResponse(code = 508, message = "Error in query"),
    })
    @Path("/media")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedia(@CookieParam("authToken") String authToken) {
        try{
            this.sesm.getSession(authToken);
            List<Video> respuesta = this.um.getmedia();
            GenericEntity<List<Video>> entity = new GenericEntity<List<Video>>(respuesta) {};
            return Response.status(201).entity(entity).build();
        }
        catch (QueryErrorException ex) {
            logger.warn("Attention, error in query");
            return Response.status(508).build();
        }
        catch (NoDataException ex) {
            logger.warn("Attention, no data available");
            return Response.status(502).build();
        }
        catch (Exception ex)
        {
            return Response.status(500).build();
        }

    }

}
