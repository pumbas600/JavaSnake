package nz.pumbas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private SnakeGame snakeGame;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Snake Game");
        primaryStage.setResizable(false);

        BorderPane root = new BorderPane();
        MenuBar menuBar = new MenuBar();
        menuBar.setMinHeight(SnakeGame.TILE_SIZE);
        menuBar.setBackground(new Background(
                new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Menu options = new Menu();
        Label optionsLabel = new Label("Options");
        optionsLabel.setStyle("-fx-text-fill: white");
        options.setGraphic(optionsLabel);

        MenuItem controls = new MenuItem("Controls");
        //Label controlsLabel = new Label("Controls");
        //controlsLabel.setStyle("-fx-text-fill: white");
        //controls.setGraphic(controlsLabel);
        controls.setOnAction(event -> {
            System.out.println("change controls");
        });
        options.getItems().add(controls);
        menuBar.getMenus().add(options);

        root.setTop(menuBar);

        snakeGame = new SnakeGame(primaryStage, root);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
