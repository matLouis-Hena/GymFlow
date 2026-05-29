package viewPackage;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class InfoBannerThread extends Thread {

    private final Label bannerLabel;
    private final String bannerText;
    private int position;

    public InfoBannerThread(Label bannerLabel) {
        this.bannerLabel = bannerLabel;
        this.bannerText =
                "Bienvenue chez BasicFrite   |   "
                        + "Prenez rendez-vous avec un coach    |    "
                        + "Pensez a renouveler votre abonnement    |    "
                        + "Fermeture exceptionnelle le 20/04    |    ";
        this.position = 0;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            String textToShow = getTextToShow();

            Platform.runLater(() -> bannerLabel.setText(textToShow));

            position++;

            if (position >= bannerText.length()) {
                position = 0;
            }

            try {
                Thread.sleep(180);
            } catch (InterruptedException exception) {
                return;
            }
        }
    }

    private String getTextToShow() {
        String repeatedText = bannerText + bannerText;
        int endPosition = position + 180;

        if (endPosition > repeatedText.length()) {
            endPosition = repeatedText.length();
        }

        return repeatedText.substring(position, endPosition);
    }
}
