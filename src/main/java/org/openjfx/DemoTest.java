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
                        CirclePanel newItem = newCir(Global.mousePressedX - Config.DEFAULT_CIRCLE_RADIUS, Global.mousePressedY - Config.DEFAULT_CIRCLE_RADIUS, parent,Config.DEFAULT_CIRCLE_RADIUS,false);
                        // 保存撤销
                        ActionGroup actionGroup = new ActionGroup();
                        actionGroup.setType(Config.actionTypeNew);
                        List<ActionItem> items = new ArrayList<>();
                        ActionItem item = new ActionItem();
                        item.setItemNum(String.valueOf(Global.cNum));
                        item.setLayoutX(Global.mousePressedX - Config.DEFAULT_CIRCLE_RADIUS);
                        item.setLayoutY(Global.mousePressedY - Config.DEFAULT_CIRCLE_RADIUS);
                        item.setItem(newItem);
                        items.add(item);
                        actionGroup.setItems(items);
                        Global.addUndo(actionGroup);
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
                if (event.getTarget() instanceof AnchorPane && Math.abs(Global.mouseReleasedX - Global.mousePressedX) > 10
                        && Math.abs(Global.mouseReleasedY - Global.mousePressedY) > 10) {
                    unSelectAll();
                    batchSelectItem(Math.min(Global.mouseReleasedX, Global.mousePressedX),
                            Math.max(Global.mouseReleasedX, Global.mousePressedX),
                            Math.min(Global.mouseReleasedY, Global.mousePressedY),
                            Math.max(Global.mouseReleasedY, Global.mousePressedY));
                } else if(event.getTarget() instanceof AnchorPane){
                    unSelectAll();
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

    private CirclePanel newCir(double x, double y, AnchorPane parent, double r, boolean isSelected) {
        CirclePanel cp = new CirclePanel(x, y, String.valueOf(Global.cNum++), r);
        if(isSelected){
            cp.selected();
        }
        updateChildren(parent, cp, true);
        return cp;
    }

    private void batchSelectItem(double xStart, double xEnd, double yStart, double yEnd) {
        System.out.println(String.format("xStart=%s,xEnd=%s,yStart=%s,yEnd=%s", xStart, xEnd, yStart, yEnd));
        Global.cl.forEach(item -> {
            if (item.getCircle().getLayoutX() >= xStart
                    && item.getCircle().getLayoutX() <= xEnd &&
                    item.getCircle().getLayoutY() >= yStart
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
                    List<CirclePanel> deletedItems = new ArrayList<>();
                    while (iterator.hasNext()) {
                        CirclePanel next = iterator.next();
                        if (next.isSelected()) {
                            ((AnchorPane) scene.getRoot()).getChildren().remove(next.getCircle());
                            iterator.remove();
                            deletedItems.add(next);
                        }
                    }
                    // 保存撤销
                    ActionGroup actionGroup = new ActionGroup();
                    actionGroup.setType(Config.actionTypeDelete);
                    List<ActionItem> items = null;
                    for(CirclePanel circlePanel :deletedItems){
                        if(items == null){
                            items = new ArrayList<>();
                        }
                        ActionItem item = new ActionItem();
                        item.setItemNum(circlePanel.getCenterTextStr());
                        item.setLayoutX(circlePanel.getCircle().getLayoutX());
                        item.setLayoutY(circlePanel.getCircle().getLayoutY());
                        item.setItem(circlePanel);
                        items.add(item);
                    }
                    if(items != null){
                        actionGroup.setItems(items);
                        Global.addUndo(actionGroup);
                    }
                } else if (("C".equals(event.getCode().toString()) || "X".equals(event.getCode().toString())) && Global.isCtrlPressed) {
                    // 复制
                    List<CirclePanel> selectedPanel = getSelectedPanel();
                    if(selectedPanel != null){
                        Iterator<CirclePanel> iterator = selectedPanel.iterator();
                        ActionGroup actionGroup = new ActionGroup();
                        List<ActionItem> actionItems = new ArrayList<>();
                        while (iterator.hasNext()) {
                            CirclePanel item = iterator.next();
                            ActionItem aItem = new ActionItem();
                            aItem.setItemNum(item.getCenterTextStr());
                            aItem.setLayoutX(item.getCircle().getLayoutX());
                            aItem.setLayoutY(item.getCircle().getLayoutY());
                            aItem.setR(item.getR());
                            actionItems.add(aItem);
                            if("X".equals(event.getCode().toString())){
                                ((AnchorPane) scene.getRoot()).getChildren().remove(item.getCircle());
                                Global.cl.remove(item);
                            }
                        }
                        
                        actionGroup.setItems(actionItems);
                        Global.setCopy(actionGroup);
                        if("X".equals(event.getCode().toString())){
                            Global.addUndo(actionGroup);
                        }
                        System.out.println("复制: " + actionItems.size());
                    }
                } else if("V".equals(event.getCode().toString()) && Global.isCtrlPressed){
                    // 释放所有已选择
                    unSelectAll();
                    // 粘贴, 坐标偏移, 重新计算index
                    ActionGroup copy = Global.getCopy();
                    if(copy != null){
                        copy.getItems().forEach(item->{
                            AnchorPane parent = (AnchorPane) scene.getRoot();
                            CirclePanel newItem = newCir(item.getLayoutX() + Config.PASTE_XY_OFFSET, item.getLayoutY() + Config.PASTE_XY_OFFSET, parent, item.getR(),true);
                        });
                        System.out.println("粘贴: " + copy.getItems().size());
                    }
                    
                } else if("Z".equals(event.getCode().toString()) && Global.isCtrlPressed){
                    // 撤销
                    ActionGroup unDo = Global.unDo();
                    if(unDo != null){
                        // 检查类型
                        if(Config.actionTypeNew.equals(unDo.getType())){
                            // 新建, 进行删除
                            unDo.getItems().forEach(iitem->{
                                ((AnchorPane) scene.getRoot()).getChildren().remove(iitem.getItem().getCircle());
                                Global.cl.remove(iitem.getItem());
                            });
                        }else if(Config.actionTypeDelete.equals(unDo.getType())){
                            // 删除, 创建
                            unDo.getItems().forEach(iitem->{
                                ((AnchorPane) scene.getRoot()).getChildren().add(iitem.getItem().getCircle());
                                Global.cl.add(iitem.getItem());
                            });
                        }else if(Config.actionTypeMove.equals(unDo.getType())){
                            // 移动, 恢复移动前位置
                            unDo.getItems().forEach(iitem->{
                                CirclePanel circlePanel = iitem.getItem();
                                StackPane stackPane = circlePanel.getCircle();
                                stackPane.setLayoutX(iitem.getBeforeMoveLayoutX());
                                stackPane.setLayoutY(iitem.getBeforeMoveLayoutY());
                            });
                        }else if(Config.actionTypeChangeR.equals(unDo.getType())){
                            // 改变半径
                        }
                    }
                } else if("Y".equals(event.getCode().toString()) && Global.isCtrlPressed){
                    // 重做
                    ActionGroup reDo = Global.reDo();
                    if(reDo != null){
                        // 检查类型
                        if(Config.actionTypeNew.equals(reDo.getType())){
                            // 新建
                            reDo.getItems().forEach(iitem->{
                                ((AnchorPane) scene.getRoot()).getChildren().add(iitem.getItem().getCircle());
                                Global.cl.add(iitem.getItem());
                            });
                        }else if(Config.actionTypeDelete.equals(reDo.getType())){
                            // 删除
                            reDo.getItems().forEach(iitem->{
                                ((AnchorPane) scene.getRoot()).getChildren().remove(iitem.getItem().getCircle());
                                Global.cl.remove(iitem.getItem());
                            });
                        }else if(Config.actionTypeMove.equals(reDo.getType())){
                            // 移动
                            reDo.getItems().forEach(iitem->{
                                CirclePanel circlePanel = iitem.getItem();
                                StackPane stackPane = circlePanel.getCircle();
                                stackPane.setLayoutX(iitem.getAfterMoveLayoutX());
                                stackPane.setLayoutY(iitem.getAfterMoveLayoutY());
                            });
                        }else if(Config.actionTypeChangeR.equals(reDo.getType())){
                            // 改变半径
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

    private List<CirclePanel> getSelectedPanel() {
        List<CirclePanel> circlePanels = null;
        for (CirclePanel item : Global.cl) {
            if (item.isSelected()) {
                if (circlePanels == null) {
                    circlePanels = new ArrayList<>();
                }
                circlePanels.add(item);
            }
        }
        return circlePanels;
    }

    private void unSelectAll() {
        List<CirclePanel> selectedPanel = getSelectedPanel();
        if(selectedPanel != null){
            selectedPanel.forEach(CirclePanel::unSelected);
        }
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
