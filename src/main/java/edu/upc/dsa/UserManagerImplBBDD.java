package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import edu.upc.dsa.orm.FactorySession;
import edu.upc.dsa.orm.SessionBD;
import edu.upc.dsa.orm.util.ObjectHelper;
import edu.upc.dsa.util.RandomUtils;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;


public class UserManagerImplBBDD implements UserManager {
    private static UserManager instance;
    protected HashMap<String, Double> multiplicadors;
    final static Logger logger = Logger.getLogger(UserManagerImplBBDD.class);
    protected HashMap<String, String> codes;
    SessionBD sessionBD;
    private RandomUtils rdu;
    private UserManagerImplBBDD() {
        this.multiplicadors = new HashMap<>();
        codes = new HashMap<>();
    }

    public static UserManager getInstance() {
        if (instance==null) instance = new UserManagerImplBBDD();
        return instance;
    }

    public int size() {
        sessionBD = FactorySession.openSession();
        int ret = sessionBD.findAll(User.class).size();
        logger.info("size " + ret);
        sessionBD.close();
        return ret;
    }

    public User addUser(User u) throws UserRepeatedException {
        sessionBD = FactorySession.openSession();
        logger.info("new User " + u);
        if(sessionBD.get(u.getClass(),"correo",u.getCorreo()) == null)
        {
            sessionBD.save(u);
            logger.info("new User added");
            sessionBD.close();
            return u;
        }
        else{
            logger.warn("User already exists with that correu");
            sessionBD.close();
            throw new UserRepeatedException();
        }
    }

    public User addUser(String user, String password,  String mail) throws UserRepeatedException{
        return this.addUser(user, password, mail);
    }

    public User getUserFromUsername(String _username) throws UserNotFoundException{
        sessionBD = FactorySession.openSession();
        logger.info("getUser("+_username+")");
        User u = (User) sessionBD.get(User.class,"name", _username);
        if(u!= null) return u;
        logger.warn("not found " + _username);
        sessionBD.close();
        throw new UserNotFoundException();
    }


    public List<User> findAll() {
        sessionBD = FactorySession.openSession();
        sessionBD.close();
        return sessionBD.findAll(User.class);
    }

    @Override
    public void deleteUser(String userName) throws UserNotFoundException{
        sessionBD = FactorySession.openSession();
        User u = (User) sessionBD.get(User.class,"name", userName);
        if (u==null) {
            logger.warn("not found " + u);
        }
        else sessionBD.delete(User.class,"name",userName);
        sessionBD.close();
    }
    public void updateCobre(double cobre, User user)throws UserNotFoundException{
        user.setCobre(cobre + user.getCobre());
        this.updateUser(user);
    };
    public double updateMoney(User user, double kilocobre) throws UserNotEnoughCobreException, UserHasNoMultiplicadorException{
        sessionBD = FactorySession.openSession();
        if (multiplicadors.containsKey(user.getName())) {
            if(user.getCobre() >= kilocobre){
                double resultat = user.getMoney() + kilocobre*multiplicadors.get(user.getName());
                user.setMoney(resultat);
                user.setCobre(user.getCobre()-kilocobre);
                sessionBD.update(user,"correo",user.getCorreo());
                sessionBD.close();
                return resultat;
            }
            else {
                sessionBD.close();
                throw new UserNotEnoughCobreException();
            }
        }
        else {
            sessionBD.close();
            throw new UserHasNoMultiplicadorException();
        }
    };
    public double damePrecioCobre(User user){
        double random = Math.random();
        double multiplicador = 1 + Math.log(1 + (9 * random));
        double arrodonit = Math.round(multiplicador*10.0)/10.0;
        multiplicadors.put(user.getName(), arrodonit);
        return arrodonit;
    };


    @Override
    public User updateUser(User u) throws UserNotFoundException{
        sessionBD = FactorySession.openSession();
        User t = (User) sessionBD.get(u.getClass(),"correo",u.getCorreo());
        if (t!=null) {
            logger.info(u+" rebut!!!! ");
            sessionBD.update(u,"correo", u.getCorreo());
            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+u);
        }
        sessionBD.close();
        return t;
    }
    public void clear() {
        sessionBD = FactorySession.openSession();
        sessionBD.deleteAll(User.class);
        sessionBD.close();
    }

    public void changePassword(User user, String pswd){
        sessionBD = FactorySession.openSession();
        user.setPassword(pswd);
        sessionBD.update(user,"correo",user.getCorreo());
        sessionBD.close();
    };
    public void RecoverPassword(User user) throws Exception{
        sessionBD = FactorySession.openSession();
        String host = "smtp.gmail.com";
        final String fromEmail = "correuperdsa@gmail.com";
        final String password = "eqqq ymrx grbg htld";
        User u1 = (User) sessionBD.get(user.getClass(),"correo", user.getCorreo());
        String toEmail = u1.getCorreo();
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Crear el missatge
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Recuperació de contrasenya Per RobaCobres");

        // Cos del text del correu
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Hola " + user.getName() + ",\n\nLa teva contrasenya és: " + user.getPassword() + "\n\nIntenta no tornara a oblidar-ho, olvidonaaa!");

        // Adjuntar imatge
        MimeBodyPart imagePart = new MimeBodyPart();
        File imageFile = new File("public/Olvidonaa.jpg");
        imagePart.attachFile(imageFile);
        imagePart.setFileName("Olvidonaa.jpg"); // Nom de l'arxiu al correu

        // Crear el contingut multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart); // Afegir el text
        multipart.addBodyPart(imagePart); // Afegir la imatge

        // Assignar el contingut al missatge
        message.setContent(multipart);

        // Enviar el correu
        Transport.send(message);
        sessionBD.close();
    }

    public void changeCorreo(User user, String correo, String code) throws WrongCodeException {
        sessionBD = FactorySession.openSession();
        if(codes.get(user.getName()).equals(code)){
            String correoInicial = user.getCorreo();
            user.setCorreo(correo);
            sessionBD.update(user,"correo",correoInicial);
            sessionBD.close();
        }
        else {
            sessionBD.close();
            throw new WrongCodeException();
        }
    }

    public void getCodeForCorreoChange(User u)throws Exception{
        sessionBD = FactorySession.openSession();
        String code = rdu.getCode();
        codes.put(u.getName(),code);
        String host = "smtp.gmail.com";
        final String fromEmail = "correuperdsa@gmail.com";
        final String password = "eqqq ymrx grbg htld";
        String toEmail = u.getCorreo();

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Crear el missatge
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Recuperació de contrasenya Per RobaCobres");

        // Cos del text del correu
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Hola el teu codi és: "+code);


        // Crear el contingut multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart); // Afegir el text

        // Assignar el contingut al missatge
        message.setContent(multipart);

        // Enviar el correu
        Transport.send(message);
        sessionBD.close();
    }

    public void ponComentarioEnForum(User u, String comentario){
        sessionBD = FactorySession.openSession();
        sessionBD.save(new Forum(u.getName(),comentario));
        sessionBD.close();
    }

    public List<Forum> dameComentariosDelForum(){
        sessionBD = FactorySession.openSession();
        List<Forum> respuesta = (List<Forum>) sessionBD.findAll(Forum.class);
        sessionBD.close();
        return respuesta;

    }

    public List<User> dameUsuariosConLosQueMantengoChatIndividual(String name) {
        sessionBD = FactorySession.openSession();
        List<User> respuesta = new ArrayList<>();
        List<String> nombres1 = new ArrayList<>();
        HashMap<String, String> condiciones = new HashMap<>();
        condiciones.put("nameTo = ",name);
        condiciones.put("nameFrom = ",name);
        List<ChatIndividual> respuesta1 = (List<ChatIndividual>) sessionBD.findAllWithConditionsOR(ChatIndividual.class, condiciones);
        List<String> nombres = new ArrayList<>();
        for (ChatIndividual chat : respuesta1) {
            if(!nombres.contains(chat.getNameTo()) && !chat.getNameTo().equals(name)){
                respuesta.add(new User(chat.getNameTo(),null,null));
                nombres.add(chat.getNameTo());
            }
            if(!nombres.contains(chat.getNameFrom()) && !chat.getNameFrom().equals(name)){
                respuesta.add(new User(chat.getNameFrom(),null,null));
                nombres.add(chat.getNameFrom());
            }
        }
        sessionBD.close();
        return respuesta;
    }

    public List<ChatIndividual> ponComentarioEnChatPrivado(ChatIndividual chatIndividual){
        sessionBD = FactorySession.openSession();
        List<User> users = (List<User>) sessionBD.findAll(User.class);
        for(User u:users) if(u.getName().equals(chatIndividual.getNameTo())){
            sessionBD.save(chatIndividual);
            sessionBD.close();
            return this.getChatsIndividuales(chatIndividual.getNameFrom(), chatIndividual.getNameTo());
        }
        sessionBD.close();
        return null;
    };

    public List<ChatIndividual> getChatsIndividuales(String nombre1, String nombre2){
        sessionBD = FactorySession.openSession();
        HashMap<String,String> condiciones = new HashMap<>();
        condiciones.put("nameFrom = ",nombre1);
        condiciones.put("nameTo = ",nombre2);
        List<ChatIndividual> respuesta = (List<ChatIndividual>) sessionBD.findAllWithConditionsAND(ChatIndividual.class, condiciones);
        HashMap<String,String> condiciones2 = new HashMap<>();
        condiciones.put("nameFrom = ",nombre2);
        condiciones.put("nameTo = ",nombre1);
        List<ChatIndividual> respuesta2 = (List<ChatIndividual>) sessionBD.findAllWithConditionsAND(ChatIndividual.class, condiciones);
        int k = 12;
        respuesta.addAll(respuesta2);
        respuesta.sort(Comparator.comparingInt(ChatIndividual::getID));
        sessionBD.close();
        return respuesta;
    };

    public void ponInsigniaParaUsuario(Insignia insignia, User user){
        sessionBD = FactorySession.openSession();
        sessionBD.save(insignia);
        Insignia i = (Insignia) sessionBD.get(Insignia.class, "name", insignia.getName());
        User u = (User) sessionBD.get(User.class,"name",user.getName());
        sessionBD.save(new InsigniaRelaciones(u.getID(),i.getID()));
        sessionBD.close();
    }

    public List<Insignia> getAllInsignias(User user){
        try{
            List<Insignia> respuesta = new ArrayList<>();
            sessionBD = FactorySession.openSession();
            Connection con = sessionBD.dameConnection();
            User u = (User) sessionBD.get(User.class, "name", user.getName());
            String query = "SELECT Insignia.* FROM Insignia JOIN Insigniarelaciones ON Insignia.ID = InsigniaRelaciones.ID_Insignia WHERE InsigniaRelaciones.ID_User = "+u.getID() +";";
            PreparedStatement pstm = null;
            pstm = con.prepareStatement(query);
            ResultSet res = pstm.executeQuery();
            while (res.next()) {
                ResultSetMetaData rsmd = res.getMetaData();

                int numColumns = rsmd.getColumnCount();
                int i = 1;
                Insignia in = new Insignia();
                while (i < numColumns + 1) {
                    String key = rsmd.getColumnName(i);
                    Object value = res.getObject(i);
                    if (key.equals("money") || key.equals("cobre")){
                        BigDecimal value1 = (BigDecimal) value;
                        value = value1.setScale(2, RoundingMode.DOWN).doubleValue(); // Truncar a 2 decimals
                    }
                    ObjectHelper.setter(in, key, value);
                    i++;
                }
                respuesta.add(in);
            }
            if(respuesta.isEmpty()) {
                logger.warn("Attention, no insignia found");
            }
            sessionBD.close();
            return respuesta;
        }
        catch(Exception ex){
            logger.warn("Attention, Exception in the getAllInsignias query");
            sessionBD.close();
            return null;
        }
    }
    public String getPartidasMaxPuntuacion(User user) throws QueryErrorException, NoDataException{
        sessionBD = FactorySession.openSession();
        String query = "SELECT * FROM Partidas WHERE ID_Jugador = "+ user.getID() +" AND PuntuacionMax = ( SELECT MAX(PuntuacionMax) FROM Partidas WHERE ID_Jugador = "+ user.getID() +")";
        List<Partidas> respuesta = (List<Partidas>)sessionBD.query(query,Partidas.class);
        if(respuesta == null) throw new QueryErrorException();
        if(respuesta.isEmpty()) throw new NoDataException();
        String puntuacionMax = String.valueOf(respuesta.get(0).getPuntuacionMax());
        sessionBD.close();
        return puntuacionMax;
    }
    public List<Ranking> getRanking() throws QueryErrorException, NoDataException {
        sessionBD = FactorySession.openSession();
        String query = "SELECT User.name AS UserName, Partidas.PuntuacionMax AS MaxPuntuacion FROM Partidas JOIN User ON User.ID = Partidas.ID_Jugador WHERE (Partidas.ID_Jugador, Partidas.PuntuacionMax) IN ( SELECT ID_Jugador, MAX(PuntuacionMax) FROM Partidas GROUP BY ID_Jugador ) ORDER BY Partidas.PuntuacionMax DESC";
        List<Ranking> respuesta = (List<Ranking>)sessionBD.query(query,Ranking.class);
        if(respuesta == null) throw new QueryErrorException();
        if(respuesta.isEmpty()) throw new NoDataException();
        sessionBD.close();
        return respuesta;
    }

    public void SendRanking(User user) throws Exception{
        sessionBD = FactorySession.openSession();
        String host = "smtp.gmail.com";
        final String fromEmail = "correuperdsa@gmail.com";
        final String password = "eqqq ymrx grbg htld";
        User u1 = (User) sessionBD.get(user.getClass(),"correo", user.getCorreo());
        String toEmail = u1.getCorreo();
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // Crear el missatge
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Ranking");

        // Cos del text del correu
        MimeBodyPart textPart = new MimeBodyPart();
        List<Ranking> ranking = getRanking();

        // Construir contingut
        if (ranking == null || ranking.isEmpty()) {
            textPart.setText("No hay jugadores en el ranking.");
        } else {
            StringBuilder rankingTexto = new StringBuilder();
            rankingTexto.append("Ranking de Jugadores:\n\n");

            for (int i = 0; i < ranking.size(); i++) {
                Ranking jugador = ranking.get(i);
                rankingTexto.append(String.format("%d. %s - %.2f puntos\n",
                        i + 1,
                        jugador.getUsername(),
                        jugador.getMaxpuntuacion()
                ));
            }

            textPart.setText(rankingTexto.toString());
        }

        // Crear el contingut multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart); // Afegir el text

        // Assignar el contingut al missatge
        message.setContent(multipart);

        // Enviar el correu
        Transport.send(message);
        sessionBD.close();
    }

    public List<Video> getmedia() throws QueryErrorException, NoDataException {
        sessionBD = FactorySession.openSession();
        List<Video> respuesta = (List<Video>)sessionBD.findAll(Video.class);
        if(respuesta == null) throw new QueryErrorException();
        if(respuesta.isEmpty()) throw new NoDataException();
        sessionBD.close();
        return respuesta;
    }

    public void guardarPartidaActual(PartidaActual partida){
        sessionBD = FactorySession.openSession();
        if(this.sessionBD.get(PartidaActual.class, "UserName",partida.getUserName()) == null)this.sessionBD.save(partida);
        else this.sessionBD.update(partida, "UserName", partida.getUserName());
        sessionBD.close();
    }

    public PartidaActual getPartidaActual(User user){
        sessionBD = FactorySession.openSession();
        PartidaActual partida = (PartidaActual) this.sessionBD.get(PartidaActual.class, "UserName",user.getName());
        sessionBD.close();
        return partida;
    }
    public void ponPartida(User u, double puntuacion){
        sessionBD = FactorySession.openSession();
        Partidas part=new Partidas();
        part.setID_Jugador(u.getID());
        part.setPuntuacionMax(puntuacion);
        sessionBD.save(part);
        sessionBD.close();
    }
}