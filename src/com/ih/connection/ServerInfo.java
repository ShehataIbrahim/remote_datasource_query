package com.ih.connection;

    public class ServerInfo{
        String serverClass;
        String serverName;
        String driver;

        public void setServerClass(String serverClass) {
            this.serverClass = serverClass;
        }

        public String getServerClass() {
            return serverClass;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getServerName() {
            return serverName;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getDriver() {
            return driver;
        }

        @Override
        public String toString() {
            
            return serverName;
        }

    }