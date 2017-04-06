package com.ih.connection;

import com.ih.beans.ConnectionData;
import com.ih.contracts.Connector;
import com.ih.contracts.QueryInterface;
import com.ih.ui.ConnectionDialog;
import com.ih.beans.FlaggedResult;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    private static ConnectionData connectionDetails;
    private static Connection currentConnection = null;

    public ConnectionManager() {
        super();
    }

    public static void disconnect() {
        try {
            currentConnection.rollback();
            
        } catch (SQLException e) {
            e.printStackTrace();
            }finally{
                try {
                    currentConnection.close();
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        currentConnection=null;
        System.out.println("Disconnected");
    }

    public static void switchConnectionStatus(QueryInterface queryForm) {
        try {
            if(isConnected()) {
            disconnect();
            queryForm.notifyConnectionClosed();
        }else
        {
        ConnectionDialog dialog =
            new ConnectionDialog(queryForm, "Connect", true);
        dialog.setVisible(true);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static FlaggedResult connect() {
        FlaggedResult result = new FlaggedResult();
        if (connectionDetails == null) {
            result.setMessage("Connection Details should be provided");
            return result;
        }
        if (!connectionDetails.validate()) {
            result.setMessage("Connection Details has missing info");
            return result;
        }
        Class c;
        try {
            c = Class.forName(connectionDetails.getServerClass());
            Connector connector = (Connector)c.newInstance();
            currentConnection = connector.connect(connectionDetails);
            result.setFlag(true);
       //     getSchemaTables("SITES_WCSITES");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            result.setMessage("Server Class not Found: " + e.getMessage());
            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            result.setMessage("IllegalAccessException while Creating connector object: " +
                              e.getMessage());
            return result;
        } catch (InstantiationException e) {
            e.printStackTrace();
            result.setMessage("InstantiationException while Creating connector object: " +
                              e.getMessage());
            return result;
        }
        return result;
    }

    public static List<String> getSchemaTables(String schemaName) throws SQLException {
        ArrayList<String> tables = new ArrayList<String>();
        DatabaseMetaData md = currentConnection.getMetaData();

        String[] types = { "TABLE" };
        ResultSet rs =
            md.getTables(currentConnection.getCatalog(), schemaName, "%",
                         types);
        while (rs.next()) {
            tables.add(rs.getString(3));
        }
        return tables;
    }

    public static Connection getConnection() throws SQLException {
        if (!isConnected())
            connect();
        return currentConnection;
    }

    private static boolean isConnected() throws SQLException {
        return currentConnection != null && !currentConnection.isClosed();
    }

    public static void setConnectionDetails(ConnectionData connectionDetails) {
        ConnectionManager.connectionDetails = connectionDetails;
    }

    public static List<String> getSchemasList() throws SQLException {
        ArrayList<String> schemas = new ArrayList<String>();
        DatabaseMetaData md = currentConnection.getMetaData();
        ResultSet rs = md.getSchemas();
   //     int columnsCount=rs.getMetaData().getColumnCount();
        while (rs.next()) {
         schemas.add(rs.getString(1));
        }
        return schemas;
    }
}
