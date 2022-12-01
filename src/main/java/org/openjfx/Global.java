package org.openjfx;

import java.util.ArrayList;
import java.util.List;

public class Global {

    /**
     * 按键按下抬起状态
     */
    public static boolean isShiftPressed = false;
    public static boolean isAltPressed = false;
    public static boolean isCtrlPressed = false;

    public static boolean isChangeCirR = false;
    public static boolean isCirDrag = false;

    public static int cNum = 1;

    /**
     * 鼠标点击时的坐标
     */
    public static boolean isMousePressed = false;
    public static double mousePressedX = -1;
    public static double mousePressedY = -1;
    public static double mouseNowX = -1;
    public static double mouseNowY = -1;
    public static double mouseReleasedX = -1;
    public static double mouseReleasedY = -1;

    /**
     * 所有圆形panel集合
     */
    public final static List<CirclePanel> cl = new ArrayList<>();

}