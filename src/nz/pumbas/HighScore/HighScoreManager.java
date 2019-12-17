package nz.pumbas.HighScore;

import javafx.scene.control.Label;
import nz.pumbas.HighScore.HighScore;
import nz.pumbas.HighScore.HighScoreFileInterpreter;

public class HighScoreManager
{
    public static final int HIGHSCORE_COUNT = 5;
    private static final String HIGHSCORE_PREFIX = "HIGHSCORE by ";

    private HighScore[] highScores = new HighScore[HIGHSCORE_COUNT];
    private Label highScoresLabel;

    public HighScoreManager(Label highScoresLabel) {
        this.highScoresLabel = highScoresLabel;
        getHighScoresFromFile();
        updateHighScoreLabel();
    }

    public void updateHighScoreLabel() {
        HighScore highScore = highScores[0];
        highScoresLabel.setText((highScore == null)
                ? highScoresLabel.getText() :
                HIGHSCORE_PREFIX + highScore.name + " : " + highScore.score);
    }

    public boolean isNewHighScore(int score) {
        return highScores[highScores.length - 1] == null ||
                score > highScores[highScores.length - 1].score;
    }

    public void addNewHighScore(String name, int score) {

        //Highscores isn't full yet
        if (highScores[highScores.length -1] == null)
        {
            for (int i = 0; i < highScores.length; i++)
            {
                if (highScores[i] == null) {
                    highScores[i] = new HighScore(name, score);
                    break;
                }
            }
        }

        //Checks if its a valid new highscore (greater than the lowest score)
        else if (score > (int) highScores[highScores.length - 1].score) {
            highScores[highScores.length - 1] = new HighScore(name, score);
        }

        else {
            return;
        }
        sortScores();
        updateHighScoresFile();
        updateHighScoreLabel();
    }

    public HighScore[] getHighScores() {
        return highScores;
    }

    public void getHighScoresFromFile() {
        highScores = HighScoreFileInterpreter.readHighScores();
    }

    public void updateHighScoresFile() {
        HighScoreFileInterpreter.writeHighScores(highScores);
    }

    private void sortScores() {
        for (int i = highScores.length - 1; i > 0; i--) {
            if(highScores[i] == null) continue;
            //If its greater than the next highscore, swap it
            if (highScores[i].score > highScores[i-1].score) {
                HighScore replaceHighScore = highScores[i-1];
                highScores[i-1] = highScores[i];
                highScores[i] = replaceHighScore;
            }

            else {
                //It has finished being sorted
                break;
            }
        }
    }
}

