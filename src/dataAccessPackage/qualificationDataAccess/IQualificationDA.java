package dataAccessPackage.qualificationDataAccess;

import exceptionPackage.qualification.*;
import modelPackage.Speciality;

import java.util.List;

public interface IQualificationDA {

    List<Speciality> getSpecialitiesByCoachId(int coachId) throws ReadQualificationException;

    boolean isCoachQualifiedForSpeciality(int coachId, String specialityName)
            throws ReadQualificationException;
}