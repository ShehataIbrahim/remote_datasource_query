package com.ih.contracts;

import com.ih.beans.ConnectionData;

import java.sql.Connection;

public interface Connector {
    public Connection connect(ConnectionData data);
}
