package dataAccessPackage;

import exceptionPackage.gymMember.AddGymMemberException;
import exceptionPackage.gymMember.DeleteGymMemberException;
import exceptionPackage.gymMember.ReadGymMemberException;
import exceptionPackage.gymMember.UpdateGymMemberException;
import modelPackage.GymMember;

import java.util.List;

public interface GymMemberDAO {

    void insert(GymMember member) throws AddGymMemberException;

    GymMember getById(int id) throws ReadGymMemberException;

    List<GymMember> getAll() throws ReadGymMemberException;

    void update(GymMember member) throws UpdateGymMemberException;

    void delete(int id) throws DeleteGymMemberException;
}