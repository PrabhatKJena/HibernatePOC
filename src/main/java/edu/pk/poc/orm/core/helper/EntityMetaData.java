package edu.pk.poc.orm.core.helper;

import java.util.HashMap;
import java.util.Map;

public class EntityMetaData {
    String tableName;
    String idColumnName;
    String idFieldName;
    Map<String, String> columnMapping = new HashMap<>(); //<entityFieldName, DB_COLUMN_NAME>

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(Map<String, String> columnMapping) {
        this.columnMapping = columnMapping;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    @Override
    public String toString() {
        return "EntityMetaData{" +
                "tableName='" + tableName + '\'' +
                ", columnMapping=" + columnMapping +
                '}';
    }
}
