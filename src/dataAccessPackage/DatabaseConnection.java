package dataAccessPackage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection instance;

    private DatabaseConnection() {}

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                Properties props = new Properties();
                InputStream input = DatabaseConnection.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties");
                props.load(input);

                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                instance = DriverManager.getConnection(url, user, password);

            } catch (IOException e) {
                throw new SQLException("Impossible de charger db.properties");
            }
        }
        return instance;
    }
}