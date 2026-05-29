package businessPackage;

import dataAccessPackage.gymMemberDataAccess.*;
import exceptionPackage.gymMember.*;
import exceptionPackage.appointment.*;
import exceptionPackage.coachAvailability.*;
import exceptionPackage.payment.*;
import exceptionPackage.sponsorship.*;
import modelPackage.GymMember;

import java.time.LocalDate;
import java.util.List;

public class GymMemberManager {

    private final IGymMemberDA gymMemberDataAccess;
    private final AppointmentManager appointmentManager;
    private final PaymentManager paymentManager;
    private final SponsorshipManager sponsorshipManager;

    public GymMemberManager() {
        this.gymMemberDataAccess = new GymMemberDBAccess();
        this.appointmentManager = new AppointmentManager();
        this.paymentManager = new PaymentManager();
        this.sponsorshipManager = new SponsorshipManager();
    }

    public GymMemberManager(IGymMemberDA gymMemberDataAccess) {
        this.gymMemberDataAccess = gymMemberDataAccess;
        this.appointmentManager = new AppointmentManager();
        this.paymentManager = new PaymentManager();
        this.sponsorshipManager = new SponsorshipManager();
    }

    public void registerMember(GymMember member)
            throws AddGymMemberException, DuplicateGymMemberException {
        validateMemberForAdd(member);
        gymMemberDataAccess.insert(member);
    }

    public void registerExistingPersonAsMember(GymMember member)
            throws AddGymMemberException, DuplicateGymMemberException {
        validateMemberForAdd(member);

        if (member.getId() <= 0) {
            throw new AddGymMemberException(
                    String.valueOf(member.getId()),
                    "L'identifiant du compte est invalide."
            );
        }

        gymMemberDataAccess.insertForExistingPerson(member);
    }

    public List<GymMember> getAllMembers() throws ReadGymMemberException {
        return gymMemberDataAccess.getAll();
    }

    public GymMember getMemberById(int id) throws ReadGymMemberException {
        validateIdForRead(id);

        GymMember member = gymMemberDataAccess.getById(id);

        if (member == null) {
            throw new ReadGymMemberException(
                    String.valueOf(id),
                    "Aucun membre trouvé avec cet identifiant."
            );
        }

        return member;
    }

    public void updateMember(GymMember member)
            throws UpdateGymMemberException, DuplicateGymMemberException {
        validateMemberForUpdate(member);
        gymMemberDataAccess.update(member);
    }

    public void deleteMember(int id) throws DeleteGymMemberException {
        validateIdForDelete(id);
        gymMemberDataAccess.delete(id);
    }

    public void deleteMemberWithDependencies(int id)
            throws DeleteGymMemberException,
            ReadAppointmentException,
            DeleteAppointmentException,
            UpdateCoachAvailabilityException,
            AppointmentBusinessException,
            DeletePaymentException,
            DeleteSponsorshipException {
        validateIdForDelete(id);

        appointmentManager.deleteAppointmentsByMemberId(id);
        paymentManager.deletePaymentsByMemberId(id);
        sponsorshipManager.deleteSponsorshipsByMemberId(id);

        gymMemberDataAccess.delete(id);
    }

    private void validateMemberForAdd(GymMember member) throws AddGymMemberException {
        if (member == null) {
            throw new AddGymMemberException(
                    "member",
                    "Le membre ne peut pas être vide."
            );
        }

        validateRequiredMemberFieldsForAdd(member);
        validatePhysicalValuesForAdd(member);
        validateBirthDateForAdd(member);
        validateEnrollmentForAdd(member);
    }

    private void validateMemberForUpdate(GymMember member) throws UpdateGymMemberException {
        if (member == null) {
            throw new UpdateGymMemberException(
                    "member",
                    "Le membre ne peut pas être vide."
            );
        }

        if (member.getId() <= 0) {
            throw new UpdateGymMemberException(
                    String.valueOf(member.getId()),
                    "L'identifiant du membre est invalide."
            );
        }

        validateRequiredMemberFieldsForUpdate(member);
        validatePhysicalValuesForUpdate(member);
        validateBirthDateForUpdate(member);
        validateEnrollmentForUpdate(member);
    }

    private void validateRequiredMemberFieldsForAdd(GymMember member) throws AddGymMemberException {
        if (member.getFirstName() == null || member.getFirstName().isBlank()) {
            throw new AddGymMemberException("firstName", "Le prénom du membre est obligatoire.");
        }

        if (member.getLastName() == null || member.getLastName().isBlank()) {
            throw new AddGymMemberException("lastName", "Le nom du membre est obligatoire.");
        }

        if (member.getBirthDate() == null) {
            throw new AddGymMemberException("birthDate", "La date de naissance du membre est obligatoire.");
        }

        if (member.getGender() == null) {
            throw new AddGymMemberException("gender", "Le genre du membre est obligatoire.");
        }

        if (member.getEmail() == null || member.getEmail().isBlank()) {
            throw new AddGymMemberException("email", "L'adresse email du membre est obligatoire.");
        }

        if (member.getUsername() == null || member.getUsername().isBlank()) {
            throw new AddGymMemberException("username", "Le nom d'utilisateur du membre est obligatoire.");
        }

        if (member.getPassword() == null || member.getPassword().isBlank()) {
            throw new AddGymMemberException("password", "Le mot de passe du membre est obligatoire.");
        }
    }

    private void validateRequiredMemberFieldsForUpdate(GymMember member)
            throws UpdateGymMemberException {
        if (member.getFirstName() == null || member.getFirstName().isBlank()) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "Le prénom du membre est obligatoire.");
        }

        if (member.getLastName() == null || member.getLastName().isBlank()) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "Le nom du membre est obligatoire.");
        }

        if (member.getBirthDate() == null) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "La date de naissance du membre est obligatoire.");
        }

        if (member.getGender() == null) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "Le genre du membre est obligatoire.");
        }

        if (member.getEmail() == null || member.getEmail().isBlank()) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "L'adresse email du membre est obligatoire.");
        }

        if (member.getUsername() == null || member.getUsername().isBlank()) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "Le nom d'utilisateur du membre est obligatoire.");
        }

        if (member.getPassword() == null || member.getPassword().isBlank()) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "Le mot de passe du membre est obligatoire.");
        }
    }

    private void validatePhysicalValuesForAdd(GymMember member) throws AddGymMemberException {
        if (member.getWeight() <= 0) {
            throw new AddGymMemberException("weight", "Le poids du membre doit être supérieur à 0.");
        }

        if (member.getHeight() <= 0) {
            throw new AddGymMemberException("height", "La taille du membre doit être supérieure à 0.");
        }
    }

    private void validatePhysicalValuesForUpdate(GymMember member)
            throws UpdateGymMemberException {
        if (member.getWeight() <= 0) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "Le poids du membre doit être supérieur à 0.");
        }

        if (member.getHeight() <= 0) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "La taille du membre doit être supérieure à 0.");
        }
    }

    private void validateBirthDateForAdd(GymMember member) throws AddGymMemberException {
        if (member.getBirthDate().isAfter(LocalDate.now())) {
            throw new AddGymMemberException("birthDate", "La date de naissance ne peut pas être dans le futur.");
        }
    }

    private void validateBirthDateForUpdate(GymMember member)
            throws UpdateGymMemberException {
        if (member.getBirthDate().isAfter(LocalDate.now())) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "La date de naissance ne peut pas être dans le futur.");
        }
    }

    private void validateEnrollmentForAdd(GymMember member) throws AddGymMemberException {
        if (member.getEnrollment() == null) {
            throw new AddGymMemberException("enrollment", "L'abonnement du membre est obligatoire.");
        }

        if (member.getEnrollment().getDurationMonths() <= 0) {
            throw new AddGymMemberException("durationMonths", "La durée de l'abonnement doit être supérieure à 0.");
        }
    }

    private void validateEnrollmentForUpdate(GymMember member)
            throws UpdateGymMemberException {
        if (member.getEnrollment() == null) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "L'abonnement du membre est obligatoire.");
        }

        if (member.getEnrollment().getId() <= 0) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "L'abonnement sélectionné est invalide.");
        }

        if (member.getEnrollment().getDurationMonths() <= 0) {
            throw new UpdateGymMemberException(String.valueOf(member.getId()), "La durée de l'abonnement doit être supérieure à 0.");
        }
    }

    private void validateIdForRead(int id) throws ReadGymMemberException {
        if (id <= 0) {
            throw new ReadGymMemberException(String.valueOf(id), "L'identifiant doit être supérieur à 0.");
        }
    }

    private void validateIdForDelete(int id) throws DeleteGymMemberException {
        if (id <= 0) {
            throw new DeleteGymMemberException(String.valueOf(id), "L'identifiant doit être supérieur à 0.");
        }
    }
}
