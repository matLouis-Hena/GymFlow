package dataAccessPackage.sponsorshipDataAccess;

import exceptionPackage.sponsorship.AddSponsorshipException;
import exceptionPackage.sponsorship.DeleteSponsorshipException;
import exceptionPackage.sponsorship.ReadSponsorshipException;
import modelPackage.Sponsorship;

import java.util.List;

public interface ISponsorshipDA {

    void insert(int sponsorId, int sponsoredId) throws AddSponsorshipException;

    List<Sponsorship> getByMemberId(int memberId) throws ReadSponsorshipException;

    int countSponsoredBySponsorId(int sponsorId) throws ReadSponsorshipException;

    boolean existsForSponsoredId(int sponsoredId) throws ReadSponsorshipException;

    void deleteByMemberId(int memberId) throws DeleteSponsorshipException;
}
