package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Person;

import java.util.List;

public interface PersonDAO {

    int insert(Person person) throws DataAccessException;

    Person getById(int id) throws DataAccessException;

    List<Person> getAll() throws DataAccessException;

    void update(Person person) throws DataAccessException;

    void delete(int id) throws DataAccessException;
}