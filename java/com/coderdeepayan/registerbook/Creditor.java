package com.coderdeepayan.registerbook;

public class Creditor {
    private String name,id;
    private boolean selected;

    public Creditor(String name, String id, boolean selected) {
        this.name = name;
        this.id = id;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }
}
