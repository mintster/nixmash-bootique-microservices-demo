package com.nixmash.jangles.db;

import com.nixmash.jangles.model.JanglesConnection;

import java.sql.*;

/**
 * Created by daveburke on 6/20/17.
 */
public abstract class JanglesSql {

    // region Constants and Properties

    private Connection connection;
    private Statement statement;
    private CallableStatement callablestatement;
    private final IConnection iConnection;

    public JanglesSql(IConnection iConnection) {
        this.iConnection = iConnection;
    }

    // endregion


    // region MySQL Connection and Query Processes

    private Connection sqlConnection() throws ClassNotFoundException, SQLException {
        JanglesConnection janglesConnection = new JanglesConnection();
        JanglesConnection cn = iConnection.get();
        return DriverManager.getConnection(cn.getUrl(), cn.getUsername(), cn.getPassword());
    }

    protected ResultSet sqlQuery(String query) {
        ResultSet rs = null;
        try {

            this.connection = sqlConnection();
            this.statement = this.connection.createStatement();
            rs = this.statement.executeQuery(query);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


    protected CallableStatement sqlCall(String statement) {
        try {
            this.connection = sqlConnection();
            this.callablestatement = this.connection.prepareCall(statement);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return callablestatement;
    }

    protected void sqlClose() throws SQLException {
        if (!this.statement.isClosed())
            this.statement.close();
        if (!this.connection.isClosed())
            this.connection.close();

    }

    protected void sqlCallClose() throws SQLException {
        if (!this.callablestatement.isClosed())
            this.callablestatement.close();
        if (!this.connection.isClosed())
            this.connection.close();

    }

    // endregion



}
