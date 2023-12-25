package com.example.stickhero;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;

import java.util.Random;

public class GameEngine {
    private final Scene scene;
    private final Pane pane;
    private final StickHero stickhero;
    private Pillar startingPillar;
    private Pillar nextPillar;
    private Label scoreLabel;
    private final int defaultX = 60;
    private final int defaultY = 570;
    private final int defaultPillarY;
    private Bridge currentBridge = null;
    private Cherry cherry;
    private Random random = new Random();
    private int score = 0;
    private ImageView cherryImg;
    private Label cherryScore;
    public GameEngine(Scene scene, StickHero stickhero, Pillar startingPillar) {
        this.scene = scene;
        this.stickhero = stickhero;
        stickhero.setGameEngine(this);
        this.startingPillar = startingPillar;
        this.defaultPillarY = (int) (defaultY + stickhero.getFitHeight());
        this.pane = (Pane) scene.lookup("#pane");
        this.scoreLabel = (Label) scene.lookup("#scoreLabel");
        this.cherryImg = (ImageView) scene.lookup("#cherryImg");
        this.cherryScore = (Label) scene.lookup("#cherryScore");
        this.cherryScore.setText(String.valueOf(StickHeroApplication.getCherry()));
    }

    public void start() {
        setPillar(startingPillar);
        stickhero.goToPos(defaultX, defaultY, 100);

        scoreLabel.setVisible(true);
        cherryImg.setVisible(true);
        cherryScore.setVisible(true);

        generatePillar();
        addMouseInput();
    }

    public void makeTower() {
        if (currentBridge == null) {
            currentBridge = new Bridge((int) (defaultX + stickhero.getFitWidth()), (int) (defaultY + stickhero.getFitHeight()));
            pane.getChildren().add(currentBridge);
        }
        currentBridge.incrementHeight();
    }

    public void blockMouseInput() {
        scene.setOnMousePressed(mouseEvent -> {});

        scene.setOnMouseReleased(mouseEvent -> {});
    }

    public void addMouseInput() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                makeTower();
            }
        };
        timer.stop();

        scene.setOnMousePressed(mouseEvent -> timer.start());

        scene.setOnMouseReleased(mouseEvent -> {
            timer.stop();
            addMouseFlipInput();
            currentBridge.dropBridge(stickhero, nextPillar, this);
        });
    }

    public void addMouseFlipInput() {
        scene.setOnMousePressed(mouseEvent -> stickhero.flip());
        scene.setOnMouseReleased(mouseEvent -> {});
    }

    public void setPillar(Pillar pillar) {
        pillar.goToPos((int) (defaultX + 40 - pillar.getWidth()), defaultPillarY, 100);
    }

    public void generatePillar() {
        int x = (int) (startingPillar.getX() + startingPillar.getWidth() + 10 + random.nextInt(190));
        int width = 10 + random.nextInt(380-60-x);
        nextPillar = new Pillar(400, defaultPillarY, width);
        pane.getChildren().add(nextPillar);
        nextPillar.goToPos(x, defaultPillarY, 200, event -> {
            addMouseInput();
            if (x - startingPillar.getX() - startingPillar.getWidth() > 40) {
                spawnCherry();
            }
            stickhero.setPillarCollisionHandler(nextPillar);
        });
    }

    public void spawnCherry() {
        cherry = new Cherry((int) ((startingPillar.getX() + startingPillar.getWidth() + nextPillar.getX() - 40)/2), defaultY + 60, this);
        pane.getChildren().add(cherry);
        stickhero.setCherryCollisionHandler(cherry);
    }

    public void nextLevel() {
        blockMouseInput();
        int diff = (int) (stickhero.getX() - defaultX);
        currentBridge.goToPos((int) (currentBridge.getX()), (int) currentBridge.getY() + diff, 200);
        startingPillar.goToPos((int) (startingPillar.getX() - diff), (int) startingPillar.getY(), 200);
        nextPillar.goToPos((int) (nextPillar.getX() - diff), (int) nextPillar.getY(), 200);
        if (cherry != null) {
            cherry.goToPos((int) (nextPillar.getX() - diff), (int) nextPillar.getY(), 200);
            cherry = null;
        }

        stickhero.goToPos(defaultX, defaultY, 200, event -> {
            generatePillar();
        });

        startingPillar = nextPillar;
        currentBridge = null;
        score++;
        scoreLabel.setText(String.valueOf(score));
    }

    public void incrementCherries() {
        StickHeroApplication.incrementCherries();
        updateCherryLabel();
    }

    public void updateCherryLabel() {
        cherryScore.setText(String.valueOf(StickHeroApplication.getCherry()));
    }

    public void revive() {
        scoreLabel.setVisible(true);
        stickhero.reset(defaultX, defaultY);
        pane.getChildren().remove(currentBridge);
        currentBridge = null;
        updateCherryLabel();
        addMouseInput();
    }

    public void gameOver() {
        blockMouseInput();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("end_screen.fxml"));
        try {
            scoreLabel.setVisible(false);
            Parent parent = fxmlLoader.load();
            pane.getChildren().add(parent);
            Label highScore = (Label) parent.lookup("#highScore");
            Label currentScore = (Label) parent.lookup("#currentScore");
            Button reviveButton = (Button) parent.lookup("#revive");
            Button restartButton = (Button) parent.lookup("#restart");
            Button homeButton = (Button) parent.lookup("#home");

            if (score > StickHeroApplication.getHighScore()) StickHeroApplication.setHighScore(score);

            highScore.setText(String.valueOf(StickHeroApplication.getHighScore()));
            currentScore.setText(String.valueOf(score));

            homeButton.setDisable(false);
            if (StickHeroApplication.getCherry() > 3) reviveButton.setDisable(false);

            homeButton.setOnAction(event -> StickHeroApplication.restart());

            restartButton.setOnAction(event -> {
                pane.getChildren().remove(parent);
                restart();
            });

            reviveButton.setOnAction(event -> {
                pane.getChildren().remove(parent);
                StickHeroApplication.setCherry(StickHeroApplication.getCherry() - 3);
                revive();
            });

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void restart() {
        score = 0;
        scoreLabel.setText(String.valueOf(score));
        revive();
    }
}
