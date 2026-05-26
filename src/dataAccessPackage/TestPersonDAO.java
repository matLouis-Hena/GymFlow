package dataAccessPackage;

import exceptionPackage.*;
import modelPackage.Person;

import java.time.LocalDate;
import java.util.List;

public class TestPersonDAO {

    public static void main(String[] args) {

        try {

            Person person1 = new Person(
                    0,
                    "Mat",
                    "Test",
                    LocalDate.of(2000,1,1),
                    'm',
                    "mat@test.com",
                    null,
                    null,
                    "mattest",
                    "password123"
            );

            Person person2 = new Person(
                    0,
                    "Ethan",
                    "Test",
                    LocalDate.of(1998,5,10),
                    'm',
                    "ethan@test.com",
                    "+32499999999",
                    12,
                    "ethantest",
                    "password456"
            );

            PersonDAO dao = new PersonDAOImpl();

            int generatedId1 = dao.insert(person1);
            int generatedId2 = dao.insert(person2);

            System.out.println("ID généré : " + generatedId1);
            System.out.println("ID généré : " + generatedId2);

            Person retrieved = dao.getById(generatedId1);

            if(retrieved != null) {
                System.out.println(retrieved.getFirstName());
                System.out.println(retrieved.getEmail());
            }

            retrieved.setFirstName("Mathis");

            dao.update(retrieved);

            Person updated = dao.getById(generatedId1);

            System.out.println("Après update : " + updated.getFirstName());

            dao.delete(generatedId2);

            System.out.println("Person supprimée");

            List<Person> persons = dao.getAll();

            for(Person p : persons) {
                System.out.println(p.getFirstName() + " " + p.getLastName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}