package com.example.stickhero;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Bridge extends Rectangle {
    public Bridge(int x, int y) {
        this.setY(y);
        this.setWidth(4);
        this.setX(x - 15);
    }

    public void incrementHeight() {
        int incrementInterval = 5;
        this.setHeight(this.getHeight() + incrementInterval);
        this.setY(this.getY() - incrementInterval);
    }

    public void dropBridge(StickHero stickhero, Pillar nextPillar, GameEngine gameEngine) {

        Rotate rotation = new Rotate();
        rotation.setPivotX(this.getX() + this.getWidth());
        rotation.setPivotY(this.getY() + this.getHeight());

        this.getTransforms().add(rotation);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), 0)),
                new KeyFrame(Duration.millis(200), new KeyValue(rotation.angleProperty(), 90))
        );

        timeline.play();
        timeline.setOnFinished(event -> {
            stickhero.crossBridge(this, nextPillar, gameEngine);
        });
    }

    public void goToPos(int x, int y, int duration) {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration),
                new KeyValue(this.xProperty(), x),
                new KeyValue(this.yProperty(), y)
        ));

        timeline.play();

    }
}
