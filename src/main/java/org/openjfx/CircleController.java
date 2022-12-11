package org.openjfx;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CircleController {
    private CircleView view;

    public CircleController(CircleView view) {
        this.view = view;
    }

    public EventHandler<MouseEvent> handleMouseClicked() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // 切换选定
                if (!Global.isCirDrag) {
                    selected();
                }
                Global.isCirDrag = false;
            }
        };
    }

    public EventHandler<MouseEvent> handleMousePressed() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.cX = event.getX() + view.circle.getRadius();
                view.cY = event.getY() + view.circle.getRadius();

                view.pX = event.getX();
                view.beforeMoveLayoutX = view.stack.getLayoutX();
                view.beforeMoveLayoutX = view.stack.getLayoutX();
            }
        };
    }

    public EventHandler<MouseEvent> handleDragDetected() {
        return (MouseEvent event) -> {
            view.circle.startFullDrag();
            Global.isCirDrag = true;
            // 插入其他已选择的图形
            List<CircleView> cps = null;
            for (CircleView item : Global.cl) {
                if (!item.equals(view) && item.isSelected()) {
                    if (cps == null) {
                        cps = new ArrayList<>();
                    }
                    cps.add(item);
                    item.setConnectX(view.stack.getLayoutX() - item.getCircle().getLayoutX());
                    item.setConnectY(view.stack.getLayoutY() - item.getCircle().getLayoutY());
                    // 保存初始位置
                    item.beforeMoveLayoutX = item.getCircle().getLayoutX();
                    item.beforeMoveLayoutY = item.getCircle().getLayoutY();
                    // 保存初始半径
                    item.beforeR = item.getR();
                }
            }
            // 保存初始位置
            view.beforeMoveLayoutX = view.stack.getLayoutX();
            view.beforeMoveLayoutY = view.stack.getLayoutY();
            // 保存初始半径
            view.beforeR = view.getR();
            System.out.println(String.format("同时控制: %s", cps));
            this.view.setBros(cps);
        };
    }

    public EventHandler<MouseEvent> handleMouseDragged() {
        return new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // 判断SHIFT是否按下, 决定调整大小还是移动位置
                if (!event.getTarget().equals(view.circle)) {
                    return;
                }
                if (Global.isShiftPressed) {
                    double newR = view.srcRadius + (event.getX() - view.pX) / 2;
                    // System.out.println("newR = " + newR + ", px = " + pX + ", event x = " +
                    // event.getX());
                    if (newR <= Config.MIN_CIRCLE_RADIUS) {
                        newR = Config.MIN_CIRCLE_RADIUS;
                    }
                    view.circle.setRadius(newR);
                    Global.isChangeCirR = true;
                    final double tt = newR;
                    // 同步缩放
                    if (view.bros != null) {
                        view.bros.forEach(item -> {
                            item.setR(item.getR() + tt - view.srcRadius);
                            item.saveR();
                        });
                    }
                    view.saveR();
                } else {
                    double ccX = event.getSceneX();
                    double ccY = event.getSceneY();
                    double[] moveRes = view.moveC(ccX, ccY, view.cX, view.cY);
                    // 同步移动
                    if (view.bros != null) {
                        view.bros.forEach(item -> {
                            item.getCircle().setLayoutX(moveRes[0] - item.getConnectX());
                            item.getCircle().setLayoutY(moveRes[1] - item.getConnectY());
                        });
                    }
                }
            };
        };
    }

    public EventHandler<MouseDragEvent> handleDragReleased() {
        return new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                view.afterMoveLayoutX = view.stack.getLayoutX();
                view.afterMoveLayoutY = view.stack.getLayoutY();
                view.afterR = view.getR();
                // 判断是否为移动
                boolean isMove = view.afterMoveLayoutX != view.beforeMoveLayoutX
                        || view.afterMoveLayoutY != view.beforeMoveLayoutY;
                if (isMove) {
                    System.out.println(
                            String.format("移动 bx = %.2f, by = %.2f, ax = %.2f, ay = %.2f", view.beforeMoveLayoutX,
                                    view.beforeMoveLayoutY, view.afterMoveLayoutX, view.afterMoveLayoutY));
                }
                // 判断是否为变更大小
                boolean isChangeR = view.afterR != view.beforeR;
                if (isChangeR) {
                    System.out.println(String.format("改变大小 原半径 = %.2f, 新半径 = %.2f", view.beforeR, view.afterR));
                }
                // 保存撤销
                ActionGroup actionGroup = null;
                List<ActionItem> items = null;
                if (isMove) {
                    actionGroup = new ActionGroup();
                    items = new ArrayList<>();
                    actionGroup.setType(Config.actionTypeMove);
                    ActionItem actionItem = new ActionItem();
                    actionItem.setBeforeMoveLayoutX(view.getBeforeMoveLayoutX());
                    actionItem.setBeforeMoveLayoutY(view.getBeforeMoveLayoutY());
                    actionItem.setAfterMoveLayoutX(view.getAfterMoveLayoutX());
                    actionItem.setAfterMoveLayoutY(view.getAfterMoveLayoutY());
                    actionItem.setItem(view);
                    items.add(actionItem);
                }
                if (isChangeR) {
                    actionGroup = new ActionGroup();
                    items = new ArrayList<>();
                    actionGroup.setType(Config.actionTypeChangeR);
                    ActionItem actionItem = new ActionItem();
                    actionItem.setBeforeR(view.beforeR);
                    actionItem.setAfterR(view.afterR);
                    actionItem.setItem(view);
                    items.add(actionItem);
                }
                if (view.bros != null) {
                    for (CircleView item : view.bros) {
                        item.saveR();
                        // 保存新位置
                        item.afterMoveLayoutX = item.getCircle().getLayoutX();
                        item.afterMoveLayoutY = item.getCircle().getLayoutY();
                        // 保存新半径
                        item.afterR = item.getR();
                        if (isMove) {
                            // 保存撤销
                            ActionItem groupActionItem = new ActionItem();
                            groupActionItem.setItem(item);
                            groupActionItem.setBeforeMoveLayoutX(item.getBeforeMoveLayoutX());
                            groupActionItem.setBeforeMoveLayoutY(item.getBeforeMoveLayoutY());
                            groupActionItem.setAfterMoveLayoutX(item.getAfterMoveLayoutX());
                            groupActionItem.setAfterMoveLayoutY(item.getAfterMoveLayoutY());
                            items.add(groupActionItem);
                        }
                        if (isChangeR) {
                            ActionItem groupActionItem = new ActionItem();
                            groupActionItem.setBeforeR(item.getBeforeR());
                            groupActionItem.setAfterR(item.getAfterR());
                            groupActionItem.setItem(item);
                            items.add(groupActionItem);
                        }
                    }
                    view.bros = null;
                }
                if (isMove || isChangeR) {
                    actionGroup.setItems(items);
                    Global.addUndo(actionGroup);
                }
                view.saveR();
                view.saveLayoutLoc();
                Global.isChangeCirR = false;
            }
        };
    }

    public void selected() {
        if (!this.view.selected) {
            this.view.selected = true;
            this.view.circle.setFill(Color.web("#4e6ef2"));
        }
    }
}
