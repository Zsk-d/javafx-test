package org.openjfx;

import java.util.ArrayList;
import java.util.List;

public class InteractionModel {
    public List<CircleView> getSelectedPanel() {
        List<CircleView> circlePanels = null;
        for (CircleView item : Global.cl) {
            if (item.isSelected()) {
                if (circlePanels == null) {
                    circlePanels = new ArrayList<>();
                }
                circlePanels.add(item);
            }
        }
        return circlePanels;
    }

    public void unSelectAll() {
        List<CircleView> selectedPanel = getSelectedPanel();
        if(selectedPanel != null){
            selectedPanel.forEach(CircleView::unSelected);
        }
    }

}
