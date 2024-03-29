package org.openjfx;

public class Config {
    public static final double DEFAULT_CIRCLE_RADIUS = 40;
    public static final double MIN_CIRCLE_RADIUS = 5;

    /**
     * 按键状态显示
     */
    public static final String KEY_STATUS_TEXT_TMP = "KEY PRESSED (SHIFT: %s, CTRL: %s, ALT:%s)";

    /**
     * 鼠标状态显示
     */
    public static final String MOUSE_STATUS_TEXT_TMP = "MOUSE (IS_PRESSED: %s, PRESSED_X: %.2f, PRESSED_Y:%.2f, RELEASED_X :%.2f, RELEASED :%.2f, NOW_X: %.2f, NOW_Y :%.2f)";

    public static final int STAGE_WIDTH = 800;
    public static final int STAGE_HEIGHT = 800;

    public static final int PASTE_XY_OFFSET = 10;

    public static final String actionTypeNew = "New";
    public static final String actionTypeDelete = "Delete";
    public static final String actionTypeMove = "Move";
    public static final String actionTypeChangeR = "ChangeR";

}
