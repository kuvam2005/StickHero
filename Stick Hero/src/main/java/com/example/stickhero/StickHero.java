package com.example.stickhero;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class StickHero extends ImageView {
    private boolean flipped = false;
    private Timeline cherryCollisionHandler;
    private Timeline pillarCollisionHandler;
    private Timeline currentTimeline;
    private GameEngine gameEngine;
    public StickHero(ImageView image) {
        this.setImage(image.getImage());
        this.setX(image.getLayoutX());
        this.setY(image.getLayoutY());
        this.setFitHeight(image.getFitHeight());
        this.setFitWidth(image.getFitWidth());
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void goToPos(int x, int y, int duration) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration),
                new KeyValue(this.xProperty(), x),
                new KeyValue(this.yProperty(), y)
        ));

        timeline.play();
        currentTimeline = timeline;
    }

    public void goToPos(int x, int y, int duration, EventHandler<ActionEvent> value) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration),
                new KeyValue(this.xProperty(), x)
        ));

        timeline.setOnFinished(value);

        timeline.play();
        currentTimeline = timeline;
    }

    public void levelFailedDrop(Bridge bridge) {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),
                new KeyValue(this.xProperty(), bridge.getHeight() + this.getFitWidth())));

        timeline.setOnFinished(event -> {
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(300),
                    new KeyValue(this.yProperty(), 800)));

            timeline1.setOnFinished(event1 -> gameEngine.gameOver());
            timeline1.play();
        });

        timeline.play();
    }

    public void crossBridge(Bridge bridge, Pillar nextPillar, GameEngine gameEngine) {
        if ((bridge.getHeight() >= nextPillar.getX() - bridge.getX()) && (bridge.getHeight() <= nextPillar.getX() + nextPillar.getWidth() - bridge.getX() - bridge.getWidth())) {
            goToPos((int) (nextPillar.getX() + nextPillar.getWidth() - this.getFitWidth()), (int) this.getY(), 1000, event -> {
                gameEngine.nextLevel();
                if (cherryCollisionHandler != null) {
                    cherryCollisionHandler.stop();
                }
                if (pillarCollisionHandler != null) {
                    pillarCollisionHandler.stop();
                }
            });
        }
        else {
            levelFailedDrop(bridge);
        }
    }

    public void flip() {
        if (!flipped) {
            this.setY(this.getY() + this.getFitHeight());
            this.setScaleY(-1);
            flipped = true;
        }
        else {
            this.setY(this.getY() - this.getFitHeight());
            this.setScaleY(1);
            flipped = false;
        }
    }

    public void setCherryCollisionHandler(Cherry cherry) {
        int checkAfter = 10;
        Timeline checkCollisionTimeline = new Timeline(new KeyFrame(Duration.millis(checkAfter), event -> {
            if (getX() <= cherry.getX() && getX() + getFitWidth() >= cherry.getX()) {
                if (flipped) {
                    cherry.collect();
                }
            }
        }));
        cherry.setToStop(checkCollisionTimeline);
        checkCollisionTimeline.setCycleCount(Animation.INDEFINITE);
        checkCollisionTimeline.play();
        this.cherryCollisionHandler = checkCollisionTimeline;
    }

    public void setPillarCollisionHandler(Pillar pillar) {
        int checkAfter = 10;
        Timeline checkCollisionTimeline = new Timeline(new KeyFrame(Duration.millis(checkAfter), event -> {
            if (getX() <= pillar.getX() && getX() + getFitWidth() >= pillar.getX()) {
                if (flipped) {
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),
                            new KeyValue(this.yProperty(), 800)));
                    timeline.setOnFinished(newEvent -> gameEngine.gameOver());
                    timeline.play();
                    currentTimeline.stop();
                }
            }
        }));
        checkCollisionTimeline.setCycleCount(Animation.INDEFINITE);
        checkCollisionTimeline.play();
        this.pillarCollisionHandler = checkCollisionTimeline;
    }

    public void reset(int defaultX, int defaultY) {
        if (flipped) {
            flip();
        }
        this.setX(defaultX);
        this.setY(defaultY);
    }
}
