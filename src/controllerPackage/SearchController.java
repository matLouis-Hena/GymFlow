package controllerPackage;

import businessPackage.GymMemberManager;
import businessPackage.SearchManager;
import businessPackage.SpecialityManager;
import exceptionPackage.gymMember.ReadGymMemberException;
import exceptionPackage.search.SearchException;
import exceptionPackage.speciality.ReadSpecialityException;
import modelPackage.GymMember;
import modelPackage.Speciality;
import modelPackage.searchResult.AppointmentSearchResult;
import modelPackage.searchResult.AvailableCoachSearchResult;
import modelPackage.searchResult.SponsoredMemberSearchResult;
import viewPackage.MainView;

import java.time.LocalDate;
import java.util.List;

public class SearchController {

    private final SearchManager searchManager;
    private final GymMemberManager gymMemberManager;
    private final SpecialityManager specialityManager;
    private final MainView mainView;

    public SearchController(
            SearchManager searchManager,
            GymMemberManager gymMemberManager,
            SpecialityManager specialityManager,
            MainView mainView
    ) {
        this.searchManager = searchManager;
        this.gymMemberManager = gymMemberManager;
        this.specialityManager = specialityManager;
        this.mainView = mainView;
    }

    public void showAppointmentSearch() {
        try {
            List<GymMember> members = gymMemberManager.getAllMembers();
            mainView.showAppointmentSearch(members);
        } catch (ReadGymMemberException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void searchAppointments(int memberId, LocalDate startDate, LocalDate endDate) {
        try {
            List<AppointmentSearchResult> results =
                    searchManager.searchAppointmentsByMemberAndDateRange(memberId, startDate, endDate);
            mainView.showAppointmentSearchResults(results);
        } catch (SearchException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void showSponsoredMemberSearch() {
        try {
            List<GymMember> members = gymMemberManager.getAllMembers();
            mainView.showSponsoredMemberSearch(members);
        } catch (ReadGymMemberException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void searchSponsoredMembers(int sponsorId) {
        try {
            List<SponsoredMemberSearchResult> results =
                    searchManager.searchSponsoredMembersBySponsorId(sponsorId);
            mainView.showSponsoredMemberSearchResults(results);
        } catch (SearchException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void showAvailableCoachSearch() {
        try {
            List<Speciality> specialities = specialityManager.getAllSpecialities();
            mainView.showAvailableCoachSearch(specialities);
        } catch (ReadSpecialityException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void searchAvailableCoaches(String specialityName, LocalDate startDate, LocalDate endDate) {
        try {
            List<AvailableCoachSearchResult> results =
                    searchManager.searchAvailableCoachesBySpecialityAndDateRange(
                            specialityName,
                            startDate,
                            endDate
                    );
            mainView.showAvailableCoachSearchResults(results);
        } catch (SearchException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }
}
