package org.openjfx;

import java.util.ArrayList;
import java.util.List;

public class InteractionModel {

    private int index;
    private List<TrialRecord> trialRecordList;

    public void setTime(){
        if(index < trialRecordList.size()){
            trialRecordList.get(index).setStartTime(System.currentTimeMillis());
        }
        trialRecordList.get(index - 1).setEndTime(System.currentTimeMillis());
    }

    public boolean hasNext(){
        return trialRecordList!= null && trialRecordList.size() > index;
    }
    public TrialRecord next(){
        return this.trialRecordList.get(index++);
    }

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

    public List<TrialRecord> getTrialRecordList() {
        return trialRecordList;
    }

    public void setTrialRecordList(List<TrialRecord> trialRecordList) {
        this.trialRecordList = trialRecordList;
        this.index = 0;
        Global.isTraining = true;
    }

    

}
