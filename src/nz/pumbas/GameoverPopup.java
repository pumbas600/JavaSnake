package nz.pumbas;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import nz.pumbas.HighScore.HighScore;
import nz.pumbas.HighScore.HighScoreManager;


public class GameoverPopup
{

    private Popup popup = new Popup();
    private GridPane popupGrid = new GridPane();
    private HighScore[] highScores;
    private Label[] highScoreLabels;

    public GameoverPopup(HighScoreManager highScoreManager) {
        highScores = highScoreManager.getHighScores();
        highScoreLabels = new Label[highScores.length];

        popupGrid.setPadding(new Insets(5));
        popupGrid.setBorder(new Border(new BorderStroke(
                Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        popupGrid.setStyle("-fx-background-color: white");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, SnakeGame.TILE_SIZE));
        gameOverLabel.setAlignment(Pos.CENTER);
        popupGrid.add(gameOverLabel, 1, 0);
    }
}
