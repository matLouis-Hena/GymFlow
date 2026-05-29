package testPackage;

import businessPackage.SearchManager;
import modelPackage.searchResult.AvailableCoachSearchResult;

import java.time.LocalDate;
import java.util.List;

public class TestAvailableCoachSearchDA {

    public static void main(String[] args) {
        SearchManager searchManager = new SearchManager();

        try {
            List<AvailableCoachSearchResult> results =
                    searchManager.searchAvailableCoachesBySpecialityAndDateRange(
                            "Musculation",
                            LocalDate.of(2024, 5, 30),
                            LocalDate.of(2026, 6, 1)
                    );
            System.out.println("Créneaux disponibles trouvés : " + results.size());

            for (AvailableCoachSearchResult result : results) {
                System.out.println(
                        result.getAvailabilityId()
                                + " - coach : "
                                + result.getCoachFirstName()
                                + " "
                                + result.getCoachLastName()
                                + " - diplômé : "
                                + result.getHasDegree()
                                + " - "
                                + result.getAvailableDate()
                                + " "
                                + result.getStartTime()
                                + " à "
                                + result.getEndTime()
                );
            }

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test AvailableCoachSearchDA : " + exception.getMessage());
        }
    }
}