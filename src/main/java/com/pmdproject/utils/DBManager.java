package com.pmdproject.utils;

import java.sql.*;

public class DBManager {
    private Connection connection;
    private static DBManager instance;

    private DBManager() {
        open();
    }
    public static synchronized DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();

        return instance;
    }
    public void open() {
        if (this.connection == null || isClosed()) {
            try {
                this.connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/pmd_project?user=default_user");
                this.connection.setAutoCommit(Boolean.FALSE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void close() {
        try {
            if (this.connection != null)
                this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isClosed() {
        try {
            return this.connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String query) {
        return executeQuery(query, null);
    }

    public ResultSet executeQuery(PreparedStatement ps) {
        try {
            ResultSet rs = ps.executeQuery();
            this.connection.commit();

            ps.close();
            return rs;
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String query, String[] params) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);

            if (params != null)
                setParameters(ps, params);

            ResultSet rs = ps.executeQuery();
            this.connection.commit();

            ps.close();
            return rs;
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public int executeUpdate(String query) {
        return executeUpdate(query, null);
    }

    public int executeUpdate(PreparedStatement ps) {
        try {
            int rows = ps.executeUpdate();
            this.connection.commit();

            ps.close();
            return rows;
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public int executeUpdate(String query, String[] params) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);

            if (params != null)
                setParameters(ps, params);

            int rows = ps.executeUpdate();
            this.connection.commit();

            ps.close();
            return rows;
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    /*
    * Genera automaticamente la query. Funziona solo se tutti i parametri sono stringhe.
    * */
    private void setParameters(PreparedStatement ps, String[] params) throws SQLException {
        int i = 1;
        for (String s : params) {
            ps.setString(i, s);
            i++;
        }
    }
}
