package edu.pk.poc.orm.core;

import edu.pk.poc.orm.core.helper.EntityMetaData;
import edu.pk.poc.orm.core.helper.MappingHelper;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class Session {
    Connection connection;
    MappingHelper mappingHelper;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setMappingHelper(MappingHelper mappingHelper) {
        this.mappingHelper = mappingHelper;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mappingHelper = null;
    }

    /**
     * Persist Given Entity
     * @param entity
     */
    public void persist(Object entity) {
        String sql = mappingHelper.generateInsertSQL(entity);
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e);
        }finally {
            mappingHelper.safeClose(null, statement, null);
        }
    }

    /**
     * Get Entity provided by Primary Key value and Entity name
     * @param pkValue
     * @param entityClass
     * @return
     */
    public <T> T get(Class<T> entityClass, Object pkValue) {
        String sql = mappingHelper.generateFindByIdSQL(pkValue, entityClass);
        T entity = null;
        Statement statement = null;
        try {
            entity = entityClass.newInstance();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            EntityMetaData metaData = mappingHelper.getEntityMetaDataMap().get(entityClass);
            if (resultSet.next()) {
                for (Map.Entry<String, String> map : metaData.getColumnMapping().entrySet()) {
                    Class<?> type = entityClass.getDeclaredField(map.getKey()).getType();
                    Method method = entityClass.getDeclaredMethod(mappingHelper.resolveMehodName(map.getKey(), "set"), new Class[]{type});
                    if (type.isAssignableFrom(String.class)) {
                        method.invoke(entity, new Object[]{resultSet.getString(map.getValue())});
                    } else
                        method.invoke(entity, new Object[]{resultSet.getInt(map.getValue())});
                }
            }else
                return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            mappingHelper.safeClose(null, statement, null);
        }
        return entity;
    }

    /**
     * Update Entity
     * @param entity
     */
    public void update(Object entity){
        String sql = mappingHelper.generateUpdateSQL(entity);
        executeQuery(sql);
    }

    public void delete(Object entity){
        String sql = mappingHelper.generateDeleteSQL(entity);
        executeQuery(sql);
    }

    private void executeQuery(String sql){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mappingHelper.safeClose(null, statement, null);
        }
    }

}
