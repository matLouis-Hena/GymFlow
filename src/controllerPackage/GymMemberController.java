package controllerPackage;

import businessPackage.GymMemberManager;
import exceptionPackage.gymMember.ReadGymMemberException;
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

    public void showMembers() {
        try {
            List<GymMember> members = gymMemberManager.getAllMembers();
            mainView.showGymMemberList(members);
        } catch (ReadGymMemberException exception) {
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
        } catch (Exception exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }
}