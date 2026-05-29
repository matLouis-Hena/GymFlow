package dataAccessPackage.gymMemberDataAccess;

import exceptionPackage.gymMember.*;
import modelPackage.GymMember;

import java.util.List;

public interface IGymMemberDA {

    void insert(GymMember member) throws AddGymMemberException, DuplicateGymMemberException;

    void insertForExistingPerson(GymMember member) throws AddGymMemberException, DuplicateGymMemberException;

    List<GymMember> getAll() throws ReadGymMemberException;

    GymMember getById(int id) throws ReadGymMemberException;

    void update(GymMember member) throws UpdateGymMemberException, DuplicateGymMemberException;

    void delete(int id) throws DeleteGymMemberException;
}
