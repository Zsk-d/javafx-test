package org.openjfx;

import java.util.List;

public class ActionGroup {
    private String type;
    private List<ActionItem> items;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ActionItem> getItems() {
        return items;
    }

    public void setItems(List<ActionItem> items) {
        this.items = items;
    }

}
