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
            final List<CirclePanel> cps = new ArrayList<>();
            Global.cl.forEach(item->{
                if(item.isSelected()){
                    cps.add(item);
                }
            });
            this.setBros(cps);
        });
        this.circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // 判断SHIFT是否按下, 决定调整大小还是移动位置
                if(Global.isShiftPressed){
                    double newR = srcRadius + (event.getX() - pX);
                    if(newR <= 5){
                        newR = 5;
                    }
                    circle.setRadius(newR);
                    Global.isChangeCirR = true;
                }else{
                    moveC(event.getSceneX(),event.getSceneY(),cX,cY);
                }
            };
        });
        this.circle.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                bros.forEach(item->item.saveR());
                saveR();
                Global.isChangeCirR = false;
                bros = null;
            }
        });
    }

    public void moveC(double sX, double sY, double cX, double cY){
        stack.setLayoutX(sX - cX);
        stack.setLayoutY(sY - cY);
    }

    public void saveR(){
        this.srcRadius = this.circle.getRadius();
    }
}
