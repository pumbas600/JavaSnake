package nz.pumbas;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import nz.pumbas.HighScorePackage.HighScore;
import nz.pumbas.HighScorePackage.HighScoreManager;
import nz.pumbas.UtilityClasses.PopupBuilder;


public class GameoverPopup
{
    HighScoreManager highScoreManager;

    private Popup popup;
    private GridPane popupGrid = new GridPane();
    private HighScore[] highScores;
    private Label[] highScoreLabels;

    public GameoverPopup(HighScoreManager highScoreManager, int score) {
        this.highScoreManager = highScoreManager;
        highScores = highScoreManager.getHighScores();
        highScoreLabels = new Label[highScores.length];

        Label highScoreName = new Label("Name: ");
        TextField nameInput = new TextField();
        Button submit = new Button("Submit");

        popup = new PopupBuilder<HBox>(new HBox())
                .setPadding(5)
                .setBorder(new Border(new BorderStroke(
                        Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)))
                .addCloseButton()
                .addNode(new Label("GAME OVER"))
                .setStyle("-fx-font-size: " + SnakeGame.TILE_SIZE +";" +
                        " -fx-font-family: verdana; -fx-font-weight: bold; -fx-alignment: CENTER")
                .startOptionalNodes(() -> {return highScoreManager.isNewHighScore(score);})
                .addNode(highScoreName)
                .addNode(nameInput)
                .addNode(submit)
                .setOnMouseClicked(event -> {
                    if (!nameInput.getText().equals(""))
                    {
                        highScoreManager.addNewHighScore(nameInput.getText(), score);
                        popupGrid.getChildren().removeAll(highScoreName, nameInput, submit);
                        HighScore[] highScores = highScoreManager.getHighScores();
                        for (int i = 0; i < highScores.length; i++)
                        {
                            if (highScores[i] == null) break; //No more after the first null
                            popupGrid.getChildren().remove(highScoreLabels[i]);
                        }
                        popup.getContent().addAll(displayHighScores());
                    }
                })
                .endOptionalNodes()
                .addNodes(displayHighScores())
                .build();


        popupGrid.setPadding(new Insets(5));
        popupGrid.setBorder(new Border(new BorderStroke(
                Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        popupGrid.setStyle("-fx-background-color: white");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, SnakeGame.TILE_SIZE));
        gameOverLabel.setAlignment(Pos.CENTER);
        popupGrid.add(gameOverLabel, 1, 0);
    }

    private Node[] displayHighScores(){
        //TODO: Implement this
        return null;
    }
}
