package testPackage;

import dataAccessPackage.SingletonConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        try {
            Connection connection = SingletonConnection.getInstance();
            System.out.println("Connexion réussie à la base de données !");

        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }
}