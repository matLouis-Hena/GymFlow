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
                            LocalDate.now().minusDays(1),
                            LocalDate.now().plusMonths(3)
                    );

            System.out.println("Coachs disponibles trouvés : " + results.size());

            for (AvailableCoachSearchResult result : results) {
                System.out.println(
                        result.getAvailabilityId()
                                + " - "
                                + result.getCoachFullName()
                                + " - "
                                + result.getSpecialityName()
                                + " - "
                                + result.getAvailableDate()
                                + " "
                                + result.getStartTime()
                                + " à "
                                + result.getEndTime()
                                + " - diplômé : "
                                + result.getHasDegree()
                );
            }

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test AvailableCoachSearchDA : " + exception.getMessage());
        }
    }
}