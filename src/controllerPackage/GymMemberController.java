package controllerPackage;

import businessPackage.GymMemberManager;
import businessPackage.SponsorshipManager;
import exceptionPackage.appointment.AppointmentBusinessException;
import exceptionPackage.appointment.DeleteAppointmentException;
import exceptionPackage.appointment.ReadAppointmentException;
import exceptionPackage.coachAvailability.UpdateCoachAvailabilityException;
import exceptionPackage.gymMember.*;
import exceptionPackage.payment.DeletePaymentException;
import exceptionPackage.sponsorship.DeleteSponsorshipException;
import modelPackage.GymMember;
import modelPackage.Sponsorship;
import modelPackage.UserRole;
import viewPackage.MainView;

import java.util.List;

public class GymMemberController {

    private final GymMemberManager gymMemberManager;
    private final SponsorshipManager sponsorshipManager;
    private final MainView mainView;

    public GymMemberController(GymMemberManager gymMemberManager, MainView mainView) {
        this.gymMemberManager = gymMemberManager;
        this.sponsorshipManager = new SponsorshipManager();
        this.mainView = mainView;
    }

    public void showConnectedPersonRegistrationForm() {
        mainView.showConnectedPersonGymMemberForm();
    }

    public void showUpdateMemberForm(GymMember member) {
        if (member == null) {
            mainView.showErrorMessage("Veuillez selectionner un membre a modifier.");
            return;
        }

        mainView.showGymMemberForm(member);
    }

    public void showMembers() {
        try {
            List<GymMember> members = gymMemberManager.getAllMembers();
            mainView.showGymMemberList(members);
        } catch (ReadGymMemberException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void showMyAccount() {
        try {
            int connectedPersonId = mainView.getConnectedPerson().getId();
            GymMember member = gymMemberManager.getMemberById(connectedPersonId);
            String sponsorText = getSponsorText(member.getId());

            mainView.showMemberAccount(member, sponsorText);

        } catch (ReadGymMemberException | ReadSponsorshipException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void addMember(GymMember member, String sponsorUsername) {
        try {
            GymMember sponsor = getSponsorIfNeeded(sponsorUsername);
            gymMemberManager.registerMember(member);

            if (sponsor != null) {
                GymMember sponsored = gymMemberManager.getMemberByUsername(member.getUsername());
                sponsorshipManager.createSponsorship(sponsor.getId(), sponsored.getId());
            }

            mainView.showInformationMessage("Membre ajoute avec succes.");
            showMembers();
        } catch (
                AddGymMemberException |
                DuplicateGymMemberException |
                ReadGymMemberException |
                AddSponsorshipException exception
        ) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void addExistingAccountMember(GymMember member, String sponsorUsername) {
        try {
            GymMember sponsor = getSponsorIfNeeded(sponsorUsername);
            gymMemberManager.registerExistingPersonAsMember(member);

            if (sponsor != null) {
                sponsorshipManager.createSponsorship(sponsor.getId(), member.getId());
            }

            mainView.showInformationMessage("Compte inscrit comme membre avec succes.");
            mainView.setConnectedUserRole(UserRole.MEMBER_WITH_SUBSCRIPTION);
            mainView.showWelcomeMessage();
        } catch (
                AddGymMemberException |
                DuplicateGymMemberException |
                ReadGymMemberException |
                AddSponsorshipException exception
        ) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void updateMember(GymMember member) {
        try {
            gymMemberManager.updateMember(member);
            mainView.showInformationMessage("Membre modifie avec succes.");
            showMembers();
        } catch (UpdateGymMemberException | DuplicateGymMemberException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void updateMyAccount(GymMember member) {
        try {
            gymMemberManager.updateMember(member);
            mainView.showInformationMessage("Compte modifie avec succes.");
            showMyAccount();
        } catch (UpdateGymMemberException | DuplicateGymMemberException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void deleteSelectedMember(GymMember member) {
        if (member == null) {
            mainView.showErrorMessage("Veuillez sélectionner un membre à supprimer.");
            return;
        }

        boolean confirmed = mainView.askConfirmation(
                "Voulez-vous vraiment supprimer le membre "
                        + member.getFirstName()
                        + " "
                        + member.getLastName()
                        + " ?"
        );

        if (!confirmed) {
            return;
        }

        try {
            gymMemberManager.deleteMemberWithDependencies(member.getId());

            mainView.showInformationMessage("Membre supprimé avec succès.");
            showMembers();

        } catch (
                DeleteGymMemberException |
                ReadAppointmentException |
                DeleteAppointmentException |
                UpdateCoachAvailabilityException |
                AppointmentBusinessException |
                DeletePaymentException |
                DeleteSponsorshipException exception
        ) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    private GymMember getSponsorIfNeeded(String sponsorUsername) throws ReadGymMemberException {
        if (sponsorUsername == null || sponsorUsername.trim().isEmpty()) {
            return null;
        }

        return gymMemberManager.getMemberByUsername(sponsorUsername);
    }

    private String getSponsorText(int memberId) throws ReadSponsorshipException {
        List<Sponsorship> sponsorships = sponsorshipManager.getSponsorshipsByMemberId(memberId);

        for (Sponsorship sponsorship : sponsorships) {
            if (sponsorship.getSponsored().getId() == memberId) {
                return "Oui, parraine par "
                        + sponsorship.getSponsor().getUsername();
            }
        }

        return "Non";
    }
}