package dataAccessPackage.sponsorshipDataAccess;

import exceptionPackage.sponsorship.*;
import modelPackage.Sponsorship;

import java.util.List;

public interface ISponsorshipDA {

    List<Sponsorship> getByMemberId(int memberId) throws ReadSponsorshipException;

    int countSponsoredBySponsorId(int sponsorId) throws ReadSponsorshipException;

    boolean existsForSponsoredId(int sponsoredId) throws ReadSponsorshipException;

    void deleteByMemberId(int memberId) throws DeleteSponsorshipException;
}