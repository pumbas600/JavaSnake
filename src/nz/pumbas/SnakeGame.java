package nz.pumbas;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import nz.pumbas.HighScorePackage.HighScore;
import nz.pumbas.HighScorePackage.HighScoreManager;
import nz.pumbas.UtilityClasses.Direction;
import nz.pumbas.UtilityClasses.Tile;
import nz.pumbas.UtilityClasses.Vector;

import java.util.ArrayList;
import java.util.Random;

public class SnakeGame
{
    public static final int TILE_SIZE = 20; //In pixels
    public static final int SNAKE_SPEED = 9; //Movements per second
    public static final int START_SIZE = 5; //Number of tiles large the snake should be
    public static final int TILE_WIDTH = 35; //The width of the screen in tiles
    public static final int TILE_HEIGHT = 20; //The height of the screen in tiles
    public static final int LABEL_HEIGHT = 3;

    private static final int FRAME_RATE = 60; //Framerate of the game - DO NOT CHANGE

    private ArrayList<Tile> snake = new ArrayList<>();
    private Direction facing = Direction.DOWN;
    private Vector pos = new Vector(TILE_WIDTH / 2, TILE_HEIGHT / 2);
    private Vector foodPos;
    private Tile foodTile;
    private int score = 0;
    //This is to prevent quick, on the spot 180's that cause you to go kill yourself
    private boolean changedDirection = false;
    private boolean gameOver = false;

    private int frame = 0;
    private GridPane grid;
    private Label scoreLabel;
    private Popup popup;
    private HighScoreManager highScoreManager;
    private Label highScoreLabel;
    private Scene scene;
    private Stage primaryStage;
    private AnimationTimer mainloop;

    public SnakeGame(Stage primaryStage, BorderPane root) {

        this.primaryStage = primaryStage;

        double width = TILE_WIDTH * TILE_SIZE;
        double height = TILE_HEIGHT * TILE_SIZE + (LABEL_HEIGHT + 1) * TILE_SIZE + 5;

        AnchorPane topBar = new AnchorPane();

        Button restartButton = new Button("Restart");

        restartButton.setMinSize(0.5 * LABEL_HEIGHT * TILE_SIZE, 0.5 * LABEL_HEIGHT * TILE_SIZE);
        restartButton.setOnAction(event -> {
            if (popup != null && popup.isShowing()) popup.hide();
            setup(true);
        });

        this.scoreLabel = new Label("SCORE: " + score);
        scoreLabel.setMinSize(TILE_WIDTH * TILE_SIZE, LABEL_HEIGHT * TILE_SIZE);
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, TILE_SIZE));

        this.highScoreLabel = new Label("HIGHSCORE: --");
        highScoreLabel.setStyle("-fx-text-fill: white");
        highScoreManager = new HighScoreManager(highScoreLabel);

        AnchorPane.setLeftAnchor(restartButton, (double) TILE_SIZE);
        AnchorPane.setBottomAnchor(restartButton, (double) TILE_SIZE);
        AnchorPane.setRightAnchor(highScoreLabel, (double) TILE_SIZE);
        AnchorPane.setBottomAnchor(highScoreLabel, (double) TILE_SIZE);
        topBar.getChildren().addAll(scoreLabel, highScoreLabel, restartButton);

        this.grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setPadding(new Insets(5));
        grid.setAlignment(Pos.CENTER);

        setupGrid(grid);

        if (pos.getY() - START_SIZE < 0) return; //Starting snake is too large to fit onscreen

        setup(false);

        root.setCenter(topBar);
        root.setBottom(grid);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        this.scene = new Scene(root, width, height);
        setKeyEvents();
        primaryStage.setScene(scene);

        (mainloop = new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                updateFrame();
            }
        }).start();

        primaryStage.show();
    }

    private void setupGrid(GridPane grid) {
        for(int i = 0; i < TILE_WIDTH; i++) {
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setPrefWidth(TILE_SIZE);
            grid.getColumnConstraints().add(constraint);
        }

        for(int i = 0; i < TILE_HEIGHT; i++) {
            RowConstraints constraint = new RowConstraints();
            constraint.setPrefHeight(TILE_SIZE);
            grid.getRowConstraints().add(constraint);
        }
    }

    private void updateFrame() {
        frame++;
        if (frame % (FRAME_RATE / SNAKE_SPEED) == 0) {
            move();
        }
    }

    private void move() {
        if (changedDirection) changedDirection = false;
        pos.add(facing.getChange());

        if(pos.getX() < 0 || pos.getX() >= TILE_WIDTH || pos.getY() < 0 || pos.getY() >= TILE_HEIGHT) {
            gameOver();
            return;
        }
        //System.out.println("Pos: " + pos.toString());
        for (Tile tile : snake) {
            //System.out.println("TILE: " + tile.getPos().toString());
            if (pos.equals(tile.getPos())) {
                //System.out.println(pos.toString() + " : " + tile.getPos().toString());
                gameOver();
                return;
            }
        }

        Tile tile = new Tile(TILE_SIZE, new Vector(pos.getX(), pos.getY()), Color.GREEN);
        grid.add(tile.getRectangle(), tile.getX(), tile.getY());
        snake.add(tile);

        if (pos.equals(foodPos))
        {
            grid.getChildren().remove(foodTile.getRectangle());
            score++;
            updateScore();
            generateFood();
        }
        else {
            //Only Remove Tail if you haven't found food - This causing the snake to
            //'grow' upon eating food.
            grid.getChildren().remove(snake.get(0).getRectangle());
            snake.remove(0);
        }
    }

    private void generateFood() {
        Random random = new Random();
        while (true) {
            boolean valid = true;
            Vector newFoodPos = new Vector(random.nextInt(TILE_WIDTH), random.nextInt(TILE_HEIGHT));
            for (Tile tile : snake) {
                if (newFoodPos.equals(tile.getPos())) {
                    valid = false;
                    break;
                }
            }
            if (!valid) continue;
            foodTile = new Tile(TILE_SIZE, new Vector(newFoodPos.getX(), newFoodPos.getY()), Color.RED);
            grid.add(foodTile.getRectangle(), foodTile.getX(), foodTile.getY());
            foodPos = newFoodPos;
            return;
        }
    }

    private void gameOver() {
        gameOver = true;
        mainloop.stop();

        for (Tile tile : snake) {
            tile.setColour(Color.RED);
        }
        managePopup();
    }

    private void managePopup() {
        popup = new Popup();
        popup.setAutoHide(true);
        GridPane popupGrid = new GridPane();
        popupGrid.setPadding(new Insets(5));
        popupGrid.setStyle("-fx-background-color: white");
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, TILE_SIZE));
        gameOverLabel.setAlignment(Pos.CENTER);
        popupGrid.add(gameOverLabel, 1, 0);

        int highScoresOffset = 1;
        Label[] highscoreLabels = new Label[highScoreManager.getHighScores().length];

        if (highScoreManager.isNewHighScore(score)) {
            Label highScoreName = new Label("Name: ");
            TextField nameInput = new TextField();
            Button submit = new Button("Submit");
            submit.setOnMouseClicked(event -> {
                if (!nameInput.getText().equals(""))
                {
                    highScoreManager.addNewHighScore(nameInput.getText(), score);
                    popupGrid.getChildren().removeAll(highScoreName, nameInput, submit);
                    HighScore[] highScores = highScoreManager.getHighScores();
                    for (int i = 0; i < highScores.length; i++)
                    {
                        if (highScores[i] == null) break; //No more after the first null
                        popupGrid.getChildren().remove(highscoreLabels[i]);
                    }

                    setPopupScores(popupGrid, 2, highscoreLabels);
                }
            });
            popupGrid.add(highScoreName, 0, 1);
            popupGrid.add(nameInput, 1, 1);
            popupGrid.add(submit, 3, 1);
            highScoresOffset = 2;
        }
        setPopupScores(popupGrid, highScoresOffset, highscoreLabels);

        Button close = new Button("Close");
        close.setOnMouseClicked(event -> popup.hide());
        popupGrid.add(close, 1, highScoresOffset + highScoreManager.getHighScores().length);

        popup.getContent().add(popupGrid);
        popup.show(primaryStage);

    }

    private void setPopupScores(GridPane popupGrid, int highScoresOffset, Label[] labels) {
        HighScore[] highScores = highScoreManager.getHighScores();
        for (int i = 0; i < highScores.length; i++) {
            if (highScores[i] == null) return; //No more after the first null

            Label highscore = highScores[i].getAsLabel(i + 1);
            labels[i] = highscore;
            highscore.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, TILE_SIZE / 1.5d));
            popupGrid.add(highscore, 1, i + highScoresOffset, 4, 1);
        }
    }

    private void setKeyEvents() {
        scene.setOnKeyPressed(event -> {
            if (changedDirection) return;
            Direction newDirection = Direction.of(event.getText());
            if (newDirection == null) return;
            //Trying to do a 180 turn:
            if (Math.abs(newDirection.getId() - facing.getId()) == 2) return;

            facing = newDirection;
            changedDirection = true;
        });
    }

    private void updateScore() {
        scoreLabel.setText("SCORE: " + score);
    }

    public void setup(boolean restart) {
        if (restart)
        {
            for (Tile tile : snake) {
                grid.getChildren().remove(tile.getRectangle());
            }
            grid.getChildren().remove(foodTile.getRectangle());

            if (gameOver) {
                mainloop.start();
                gameOver = false;
            }

            snake = new ArrayList<>();
            facing = Direction.DOWN;
            pos = new Vector(TILE_WIDTH / 2, TILE_HEIGHT / 2);
            score = 0;
            changedDirection = false;
            updateScore();
            highScoreManager.getHighScoresFromFile();
        }

        for (int i = START_SIZE - 1; i >= 0; i--)
        {
            Tile tile = new Tile(TILE_SIZE, new Vector(pos.getX(), pos.getY() - i), Color.GREEN);
            grid.add(tile.getRectangle(), tile.getX(), tile.getY());
            //grid.add(new Label(String.valueOf(i)), tile.getX() + 1, tile.getY());
            snake.add(tile);
        }

        generateFood();
    }
}
