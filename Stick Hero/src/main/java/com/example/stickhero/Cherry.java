package com.example.stickhero;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Cherry extends ImageView {
    GameEngine gameEngine;
    Timeline toStop;
    public Cherry(int x, int y, GameEngine gameEngine) {
        this.setImage(new Image(String.valueOf(getClass().getResource("cherry.png"))));
        setX(x);
        setY(y);
        setFitHeight(40);
        setFitWidth(40);
        setVisible(true);
        this.gameEngine = gameEngine;
    }

    // Strategy
    public void goToPos(int x, int y, int duration) {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration),
                new KeyValue(this.xProperty(), x),
                new KeyValue(this.yProperty(), y)
        ));

        timeline.play();

    }

    public void collect() {
        gameEngine.incrementCherries();
        toStop.stop();
        setVisible(false);
    }

    public void setToStop(Timeline toStop) {
        this.toStop = toStop;
    }
}
