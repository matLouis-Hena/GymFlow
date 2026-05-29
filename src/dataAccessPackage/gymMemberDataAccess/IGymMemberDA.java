package dataAccessPackage.gymMemberDataAccess;

import exceptionPackage.gymMember.AddGymMemberException;
import exceptionPackage.gymMember.DeleteGymMemberException;
import exceptionPackage.gymMember.ReadGymMemberException;
import exceptionPackage.gymMember.UpdateGymMemberException;
import exceptionPackage.gymMember.DuplicateGymMemberException;
import modelPackage.GymMember;

import java.util.List;

public interface IGymMemberDA {

    void insert(GymMember member) throws AddGymMemberException, DuplicateGymMemberException;

    GymMember getById(int id) throws ReadGymMemberException;

    List<GymMember> getAll() throws ReadGymMemberException;

    void update(GymMember member) throws UpdateGymMemberException, DuplicateGymMemberException;

    void delete(int id) throws DeleteGymMemberException;
}