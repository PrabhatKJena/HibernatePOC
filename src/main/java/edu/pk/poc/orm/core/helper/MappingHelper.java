package edu.pk.poc.orm.core.helper;

import edu.pk.poc.orm.core.annotaion.ORMColumn;
import edu.pk.poc.orm.core.annotaion.ORMEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingHelper {
    Map<Class, EntityMetaData> entityMetaDataMap;

    public Map<Class, EntityMetaData> getEntityMetaDataMap() {
        return entityMetaDataMap;
    }

    /**
     * Introspecting Class mets data
     * @param classes
     */
    public void introspect(List<Class> classes) {
        for (Class klass : classes) {
            entityMetaDataMap = new HashMap<>();
            EntityMetaData metaData = getMetaData(klass);
            entityMetaDataMap.put(klass, metaData);
        }
        //System.out.println(entityMetaDataMap);
    }

    private EntityMetaData getMetaData(Class klass) {
        EntityMetaData metaData = new EntityMetaData();
        String tName = ((ORMEntity) klass.getDeclaredAnnotation(ORMEntity.class)).tableName();
        metaData.setTableName(tName);

        Map<String, String> columnMapping = new HashMap<>();
        for (Field field : klass.getDeclaredFields()) {
            ORMColumn annotation = field.getDeclaredAnnotation(ORMColumn.class);
            if (annotation.isId()) {
                metaData.setIdColumnName(annotation.name());
                metaData.setIdFieldName(field.getName());
            }
            columnMapping.put(field.getName(), annotation.name());
        }
        metaData.setColumnMapping(columnMapping);

        return metaData;
    }

    /**
     * Generate INSERT Query for the given Entity Object
     * @param object
     * @return
     */
    public String generateInsertSQL(Object object) {
        Class klass = object.getClass();
        EntityMetaData metaData = entityMetaDataMap.get(klass);
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(metaData.getTableName());
        Map<String, String> columnMapping = metaData.getColumnMapping();
        String colStr = "(", valStr = "(";
        for (Map.Entry<String, String> cm : columnMapping.entrySet()) {
            colStr += (cm.getValue() + ",");
            valStr += (getVal(object, cm.getKey()) + ",");
    }
        colStr = colStr.substring(0, colStr.length() - 1) + ")";
        valStr = valStr.substring(0, valStr.length() - 1) + ")";
        sql.append(colStr).append(" VALUES ").append(valStr);
        return sql.toString();
    }

    private String getVal(Object obj, String fieldName) {
        String val = null;
        try {
            Method method = obj.getClass().getMethod(resolveMehodName(fieldName, "get"));
            Object result = method.invoke(obj);
            if (result instanceof Integer)
                val = String.valueOf(result);
            else if (result instanceof String)
                val = "'" + result + "'";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    /**
     * prepare the method name by adding prefix
     * @param fieldName
     * @param prefix
     * @return
     */
    public String resolveMehodName(String fieldName, String prefix) {
        return prefix + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    /**
     * Generate SQL to fetch record by ID column
     * @param pkValue
     * @param departmentEntityClass
     * @return
     */
    public String generateFindByIdSQL(Object pkValue, Class departmentEntityClass) {
        EntityMetaData metaData = entityMetaDataMap.get(departmentEntityClass);
        String sql = "SELECT * FROM " + metaData.getTableName() + " WHERE " + metaData.getIdColumnName() + " =";
        try {
            Class<?> type = departmentEntityClass.getDeclaredField(metaData.getIdFieldName()).getType();
            if (type.isInstance(String.class))
                sql += ("'" + pkValue + "'");
            else
                sql += pkValue;
        } catch (NoSuchFieldException e) {
            System.out.println(e);
        }
        return sql;
    }

    public String generateUpdateSQL(Object object) {
        EntityMetaData metaData = entityMetaDataMap.get(object.getClass());
        String sql = "UPDATE " + metaData.getTableName() + " SET ";
        try {
            Map<String, String> columnMapping = metaData.getColumnMapping();
            for (Map.Entry<String, String> cm : columnMapping.entrySet()) {
                sql += cm.getKey() + "=" + getVal(object, cm.getKey())+",";
            }
            sql = sql.substring(0, sql.length() - 1);

            // Generating WHERE clause
            sql+= " WHERE " + metaData.getIdColumnName() + " = ";
            String pkValue = null;
            pkValue = getVal(object, metaData.getIdFieldName());
            sql+=pkValue;
        } catch (Exception e) {
            System.out.println(e);
        }
        return sql;
    }

    public String generateDeleteSQL(Object entity){
        EntityMetaData metaData = entityMetaDataMap.get(entity.getClass());
        String sql = "DELETE FROM " + metaData.getTableName() + " WHERE " +metaData.getIdColumnName() + " = "
                + getVal(entity, metaData.getIdFieldName());
        return sql;
    }
    public void safeClose(ResultSet rs, Statement st, Connection con) {
        try {
            if (rs != null && !rs.isClosed())
                rs.close();
            if (st != null && !st.isClosed())
                st.close();
            if (con != null && !con.isClosed())
                con.close();
        } catch (Exception e) {
        }
    }
}
