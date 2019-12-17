package nz.pumbas.HighScorePackage;
import nz.pumbas.FileManagerPackage.FileManager;

import java.util.Objects;
import java.util.stream.Stream;

public class HighScoreFileInterpreter
{
    private static final String FILENAME = "highscores.csv";
    private static final String DELIMITER = ",";

    public static HighScore[] readHighScores() {
        String[] information = FileManager.read(FILENAME);
        HighScore[] highScores = new HighScore[HighScoreManager.HIGHSCORE_COUNT];

        if (information.length == 0 ) return highScores;
        for (int i = 0; i < information.length; i++) {
            String[] highScoreDetails = information[i].split(DELIMITER);
            highScores[i] = new HighScore(
                    highScoreDetails[0], Integer.parseInt(highScoreDetails[1]));
        }
        return highScores;
    }

    public static void writeHighScores(HighScore[] highScores) {
        String[] information = Stream.of(highScores)
                .filter(Objects::nonNull)
                .map(HighScore::toString)
                .toArray(String[]::new);
        FileManager.write(FILENAME, information);

    }
}
