package com.ih.beans;

public class ConnectionData {
    private String serverName;
    private String serverClass;
    private String connectionDriver;
    private String serverIP;
    private String serverConsolePort;
    private String dataSourceJNDI;
    private String userName;
    private String password;
    public ConnectionData() {
        super();
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerClass(String serverClass) {
        this.serverClass = serverClass;
    }

    public String getServerClass() {
        return serverClass;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setServerConsolePort(String serverConsolePort) {
        this.serverConsolePort = serverConsolePort;
    }

    public String getServerConsolePort() {
        return serverConsolePort;
    }

    public void setConnectionDriver(String connectionDriver) {
        this.connectionDriver = connectionDriver;
    }

    public String getConnectionDriver() {
        return connectionDriver;
    }

    public void setDataSourceJNDI(String dataSourceJNDI) {
        this.dataSourceJNDI = dataSourceJNDI;
    }

    public String getDataSourceJNDI() {
        return dataSourceJNDI;
    }
    public boolean validate() {
        boolean result=true;
        if(isEmptyString(serverClass))
            result=false;
        if(isEmptyString(serverConsolePort))
            result=false;
        if(isEmptyString(serverIP))
            result=false;
        if(isEmptyString(connectionDriver))
            result=false;
        if(isEmptyString(dataSourceJNDI))
            result=false;
        if(isEmptyString(userName))
            result=false;
        if(isEmptyString(password))
            result=false;
        return result;
    }
    private boolean isEmptyString(String str) {
        return str==null || str.trim().isEmpty();
    }
}
