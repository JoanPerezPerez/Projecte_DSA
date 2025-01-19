package edu.upc.dsa.orm;


import edu.upc.dsa.DBUtils;

import java.sql.*;

public class FactorySession {

    public static SessionBD openSession() {
        Connection conn = getConnection();
        SessionBD sessionBD = new SessionImpl(conn);
        return sessionBD;
    }



    public static Connection getConnection()  {
        String db = DBUtils.getDb();
        String host = DBUtils.getDbHost();
        String port = DBUtils.getDbPort();
        String user = DBUtils.getDbUser();
        String pass = DBUtils.getDbPasswd();


        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+
                    db+"?user="+user+"&password="+pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static SessionBD openSession(String url, String user, String password) {
        return null;
    }

}
