package businessPackage;

import dataAccessPackage.searchDataAccess.ISearchDA;
import dataAccessPackage.searchDataAccess.SearchDBAccess;
import exceptionPackage.search.SearchException;
import modelPackage.searchResult.AppointmentSearchResult;
import modelPackage.searchResult.AvailableCoachSearchResult;
import modelPackage.searchResult.SponsoredMemberSearchResult;

import java.time.LocalDate;
import java.util.List;

public class SearchManager {

    private final ISearchDA searchDataAccess;

    public SearchManager() {
        this.searchDataAccess = new SearchDBAccess();
    }

    public SearchManager(ISearchDA searchDataAccess) {
        this.searchDataAccess = searchDataAccess;
    }

    public List<AppointmentSearchResult> searchAppointmentsByMemberAndDateRange(
            int memberId,
            LocalDate startDate,
            LocalDate endDate
    ) throws SearchException {
        validateMemberId(memberId);
        validateDateRange(startDate, endDate);

        return searchDataAccess.searchAppointmentsByMemberAndDateRange(
                memberId,
                startDate,
                endDate
        );
    }

    public List<SponsoredMemberSearchResult> searchSponsoredMembersBySponsorId(
            int sponsorId
    ) throws SearchException {
        validateSponsorId(sponsorId);

        return searchDataAccess.searchSponsoredMembersBySponsorId(sponsorId);
    }

    public List<AvailableCoachSearchResult> searchAvailableCoachesBySpecialityAndDateRange(
            String specialityName,
            LocalDate startDate,
            LocalDate endDate
    ) throws SearchException {
        validateSpecialityName(specialityName);
        validateDateRange(startDate, endDate);

        return searchDataAccess.searchAvailableCoachesBySpecialityAndDateRange(
                specialityName,
                startDate,
                endDate
        );
    }

    private void validateMemberId(int memberId) throws SearchException {
        if (memberId <= 0) {
            throw new SearchException(
                    String.valueOf(memberId),
                    "L'identifiant du membre doit être supérieur à 0."
            );
        }
    }

    private void validateSponsorId(int sponsorId) throws SearchException {
        if (sponsorId <= 0) {
            throw new SearchException(
                    String.valueOf(sponsorId),
                    "L'identifiant du parrain doit être supérieur à 0."
            );
        }
    }

    private void validateSpecialityName(String specialityName) throws SearchException {
        if (specialityName == null || specialityName.isBlank()) {
            throw new SearchException(
                    "specialityName",
                    "Le nom de la spécialité est obligatoire."
            );
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) throws SearchException {
        if (startDate == null) {
            throw new SearchException(
                    "startDate",
                    "La date de début est obligatoire."
            );
        }

        if (endDate == null) {
            throw new SearchException(
                    "endDate",
                    "La date de fin est obligatoire."
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new SearchException(
                    "endDate",
                    "La date de fin ne peut pas être avant la date de début."
            );
        }
    }
}