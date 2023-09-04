package com.badbones69.crazycrates.paper.storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionImpl {

    String getImplName();

    void init();

    void shutdown() throws Exception;

    Connection getConnection() throws SQLException;

}