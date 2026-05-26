package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.GymMember;

import java.util.List;

public interface GymMemberDAO {

    void insert(GymMember member) throws DataAccessException;

    GymMember getById(int id) throws DataAccessException;

    List<GymMember> getAll() throws DataAccessException;

    void update(GymMember member) throws DataAccessException;

    void delete(int id) throws DataAccessException;
}