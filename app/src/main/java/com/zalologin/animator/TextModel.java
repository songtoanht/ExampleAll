package com.zalologin.animator;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/21/2017.
 */

public class TextModel {
    private String name;
    private boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TextModel copy() {
        TextModel textModel = new TextModel();
        textModel.setName(name);
        textModel.setSelected(selected);
        return textModel;
    }
}
