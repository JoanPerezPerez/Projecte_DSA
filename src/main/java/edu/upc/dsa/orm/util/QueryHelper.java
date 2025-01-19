package edu.upc.dsa.orm.util;

import edu.upc.dsa.models.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryHelper {

    public static String createQueryINSERTWithID(Object entity) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
        sb.append("(");

        String [] fields = ObjectHelper.getFields(entity);

        sb.append("ID");
        for (String field: fields) {
            if (!field.equals("ID")) sb.append(", ").append(field);
        }
        sb.append(") VALUES (?");

        for (String field: fields) {
            if (!field.equals("ID"))  sb.append(", ?");
        }
        sb.append(")");
        // INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?)
        return sb.toString();
    }

    public static String createQueryINSERT(Object entity) {

        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" ").append("(");

        String [] fields = ObjectHelper.getFields(entity);
        int i = 0;
        for (String field: fields) {
            sb.append(field);
            if(i< fields.length-1) sb.append(", ");
            i++;
        }
        sb.append(") VALUES (");
        i=0;
        for (String field: fields) {
            if (!field.equals("ID"))  sb.append("?");
            if(i< fields.length-1) sb.append(", ");
            i++;
        }
        sb.append(")");
        // INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?)
        return sb.toString();
    }
    public static String createQuerySELECT(Class theClass, Object keyQ){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName());
        sb.append(" WHERE "+keyQ+" = ?");

        return sb.toString();
    }
    public static String createQuerySELECTWithID(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE ID = ?");

        return sb.toString();
    }
    public static String createQuerySELECTWithName(Class theClass) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName());
        sb.append(" WHERE name = ?");

        return sb.toString();
    }
    public static String createQuerySELECTWithCorreo(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE correo = ?");
        return sb.toString();
    }
    public static String createQuerySELECTALL(Class theClass) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName()).append(";");
        return sb.toString();
    }
    public static String createQueryDELETE(Class theClass, Object Key) {
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM ").append(theClass.getSimpleName());
        sb.append(" WHERE "+Key+" = ?");
        return sb.toString();
    }
    public static String createQueryDELETEWithName(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE name = ?");
        return sb.toString();
    }
    public static String createQueryDELETEALL(Class theClass) {
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM ").append(theClass.getSimpleName());
        return sb.toString();
    }
    public static String createSelectIDWhereNotIn(Class theClass) {
        //SELECT * FROM Item WHERE ID NOT IN (SELECT ID_Item FROM UserItemCharacterRelation WHERE ID_User = <ID_DEL_USUARIO>);
        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE ID NOT IN(SELECT ID_" + theClass.getSimpleName()+ " FROM UserItemCharacterRelation WHERE ID_User = ? AND ID_"+ theClass.getSimpleName()+ " IS NOT NULL)");
        return sb.toString();
    }
    public static String createSelectFindAllAND(Class theClass, HashMap<Object, Object> params) {

        Set<Map.Entry<Object, Object>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE ");
        int i = 0;
        for (Object key: params.keySet()) {
            sb.append(key + " ?");
            if(i<params.size()-1)sb.append(" AND ");
            i++;
        }
        return sb.toString();
    }

    public static String createSelectFindAllOR(Class theClass, HashMap<Object, Object> params) {

        Set<Map.Entry<Object, Object>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE ");
        int i = 0;
        for (Object key: params.keySet()) {
            sb.append(key + " ?");
            if(i<params.size()-1)sb.append(" OR ");
            i++;
        }
        return sb.toString();
    }


    public static String createQueryUPDATEWithID(Object entity) {

        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
        sb.append("SET");

        String [] fields = ObjectHelper.getFields(entity);
        int i = 0;
        for (String field: fields) {
            if (!field.equals("ID")) sb.append(" ").append(field).append(" = ? ");
            if (i<fields.length-1) sb.append(",");
            i++;
        }

        sb.append(" WHERE ID = ? ;");
        // INSERT INTO User (ID, lastName, firstName, address, city) VALUES (0, ?, ?, ?,?)
        return sb.toString();
    }
    public static String createQueryUPDATEWithName(Object entity){
        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
        sb.append("SET");

        String [] fields = ObjectHelper.getFields(entity);
        int i = 0;
        for (String field: fields) {
            if (!field.equals("ID")) sb.append(" ").append(field).append(" = ? ");
            if (i<fields.length-1) sb.append(",");
            i++;
        }

        sb.append(" WHERE name = ? ;");
        return sb.toString();
    }
    public static String createQueryUPDATE(Object object, Object Key) {

        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(object.getClass().getSimpleName()).append(" ");
        sb.append("SET");
        String [] fields = ObjectHelper.getFields(object);
        int i = 0;
        for (String field: fields) {
            sb.append(" ").append(field).append(" = ? ");
            if (i<fields.length-1) sb.append(",");
            i++;
        }

        sb.append(" WHERE "+Key+" = ? ;");
        return sb.toString();
    }
    public static String createQueryGetWithRelations(Class theClass){
        StringBuffer sb = new StringBuffer("SELECT * FROM useritemcharacterrelation INNER JOIN ");
        sb.append(theClass.getSimpleName());
        sb.append(" ON useritemcharacterrelation.ID_").append(theClass.getSimpleName()).append(" = ").append(theClass.getSimpleName()).append(".ID WHERE useritemcharacterrelation.ID_User = ?");
        //Query resultant amb item:
        //SELECT name FROM useritemcharacterrelation INNER JOIN Item ON useritemcharacterrelation.ID_Item = Item.ID WHERE useritemcharacterrelation.ID_User = ?;
        return sb.toString();
    }



}
