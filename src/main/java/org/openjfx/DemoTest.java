package org.openjfx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DemoTest extends Application {

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

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        AnchorPane parent = new AnchorPane();
        // 状态显示文字
        Text keyStatusText = new Text(5, 25,
                String.format(Config.KEY_STATUS_TEXT_TMP, Global.isShiftPressed, Global.isAltPressed,
                        Global.isCtrlPressed));
        keyStatusText.setFont(Font.font(20));
        Text mouseStatusText = new Text(5, 45,
                String.format(Config.MOUSE_STATUS_TEXT_TMP, Global.isMousePressed, Global.mousePressedX,
                        Global.mousePressedY, Global.mouseReleasedX, Global.mouseReleasedY, Global.mouseNowX,
                        Global.mouseNowY));
        mouseStatusText.setFont(Font.font(12));

        Scene scene = new Scene(parent);
        addSceneKeyEvent(keyStatusText, scene);
        addSceneMouseEvent(mouseStatusText, scene, primaryStage);
        /* -------------------------- */

        /* --------------------------- */
        parent.getChildren().addAll(keyStatusText, mouseStatusText, mouseSelectR);
        showUi(primaryStage, scene);
    }

    /**
     * 更新画布子元素
     * 
     * @param parent
     * @param shape
     * @param isAdd
     */
    private void updateChildren(AnchorPane parent, StackPane circle, boolean isAdd) {
        if (isAdd) {
            parent.getChildren().add(circle);
        } else {
            if (parent.getChildren().contains(circle)) {
                parent.getChildren().remove(circle);
            }
        }
    }

    private void updateChildren(AnchorPane parent, CirclePanel shapePanel, boolean isAdd) {
        if (Objects.isNull(shapePanel)) {
            return;
        }
        this.updateChildren(parent, shapePanel.getCircle(), isAdd);
        Global.cl.add(shapePanel);
    }

    private void addSceneMouseEvent(Text mouseStatusText, Scene scene, Stage primaryStage) {
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // System.out.println(event.getTarget());
                if (event.getTarget() instanceof AnchorPane && !Global.isChangeCirR) {
                    if (Global.isShiftPressed) {
                        // 创建圆形
                        AnchorPane parent = (AnchorPane) scene.getRoot();
                        newCir(Global.mousePressedX, Global.mousePressedY,parent);
                    }
                }
            }
        });
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Global.isMousePressed = true;
                Global.mousePressedX = event.getSceneX();
                Global.mousePressedY = event.getSceneY();
                updateMouseText(mouseStatusText);
            }
        });
        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Global.isMousePressed = false;
                Global.mouseReleasedX = event.getSceneX();
                Global.mouseReleasedY = event.getSceneY();
                updateMouseText(mouseStatusText);
                // 选择框关闭显示
                mouseSelectR.setVisible(false);
                // 判断框选
                if (Math.abs(Global.mouseReleasedX - Global.mousePressedX) > 10
                        && Math.abs(Global.mouseReleasedY - Global.mousePressedY) > 10) {
                    batchSelectItem(Math.min(Global.mouseReleasedX, Global.mousePressedX),
                            Math.max(Global.mouseReleasedX, Global.mousePressedX),
                            Math.min(Global.mouseReleasedY, Global.mousePressedY),
                            Math.max(Global.mouseReleasedY, Global.mousePressedY));
                } else {
                    Global.cl.forEach(item -> item.unSelected());
                }
            }
        });
        scene.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scene.startFullDrag();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Global.mouseNowX = event.getSceneX();
                Global.mouseNowY = event.getSceneY();
                updateMouseText(mouseStatusText);
                if (event.getTarget() instanceof AnchorPane) {
                    // 显示选择框
                    mouseSelectR.setVisible(true);
                    mouseSelectR.setX(Math.min(Global.mouseNowX, Global.mousePressedX));
                    mouseSelectR.setY(Math.min(Global.mouseNowY, Global.mousePressedY));
                    mouseSelectR.setWidth(Math.abs(Global.mouseNowX - Global.mousePressedX));
                    mouseSelectR.setHeight(Math.abs(Global.mouseNowY - Global.mousePressedY));
                } else if (event.getTarget() instanceof Circle && !Global.isCirDrag) {
                    System.out.println("拖动圆形");
                    Global.isCirDrag = true;
                }
            }
        });

    }

    private void newCir(double x, double y, AnchorPane parent){
        CirclePanel cp = new CirclePanel(x,y, String.valueOf(Global.cNum++));
                        updateChildren(parent, cp, true);
    }

    private void batchSelectItem(double xStart, double xEnd, double yStart, double yEnd) {
        System.out.println(String.format("xStart=%s,xEnd=%s,yStart=%s,yEnd=%s",xStart, xEnd, yStart, yEnd));
        Global.cl.forEach(item -> {
            if (item.getCircle().getLayoutX() >= xStart
                    && item.getCircle().getLayoutX() <= xEnd &&
                    item.getCircle().getLayoutY()>= yStart
                    && item.getCircle().getLayoutY() <= yEnd) {
                item.selected();
                System.out.println(item.getCircle().getLayoutX() + " - " + item.getCircle().getLayoutY());
            }
        });
    }

    /**
     * 添加键盘监听
     * 
     * @param statusText 按键状态文字
     * @param scene
     */
    private void addSceneKeyEvent(Text statusText, Scene scene) {
        // 键盘监听
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if ("SHIFT".equals(event.getCode().toString())) {
                    Global.isShiftPressed = true;
                } else if ("ALT".equals(event.getCode().toString())) {
                    Global.isAltPressed = true;
                } else if ("CONTROL".equals(event.getCode().toString())) {
                    Global.isCtrlPressed = true;
                } else if ("DELETE".equals(event.getCode().toString())) {
                    // 删除元素
                    Iterator<CirclePanel> iterator = Global.cl.iterator();
                    while (iterator.hasNext()){
                        CirclePanel next = iterator.next();
                        if(next.isSelected()){
                            ((AnchorPane)scene.getRoot()).getChildren().remove(next.getCircle());
                            iterator.remove();
                        }
                    }
                }
                statusText.setText(String.format(Config.KEY_STATUS_TEXT_TMP, Global.isShiftPressed, Global.isAltPressed,
                        Global.isCtrlPressed));
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if ("SHIFT".equals(event.getCode().toString())) {
                    Global.isShiftPressed = false;
                } else if ("ALT".equals(event.getCode().toString())) {
                    Global.isAltPressed = false;
                } else if ("CONTROL".equals(event.getCode().toString())) {
                    Global.isCtrlPressed = false;
                }
                statusText.setText(String.format(Config.KEY_STATUS_TEXT_TMP, Global.isShiftPressed, Global.isAltPressed,
                        Global.isCtrlPressed));
            }
        });
    }

    /**
     * 改变按键状态信息
     * 
     * @param text
     */
    private void updateMouseText(Text text) {
        text.setText(String.format(Config.MOUSE_STATUS_TEXT_TMP, Global.isMousePressed, Global.mousePressedX,
                Global.mousePressedY, Global.mouseReleasedX, Global.mouseReleasedY, Global.mouseNowX,
                Global.mouseNowY));
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
