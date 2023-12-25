package com.example.stickhero;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.security.Key;

public class Pillar extends Rectangle {
    public Pillar(int x, int y, int width) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(300);
    }

    public Pillar(Rectangle rectangle) {
        this.setX(rectangle.getLayoutX());
        this.setY(rectangle.getLayoutY());
        this.setWidth(rectangle.getWidth());
        this.setHeight(300);
    }

    public void goToPos(int x, int y, int duration) {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration),
                new KeyValue(this.xProperty(), x),
                new KeyValue(this.yProperty(), y)
        ));

        timeline.play();

        this.setX(x);
        this.setY(y);
    }

    public void goToPos(int x, int y, int duration, EventHandler<ActionEvent> value) {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(duration),
                new KeyValue(this.xProperty(), x),
                new KeyValue(this.yProperty(), y)
        ));

        timeline.setOnFinished(value);

        timeline.play();
    }
}
