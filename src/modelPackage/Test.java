package modelPackage;

import exceptionPackage.*;
import java.time.LocalDate;

public class Test {

    public static void main(String[] args) {

        //
        //  TEST 2 — Person avec email invalide
        //
        System.out.println("\n=== TEST 2 : Person avec email invalide ===");
        try {
            Person bob = new Person(
                    2,
                    "Bob",
                    "Martin",
                    LocalDate.of(1990, 7, 20),
                    Gender.Male,
                    "emailsansarobase",   //  email invalide
                    "+32498000000",
                    5,
                    "bob90",
                    "password123"
            );
            System.out.println("Person créée : " + bob.getFirstName());
        } catch (InvalidFirstNameException e) {
            System.out.println("Erreur prénom : " + e.getMessage());
        } catch (InvalidLastNameException e) {
            System.out.println("Erreur nom : " + e.getMessage());
        } catch (InvalidGenderException e) {
            System.out.println("Erreur genre : " + e.getMessage());
        } catch (InvalidEmailException e) {
            System.out.println("Erreur email : " + e.getMessage()); // ← doit s'afficher
        } catch (InvalidPhoneException e) {
            System.out.println("Erreur téléphone : " + e.getMessage());
        } catch (InvalidLockerNumberException e) {
            System.out.println("Erreur casier : " + e.getMessage());
        } catch (InvalidUsernameException e) {
            System.out.println("Erreur username : " + e.getMessage());
        } catch (InvalidPasswordException e) {
            System.out.println("Erreur mot de passe : " + e.getMessage());
        }


    }
}