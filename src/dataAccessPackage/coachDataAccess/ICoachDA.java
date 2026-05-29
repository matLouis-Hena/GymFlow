package dataAccessPackage.coachDataAccess;

import exceptionPackage.coach.ReadCoachException;
import modelPackage.Coach;

import java.util.List;

public interface ICoachDA {

    List<Coach> getAll() throws ReadCoachException;

    Coach getById(int id) throws ReadCoachException;

    List<Coach> getBySpecialityName(String specialityName) throws ReadCoachException;
}