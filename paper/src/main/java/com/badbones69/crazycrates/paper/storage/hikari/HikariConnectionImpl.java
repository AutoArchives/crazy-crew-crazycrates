package com.badbones69.crazycrates.paper.storage.hikari;

import com.badbones69.crazycrates.paper.storage.ConnectionImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class HikariConnectionImpl implements ConnectionImpl {

    private HikariDataSource hikari;

    protected abstract String port();

    protected abstract void configure(HikariConfig config, String address, String port, String databaseName, String username, String password);

    @Override
    public void init() {
        HikariConfig config = new HikariConfig();

        config.setPoolName("crazycrates-hikari");
    }

    @Override
    public void shutdown() {
        if (this.hikari != null) {
            this.hikari.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.hikari == null) throw new SQLException("Unable to get a connection, Hikari seems to be null.");

        Connection connection = this.hikari.getConnection();
        if (connection == null) throw new SQLException("Unable to get a connection, It seems to have returned null.");

        return connection;
    }
}