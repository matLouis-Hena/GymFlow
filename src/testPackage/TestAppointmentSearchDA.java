package testPackage;

import businessPackage.SearchManager;
import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import modelPackage.GymMember;
import modelPackage.searchResult.AppointmentSearchResult;

import java.time.LocalDate;
import java.util.List;

public class TestAppointmentSearchDA {

    public static void main(String[] args) {
        SearchManager searchManager = new SearchManager();
        IGymMemberDA gymMemberDataAccess = new GymMemberDBAccess();

        try {
            List<GymMember> members = gymMemberDataAccess.getAll();

            if (members.isEmpty()) {
                System.out.println("Aucun membre disponible pour tester la recherche de rendez-vous.");
                return;
            }

            GymMember member = members.get(0);

            List<AppointmentSearchResult> results =
                    searchManager.searchAppointmentsByMemberAndDateRange(
                            member.getId(),
                            LocalDate.now().minusMonths(1),
                            LocalDate.now().plusMonths(3)
                    );

            System.out.println(
                    "Rendez-vous trouvés pour "
                            + member.getFirstName()
                            + " "
                            + member.getLastName()
                            + " : "
                            + results.size()
            );

            for (AppointmentSearchResult result : results) {
                System.out.println(
                        result.getAppointmentId()
                                + " - coach : "
                                + result.getCoachFirstName()
                                + " "
                                + result.getCoachLastName()
                                + " - "
                                + result.getAvailableDate()
                                + " "
                                + result.getStartTime()
                                + " à "
                                + result.getEndTime()
                                + " - objectif : "
                                + result.getObjective()
                                + " - statut : "
                                + result.getStatus()
                                + " - salle : "
                                + result.getRoomName()
                );
            }

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test AppointmentSearchDA : " + exception.getMessage());
        }
    }
}