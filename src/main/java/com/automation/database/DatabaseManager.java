package com.automation.database;

import com.automation.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Manager to handle database connections and operations
 * Supports MySQL and PostgreSQL databases
 */
public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private ConfigManager config = ConfigManager.getInstance();
    private Connection connection;
    
    private DatabaseManager() {
        // Private constructor for singleton
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Establish database connection based on configuration
     */
    public void connect() {
        try {
            String dbType = config.getDbType().toLowerCase();
            String url = buildConnectionUrl(dbType);
            
            connection = DriverManager.getConnection(
                url,
                config.getDbUsername(),
                config.getDbPassword()
            );
            
            logger.info("Database connection established: {}", dbType);
        } catch (SQLException e) {
            logger.error("Failed to establish database connection", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }
    
    /**
     * Build connection URL based on database type
     */
    private String buildConnectionUrl(String dbType) {
        String host = config.getDbHost();
        int port = config.getDbPort();
        String dbName = config.getDbName();
        
        return switch (dbType) {
            case "mysql" -> String.format(
                "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                host, port, dbName
            );
            case "postgresql" -> String.format(
                "jdbc:postgresql://%s:%d/%s",
                host, port, dbName
            );
            default -> throw new IllegalArgumentException("Unsupported database type: " + dbType);
        };
    }
    
    /**
     * Execute SELECT query and return results as List of Maps
     */
    public List<Map<String, Object>> executeQuery(String query, Object... parameters) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setParameters(stmt, parameters);
            
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
            
            logger.info("Query executed successfully. Returned {} rows", results.size());
        } catch (SQLException e) {
            logger.error("Failed to execute query: {}", query, e);
            throw new RuntimeException("Query execution failed", e);
        }
        
        return results;
    }
    
    /**
     * Execute UPDATE, INSERT, DELETE queries
     */
    public int executeUpdate(String query, Object... parameters) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setParameters(stmt, parameters);
            int affectedRows = stmt.executeUpdate();
            
            logger.info("Update query executed successfully. Affected rows: {}", affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            logger.error("Failed to execute update query: {}", query, e);
            throw new RuntimeException("Update query execution failed", e);
        }
    }
    
    /**
     * Get single value from database
     */
    public Object getSingleValue(String query, Object... parameters) {
        List<Map<String, Object>> results = executeQuery(query, parameters);
        if (results.isEmpty()) {
            return null;
        }
        
        Map<String, Object> firstRow = results.get(0);
        return firstRow.values().iterator().next();
    }
    
    /**
     * Check if record exists
     */
    public boolean recordExists(String tableName, String whereClause, Object... parameters) {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s", tableName, whereClause);
        Object count = getSingleValue(query, parameters);
        return count != null && ((Number) count).intValue() > 0;
    }
    
    /**
     * Set parameters for prepared statement
     */
    private void setParameters(PreparedStatement stmt, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            stmt.setObject(i + 1, parameters[i]);
        }
    }
    
    /**
     * Close database connection
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Failed to close database connection", e);
            }
        }
    }
    
    /**
     * Check if connection is valid
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}
