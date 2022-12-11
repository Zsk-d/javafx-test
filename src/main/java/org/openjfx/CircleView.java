package org.openjfx;

import java.util.List;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CircleView {

    final StackPane stack = new CircleModel();
    public Circle circle = new Circle(Config.DEFAULT_CIRCLE_RADIUS);
    final Text centerText = new Text();
    public List<CircleView> bros;

    public boolean selected;
    public double cX;
    public double cY;
    public double pX;
    public double srcRadius = Config.DEFAULT_CIRCLE_RADIUS;

    public double srcLayoutX = 0;
    public double srcLayoutY = 0;

    /**
     * 移动前的位置
     */
    public double beforeMoveLayoutX = 0;
    public double beforeMoveLayoutY = 0;
    /**
     * 移动后位置
     */
    public double afterMoveLayoutX = 0;
    public double afterMoveLayoutY = 0;

    /**
     * 缩放前和后的半径
     */
    public double beforeR = 0;
    public double afterR = 0;

    public double connectX = 0;
    public double connectY = 0;

    public CircleController circleController;

    public void setR(double r) {
        if (r < Config.MIN_CIRCLE_RADIUS) {
            r = Config.MIN_CIRCLE_RADIUS;
        }
        this.circle.setRadius(r);
    }

    public StackPane getCircle() {
        return this.stack;
    }

    public double getR() {
        return this.circle.getRadius();
    }

    public String getCenterTextStr() {
        return this.centerText.getText();
    }

    public CircleView(double x, double y, String centerTextStr, double r) {
        this.circleController = new CircleController(this);
        this.circle.setStroke(Color.BLACK);
        this.circle.setFill(Color.WHITE);
        this.setMouseEvent();
        this.centerText.setText(centerTextStr);
        centerText.setFont(Font.font(10));
        stack.getChildren().addAll(this.circle, this.centerText);
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

    public CircleView(double x, double y, String centerTextStr) {
        this(x, y, centerTextStr, Config.DEFAULT_CIRCLE_RADIUS);
    }

    public void setBros(List<CircleView> bros) {
        this.bros = bros;
    }

    /**
     * 设定选中状态
     * 
     * @param selected
     */
    public void selected() {
        if (!this.selected) {
            this.selected = true;
            this.circle.setFill(Color.web("#4e6ef2"));
            System.out.println("select de !!");
        }
    }

    public void unSelected() {
        if (this.selected) {
            this.selected = false;
            this.circle.setFill(Color.WHITE);
        }
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void toggleSelected() {
        if (this.selected) {
            this.unSelected();
        } else {
            this.selected();
        }
    }

    public void setMouseEvent() {
        this.circle.setOnMouseClicked(this.circleController.handleMouseClicked());
        this.circle.setOnMousePressed(this.circleController.handleMousePressed());
        this.circle.setOnDragDetected(this.circleController.handleDragDetected());
        this.circle.setOnMouseDragged(this.circleController.handleMouseDragged());
        this.circle.setOnMouseDragReleased(this.circleController.handleDragReleased());
    }

    public double[] moveC(double sX, double sY, double cX, double cY) {
        double xd = sX - cX;
        double yd = sY - cY;
        stack.setLayoutX(xd);
        stack.setLayoutY(yd);
        return new double[] { xd, yd };
    }

    public void saveR() {
        this.srcRadius = this.circle.getRadius();
    }

    public void saveLayoutLoc() {
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
