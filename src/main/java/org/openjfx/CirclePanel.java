package org.openjfx;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CirclePanel {
    
    final StackPane stack = new StackPane();
    private Circle circle = new Circle(Config.DEFAULT_CIRCLE_RADIUS);
    final Text centerText = new Text ();
    private List<CirclePanel> bros;

    private boolean selected;
    private double cX;
    private double cY;
    private double pX;
    private double srcRadius = Config.DEFAULT_CIRCLE_RADIUS;

    private double srcLayoutX = 0;
    private double srcLayoutY = 0;

    /**
     * 移动前的位置
     */
    private double beforeMoveLayoutX = 0;
    private double beforeMoveLayoutY = 0;
    /**
     * 移动后位置
     */
    private double afterMoveLayoutX = 0;
    private double afterMoveLayoutY = 0;

    /**
     * 缩放前和后的半径
     */
    private double beforeR = 0;
    private double afterR = 0;

    private double connectX = 0;
    private double connectY = 0;

    public void setR(double r){
        if(r < Config.MIN_CIRCLE_RADIUS){
            r = Config.MIN_CIRCLE_RADIUS;
        }
        this.circle.setRadius(r);
    }

    public StackPane getCircle(){
        return this.stack;
    }

    public double getR(){
        return this.circle.getRadius();
    }

    public String getCenterTextStr(){
        return this.centerText.getText();
    }
    public CirclePanel(double x, double y, String centerTextStr, double r) {
        this.circle.setStroke(Color.BLACK);
        this.circle.setFill(Color.WHITE);
        // AnchorPane.setLeftAnchor(circle, x);
        // AnchorPane.setTopAnchor(circle, y);
        this.setMouseEvent();
        this.centerText.setText(centerTextStr);
        centerText.setFont(Font.font(10));
        stack.getChildren().addAll(this.circle,this.centerText);
        stack.setLayoutX(x);
        stack.setLayoutY(y);
        this.srcLayoutX = x;
        this.srcLayoutY = y;
        this.beforeMoveLayoutX = x;
        this.beforeMoveLayoutY = y;
        this.afterMoveLayoutX = x;
        this.afterMoveLayoutY = y;
        this.circle.setRadius(r);
        this.beforeR = r;
        this.afterR = r;
    }

    public CirclePanel(double x, double y, String centerTextStr) {
        this(x, y, centerTextStr,Config.DEFAULT_CIRCLE_RADIUS);
    }

    public void setBros(List<CirclePanel> bros){
        this.bros = bros;
    }

    /**
     * 设定选中状态
     * @param selected
     */
    public void selected(){
        if(!this.selected){
            this.selected = true;
            this.circle.setFill(Color.web("#4e6ef2"));
            System.out.println("select de !!");
        }
    }
    public void unSelected(){
        if(this.selected){
            this.selected = false;
            this.circle.setFill(Color.WHITE);
        }
    }

    public boolean isSelected(){
        return this.selected;
    }

    public void toggleSelected(){
        if(this.selected){
            this.unSelected();
        }else{
            this.selected();
        }
    }

    private void setMouseEvent() {
        CirclePanel thisObj = this;
        this.circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // 切换选定
                if(!Global.isCirDrag){
                    selected();
                }
                Global.isCirDrag = false;
            }
        });
        this.circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // cX = event.getX() + stack.getWidth();
                // cY = event.getY() + stack.getHeight();
                cX = event.getX() + circle.getRadius();
                cY = event.getY() + circle.getRadius();

                pX = event.getX();
                beforeMoveLayoutX = stack.getLayoutX();
                beforeMoveLayoutX = stack.getLayoutX();
                // System.out.println(String.format("x=%s,cx=%s,r=%s", event.getX(),AnchorPane.getLeftAnchor(circle),circle.getRadius()));
                // System.out.println(String.format("y=%s,cy=%s,r=%s", event.getY(),AnchorPane.getTopAnchor(circle),circle.getRadius()));
            }
        });
        this.circle.setOnDragDetected((MouseEvent event) -> {
            circle.startFullDrag();
            Global.isCirDrag = true;
            // 插入其他已选择的图形
            List<CirclePanel> cps = null;
            for(CirclePanel item : Global.cl){
                if(!item.equals(this) && item.isSelected()){
                    if(cps == null){
                        cps = new ArrayList<>();
                    }
                    cps.add(item);
                    item.setConnectX(stack.getLayoutX() - item.getCircle().getLayoutX());
                    item.setConnectY(stack.getLayoutY() - item.getCircle().getLayoutY());
                    // 保存初始位置
                    item.beforeMoveLayoutX = item.getCircle().getLayoutX();
                    item.beforeMoveLayoutY = item.getCircle().getLayoutY();
                    // 保存初始半径
                    item.beforeR = item.getR();
                }
            }
            // 保存初始位置
            beforeMoveLayoutX = stack.getLayoutX();
            beforeMoveLayoutY = stack.getLayoutY();
            // 保存初始半径
            beforeR = getR();
            System.out.println(String.format("同时控制: %s",cps));
            this.setBros(cps);
        });
        this.circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // 判断SHIFT是否按下, 决定调整大小还是移动位置
                if(!event.getTarget().equals(circle)){
                    return;
                }
                if(Global.isShiftPressed){
                    double newR = srcRadius + (event.getX() - pX)/2;
                    // System.out.println("newR = " + newR + ", px = " + pX + ", event x = " + event.getX());
                    if(newR <= Config.MIN_CIRCLE_RADIUS){
                        newR = Config.MIN_CIRCLE_RADIUS;
                    }
                    circle.setRadius(newR);
                    Global.isChangeCirR = true;
                    final double tt = newR;
                    // 同步缩放
                    if(bros != null){
                        bros.forEach(item->{
                            item.setR(item.getR() + tt - srcRadius);
                            item.saveR();
                        });
                    }
                    saveR();
                    // stack.setLayoutX(srcLayoutX);
                    // stack.setLayoutY(srcLayoutY);
                    // System.out.println(stack.getLayoutX() + " - " + stack.getLayoutY());
                }else{
                    double ccX = event.getSceneX();
                    double ccY = event.getSceneY();
                    // System.out.println("ccX = " + ccX + ", ccy = " + ccY);
                    double[] moveRes = moveC(ccX,ccY,cX,cY);
                    // 同步移动
                    if(bros != null){
                        bros.forEach(item->{
                            item.getCircle().setLayoutX(moveRes[0] - item.getConnectX());
                            item.getCircle().setLayoutY(moveRes[1] - item.getConnectY());
                            // System.out.println("ccX = " + stack.getLayoutX() + ", ccy = " + stack.getLayoutY());
                        });
                    }
                }
            };
        });
        this.circle.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                afterMoveLayoutX = stack.getLayoutX();
                afterMoveLayoutY = stack.getLayoutY();
                afterR = getR();
                // 判断是否为移动
                boolean isMove = afterMoveLayoutX != beforeMoveLayoutX || afterMoveLayoutY != beforeMoveLayoutY;
                if(isMove){
                    System.out.println(String.format("移动 bx = %.2f, by = %.2f, ax = %.2f, ay = %.2f", beforeMoveLayoutX,beforeMoveLayoutY,afterMoveLayoutX,afterMoveLayoutY));
                }
                // 判断是否为变更大小
                boolean isChangeR = afterR != beforeR;
                if(isChangeR){
                    System.out.println(String.format("改变大小 原半径 = %.2f, 新半径 = %.2f",beforeR,afterR));
                }
                // 保存撤销
                ActionGroup actionGroup = null;
                List<ActionItem> items = null;
                if(isMove){
                    actionGroup = new ActionGroup();
                    items = new ArrayList<>();
                    actionGroup.setType(Config.actionTypeMove);
                    ActionItem actionItem = new ActionItem();
                    actionItem.setBeforeMoveLayoutX(thisObj.getBeforeMoveLayoutX());
                    actionItem.setBeforeMoveLayoutY(thisObj.getBeforeMoveLayoutY());
                    actionItem.setAfterMoveLayoutX(thisObj.getAfterMoveLayoutX());
                    actionItem.setAfterMoveLayoutY(thisObj.getAfterMoveLayoutY());
                    actionItem.setItem(thisObj);
                    items.add(actionItem);
                }
                if(isChangeR){
                    actionGroup = new ActionGroup();
                    items = new ArrayList<>();
                    actionGroup.setType(Config.actionTypeChangeR);
                    ActionItem actionItem = new ActionItem();
                    actionItem.setBeforeR(beforeR);
                    actionItem.setAfterR(afterR);
                    actionItem.setItem(thisObj);
                    items.add(actionItem);
                }
                if(bros != null){
                    for(CirclePanel item: bros){
                        item.saveR();
                        // 保存新位置
                        item.afterMoveLayoutX = item.getCircle().getLayoutX();
                        item.afterMoveLayoutY = item.getCircle().getLayoutY();
                        // 保存新半径
                        item.afterR = item.getR();
                        if(isMove){
                            // 保存撤销
                            ActionItem groupActionItem = new ActionItem();
                            groupActionItem.setItem(item);
                            groupActionItem.setBeforeMoveLayoutX(item.getBeforeMoveLayoutX());
                            groupActionItem.setBeforeMoveLayoutY(item.getBeforeMoveLayoutY());
                            groupActionItem.setAfterMoveLayoutX(item.getAfterMoveLayoutX());
                            groupActionItem.setAfterMoveLayoutY(item.getAfterMoveLayoutY());
                            items.add(groupActionItem);
                        }
                        if(isChangeR){
                            ActionItem groupActionItem = new ActionItem();
                            groupActionItem.setBeforeR(item.getBeforeR());
                            groupActionItem.setAfterR(item.getAfterR());
                            groupActionItem.setItem(item);
                            items.add(groupActionItem);
                        }
                    }
                    bros = null;
                }
                if(isMove || isChangeR){
                    actionGroup.setItems(items);
                    Global.addUndo(actionGroup);
                }
                saveR();
                saveLayoutLoc();
                Global.isChangeCirR = false;
            }
        });
    }

    public double[] moveC(double sX, double sY, double cX, double cY){
        double xd = sX - cX;
        double yd = sY - cY;
        stack.setLayoutX(xd);
        stack.setLayoutY(yd);
        return new double[]{xd,yd};
    }

    public void saveR(){
        this.srcRadius = this.circle.getRadius();
    }

    public void saveLayoutLoc(){
        this.srcLayoutX = this.stack.getLayoutX();
        this.srcLayoutY = this.stack.getLayoutY();
        System.out.println("save center xy!");
        System.out.println(stack.getLayoutX() + " - " + stack.getLayoutY());
    }
        
    public double getConnectX() {
        return connectX;
    }

    public void setConnectX(double connectX) {
        this.connectX = connectX;
    }

    public double getConnectY() {
        return connectY;
    }

    public void setConnectY(double connectY) {
        this.connectY = connectY;
    }

    public double getBeforeMoveLayoutX() {
        return beforeMoveLayoutX;
    }

    public void setBeforeMoveLayoutX(double beforeMoveLayoutX) {
        this.beforeMoveLayoutX = beforeMoveLayoutX;
    }

    public double getBeforeMoveLayoutY() {
        return beforeMoveLayoutY;
    }

    public void setBeforeMoveLayoutY(double beforeMoveLayoutY) {
        this.beforeMoveLayoutY = beforeMoveLayoutY;
    }

    public double getAfterMoveLayoutX() {
        return afterMoveLayoutX;
    }

    public void setAfterMoveLayoutX(double afterMoveLayoutX) {
        this.afterMoveLayoutX = afterMoveLayoutX;
    }

    public double getAfterMoveLayoutY() {
        return afterMoveLayoutY;
    }

    public void setAfterMoveLayoutY(double afterMoveLayoutY) {
        this.afterMoveLayoutY = afterMoveLayoutY;
    }

    public double getBeforeR() {
        return beforeR;
    }

    public void setBeforeR(double beforeR) {
        this.beforeR = beforeR;
    }

    public double getAfterR() {
        return afterR;
    }

    public void setAfterR(double afterR) {
        this.afterR = afterR;
    }

    @Override
    public String toString() {
        return "num: " + this.centerText.getText();
    }
}
