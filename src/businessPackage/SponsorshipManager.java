package businessPackage;

import dataAccessPackage.sponsorshipDataAccess.ISponsorshipDA;
import dataAccessPackage.sponsorshipDataAccess.SponsorshipDBAccess;
import exceptionPackage.sponsorship.DeleteSponsorshipException;
import exceptionPackage.sponsorship.ReadSponsorshipException;
import modelPackage.Sponsorship;

import java.util.List;

public class SponsorshipManager {

    private final ISponsorshipDA sponsorshipDataAccess;

    public SponsorshipManager() {
        this.sponsorshipDataAccess = new SponsorshipDBAccess();
    }

    public SponsorshipManager(ISponsorshipDA sponsorshipDataAccess) {
        this.sponsorshipDataAccess = sponsorshipDataAccess;
    }

    public void createSponsorship(int sponsorId, int sponsoredId) throws AddSponsorshipException {
        if (sponsorId <= 0) {
            throw new AddSponsorshipException(
                    String.valueOf(sponsorId),
                    "L'identifiant du parrain doit etre superieur a 0."
            );
        }

        if (sponsoredId <= 0) {
            throw new AddSponsorshipException(
                    String.valueOf(sponsoredId),
                    "L'identifiant du membre parraine doit etre superieur a 0."
            );
        }

        if (sponsorId == sponsoredId) {
            throw new AddSponsorshipException(
                    String.valueOf(sponsoredId),
                    "Un membre ne peut pas se parrainer lui-meme."
            );
        }

        sponsorshipDataAccess.insert(sponsorId, sponsoredId);
    }

    public List<Sponsorship> getSponsorshipsByMemberId(int memberId) throws ReadSponsorshipException {
        validateMemberIdForRead(memberId);
        return sponsorshipDataAccess.getByMemberId(memberId);
    }

    public int countSponsoredBySponsorId(int sponsorId) throws ReadSponsorshipException {
        validateMemberIdForRead(sponsorId);
        return sponsorshipDataAccess.countSponsoredBySponsorId(sponsorId);
    }

    public boolean existsForSponsoredId(int sponsoredId) throws ReadSponsorshipException {
        validateMemberIdForRead(sponsoredId);
        return sponsorshipDataAccess.existsForSponsoredId(sponsoredId);
    }

    public void deleteSponsorshipsByMemberId(int memberId) throws DeleteSponsorshipException {
        validateMemberIdForDelete(memberId);
        sponsorshipDataAccess.deleteByMemberId(memberId);
    }

    private void validateMemberIdForRead(int memberId) throws ReadSponsorshipException {
        if (memberId <= 0) {
            throw new ReadSponsorshipException(
                    String.valueOf(memberId),
                    "L'identifiant du membre doit être supérieur à 0."
            );
        }
    }

    private void validateMemberIdForDelete(int memberId) throws DeleteSponsorshipException {
        if (memberId <= 0) {
            throw new DeleteSponsorshipException(
                    String.valueOf(memberId),
                    "L'identifiant du membre doit être supérieur à 0."
            );
        }
    }
}
