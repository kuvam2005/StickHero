package com.example.stickhero;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class StickHeroApplication extends Application {
    private static Scene scene;
    private static int height = 775;
    private static int width = 380;
    private static Button playButton;
    private static Label titleText;
    private static StickHero stickhero;
    private static Pane pane;
    private static GameEngine gameEngine;
    private static Pillar startingPillar;

    private static int cherry = 0;
    private static int highScore = 0;

    private static Stage st;

    @Override
    public void start(Stage stage) throws IOException {
        st = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        scene = new Scene(fxmlLoader.load(), width, height);

        playButton = (Button) scene.lookup("#playButton");
        titleText = (Label) scene.lookup("#titleText");
        // Singleton
        stickhero = new StickHero((ImageView) scene.lookup("#stickman"));
        pane = (Pane) scene.lookup("#pane");
        startingPillar = new Pillar(((Rectangle) scene.lookup("#startingPillar")));
        pane.getChildren().add(startingPillar);
        pane.getChildren().add(stickhero);

        scene.lookup("#startingPillar").setVisible(false);
        scene.lookup("#stickman").setVisible(false);

        // Singleton
        gameEngine = new GameEngine(scene, stickhero, startingPillar);

        stage.setTitle("Stick Hero");
        stage.setScene(scene);

        playButton.setOnAction(event -> {
            titleText.setVisible(false);
            playButton.setVisible(false);
            gameEngine.start();
        });

        stage.show();
    }

    public static void restart() {
        FXMLLoader fxmlLoader = new FXMLLoader(StickHeroApplication.class.getResource("main-view.fxml"));
        try {
            scene = new Scene(fxmlLoader.load(), width, height);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        playButton = (Button) scene.lookup("#playButton");
        titleText = (Label) scene.lookup("#titleText");
        stickhero = new StickHero((ImageView) scene.lookup("#stickman"));
        pane = (Pane) scene.lookup("#pane");
        startingPillar = new Pillar(((Rectangle) scene.lookup("#startingPillar")));
        pane.getChildren().add(startingPillar);
        pane.getChildren().add(stickhero);

        scene.lookup("#startingPillar").setVisible(false);
        scene.lookup("#stickman").setVisible(false);

        gameEngine = new GameEngine(scene, stickhero, startingPillar);

        st.setScene(scene);

        playButton.setOnAction(event -> {
            titleText.setVisible(false);
            playButton.setVisible(false);
            gameEngine.start();
        });
    }

    public void reset() {
        playButton.setVisible(true);
        titleText.setVisible(true);
    }

    public static int getCherry() {
        return cherry;
    }

    public static void setCherry(int cherry) {
        StickHeroApplication.cherry = cherry;
    }

    public static int getHighScore() {
        return highScore;
    }

    public static void setHighScore(int highScore) {
        StickHeroApplication.highScore = highScore;
    }

    public static void incrementCherries() {
        cherry++;
    }

    public static void main(String[] args) {
        launch();
    }
}