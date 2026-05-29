package controllerPackage;

import businessPackage.GymMemberManager;
import exceptionPackage.gymMember.AddGymMemberException;
import exceptionPackage.gymMember.DeleteGymMemberException;
import exceptionPackage.gymMember.DuplicateGymMemberException;
import exceptionPackage.gymMember.ReadGymMemberException;
import exceptionPackage.gymMember.UpdateGymMemberException;
import modelPackage.GymMember;
import viewPackage.MainView;

import java.util.List;

public class GymMemberController {

    private final GymMemberManager gymMemberManager;
    private final MainView mainView;

    public GymMemberController(GymMemberManager gymMemberManager, MainView mainView) {
        this.gymMemberManager = gymMemberManager;
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

    public void addMember(GymMember member) {
        try {
            gymMemberManager.registerMember(member);
            mainView.showInformationMessage("Membre ajoute avec succes.");
            showMembers();
        } catch (AddGymMemberException | DuplicateGymMemberException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void addExistingAccountMember(GymMember member) {
        try {
            gymMemberManager.registerExistingPersonAsMember(member);
            mainView.showInformationMessage("Compte inscrit comme membre avec succes.");
            showMembers();
        } catch (AddGymMemberException | DuplicateGymMemberException exception) {
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

    public void deleteSelectedMember(GymMember member) {
        if (member == null) {
            mainView.showErrorMessage("Veuillez selectionner un membre a supprimer.");
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
            mainView.showInformationMessage("Membre supprime avec succes.");
            showMembers();
        } catch (DeleteGymMemberException exception) {
            mainView.showErrorMessage(exception.getMessage());
        } catch (Exception exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }
}
