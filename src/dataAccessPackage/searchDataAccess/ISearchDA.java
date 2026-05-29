package dataAccessPackage.searchDataAccess;

import exceptionPackage.search.SearchException;
import modelPackage.searchResult.*;

import java.time.LocalDate;
import java.util.List;

public interface ISearchDA {

    List<AppointmentSearchResult> searchAppointmentsByMemberAndDateRange(
            int memberId,
            LocalDate startDate,
            LocalDate endDate
    ) throws SearchException;

    List<AvailableCoachSearchResult> searchAvailableCoachesBySpecialityAndDateRange(
            String specialityName,
            LocalDate startDate,
            LocalDate endDate
    ) throws SearchException;

    List<PaymentSearchResult> searchPaymentsByMemberAndDateRange(
            int memberId,
            LocalDate startDate,
            LocalDate endDate
    ) throws SearchException;
}