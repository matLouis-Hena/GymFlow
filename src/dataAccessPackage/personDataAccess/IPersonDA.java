package dataAccessPackage.personDataAccess;

import exceptionPackage.person.AddPersonException;
import exceptionPackage.person.DeletePersonException;
import exceptionPackage.person.ReadPersonException;
import exceptionPackage.person.UpdatePersonException;
import modelPackage.Person;

import java.util.List;

public interface IPersonDA {

    int insert(Person person) throws AddPersonException;

    List<Person> getAll() throws ReadPersonException;

    Person getById(int id) throws ReadPersonException;

    void update(Person person) throws UpdatePersonException;

    void delete(int id) throws DeletePersonException;
}