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

    /**
     * 撤销/重做 列表
     */
    private final static List<ActionGroup> UNDO_GROUPS = new ArrayList<>();

    /**
     * 剪贴板
     */
    private static ActionGroup COPY_GROUP = null;
    
    public static void addUndo(ActionGroup actionGroup){
        UNDO_GROUPS.add(actionGroup);
        if(UNDO_GROUPS.size() >  10){
            UNDO_GROUPS.remove(0);
        }
    }

    private ActionGroup getUndo(){
        if(UNDO_GROUPS.size() > 0){
            return UNDO_GROUPS.remove(UNDO_GROUPS.size() - 1);
        }
        return null;
    }

    public static void setCopy(ActionGroup actionGroup){
        COPY_GROUP = actionGroup;
    }

    public static ActionGroup getCopy(){
        COPY_GROUP.getItems().forEach(item->{
            item.setLayoutX(item.getLayoutX() + 5);
            item.setLayoutY(item.getLayoutY() + 5);
        });
        return COPY_GROUP;
    }

}