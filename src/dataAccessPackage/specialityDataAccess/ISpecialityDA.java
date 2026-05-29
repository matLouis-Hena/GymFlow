package dataAccessPackage.specialityDataAccess;

import exceptionPackage.speciality.ReadSpecialityException;
import modelPackage.Speciality;

import java.util.List;

public interface ISpecialityDA {

    List<Speciality> getAll() throws ReadSpecialityException;

    Speciality getByName(String name) throws ReadSpecialityException;
}