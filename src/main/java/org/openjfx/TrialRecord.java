package org.openjfx;

public class TrialRecord {
    private long startTime;
    private long endTime;
    private double r;
    private double x;
    private double y;
    private int index;

    public long getTimeRange(){
        return endTime - startTime;
    }
    
    @Override
    public String toString() {
        return "TrialRecord [r=" + r + ", x=" + x + ", y=" + y + ", index=" + index + ",time range=" + getTimeRange() + "]";
    }
    public TrialRecord(double r, double x, double y, int index) {
        this.r = r;
        this.x = x;
        this.y = y;
        this.index = index;
    }
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getEndTime() {
        return endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public double getR() {
        return r;
    }
    public void setR(double r) {
        this.r = r;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    
}
