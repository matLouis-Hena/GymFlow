package dataAccessPackage.searchDataAccess;

import exceptionPackage.search.SearchException;
import modelPackage.searchResult.AppointmentSearchResult;
import modelPackage.searchResult.AvailableCoachSearchResult;
import modelPackage.searchResult.SponsoredMemberSearchResult;

import java.time.LocalDate;
import java.util.List;

public interface ISearchDA {

    List<AppointmentSearchResult> searchAppointmentsByMemberAndDateRange(
            int memberId,
            LocalDate startDate,
            LocalDate endDate
    ) throws SearchException;

    List<SponsoredMemberSearchResult> searchSponsoredMembersBySponsorId(
            int sponsorId
    ) throws SearchException;

    List<AvailableCoachSearchResult> searchAvailableCoachesBySpecialityAndDateRange(
            String specialityName,
            LocalDate startDate,
            LocalDate endDate
    ) throws SearchException;
}