package nz.pumbas.HighScorePackage;

import javafx.scene.control.Label;

public class HighScore {

    public String name;
    public int score;

    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return name + "," + String.valueOf(score);
    }

    public Label getAsLabel(int position) {

        return new Label(position + ". " + name + ": " + score);
    }
}
