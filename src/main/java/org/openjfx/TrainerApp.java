package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TrainerApp extends Application {

    /**
     * 鼠标选择框
     */
    private static Rectangle mouseSelectR;

    static {
        mouseSelectR = new Rectangle();
        mouseSelectR.setStrokeWidth(1);
        mouseSelectR.setStroke(Color.BLACK);
        mouseSelectR.setVisible(false);
        mouseSelectR.setFill(null);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene mainUI = new MainUI().initScene(primaryStage, mouseSelectR);
        showUi(primaryStage, mainUI);
    }
    /**
     * 显示整体窗口
     * 
     * @param primaryStage
     * @param scene
     */
    private void showUi(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setWidth(Config.STAGE_WIDTH);
        primaryStage.setHeight(Config.STAGE_HEIGHT);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
