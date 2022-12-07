package org.openjfx;

public class ActionItem {
    private String itemNum;
    private double layoutX;
    private double layoutY;
    private double r;
    private CirclePanel item;
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
    
    public String getItemNum() {
        return itemNum;
    }
    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }
    public double getLayoutX() {
        return layoutX;
    }
    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }
    public double getLayoutY() {
        return layoutY;
    }
    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }
    public double getR() {
        return r;
    }
    public void setR(double r) {
        this.r = r;
    }
    public CirclePanel getItem() {
        return item;
    }
    public void setItem(CirclePanel item) {
        this.item = item;
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

    
}
