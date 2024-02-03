package com.example.adproject;

import java.sql.*;

public class ConnectionUtils {
    public static Connection getConn() {
        String url ="jdbc:mysql://localhost:3306/adproject?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String username = "root";
        String password = "zhangten0131";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        try {
            DriverManager.setLoginTimeout(2); // 设置登录超时时间为2秒
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static boolean close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
