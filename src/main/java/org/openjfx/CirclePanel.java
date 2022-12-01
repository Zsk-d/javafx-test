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

    private double connectX = 0;
    private double connectY = 0;

    public StackPane getCircle(){
        return this.stack;
    }

    public CirclePanel(double x, double y, String centerTextStr) {
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
                }
            }
            System.out.println(String.format("同时移动: %s",cps));
            this.setBros(cps);
        });
        this.circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // 判断SHIFT是否按下, 决定调整大小还是移动位置
                if(Global.isShiftPressed){
                    double newR = srcRadius + (event.getX() - pX);
                    System.out.println("newR = " + newR + ", px = " + pX + ", event x = " + event.getX());
                    if(newR <= 5){
                        newR = 5;
                    }
                    circle.setRadius(newR);
                    Global.isChangeCirR = true;
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
                if(bros != null){
                    bros.forEach(item->item.saveR());
                    bros = null;
                }
                saveR();
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

    @Override
    public String toString() {
        return "num: " + this.centerText.getText();
    }
}
