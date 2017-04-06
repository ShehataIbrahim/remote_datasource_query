package com.ih.utils;

import com.ih.beans.ConnectionData;
import com.ih.contracts.Connector;

import java.sql.Connection;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

public class WebLogic12cConnector implements Connector {
    public WebLogic12cConnector() {
        super();
    }

    public Connection connect(ConnectionData data) {
        Context ctx = null;
        Hashtable<String, String> env = new Hashtable<String, String>();
        try{
        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
                                        env.put(Context.PROVIDER_URL, "t3://" + data.getServerIP() + ":" + data.getServerConsolePort());
                                        env.put(Context.SECURITY_PRINCIPAL, data.getUserName());
                                        env.put(Context.SECURITY_CREDENTIALS, data.getPassword());
                                        ctx = new InitialContext(env);
                                        DataSource ds = (DataSource) ctx.lookup(data.getDataSourceJNDI());
            return ds.getConnection();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
