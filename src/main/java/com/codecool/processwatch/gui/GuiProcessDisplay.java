package com.codecool.processwatch.gui;

import javafx.collections.ObservableList;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.ProcessDisplay;

public class GuiProcessDisplay extends ProcessDisplay  {
    private final ObservableList<ProcessView> displayList;

    public GuiProcessDisplay(ObservableList<ProcessView> displayList) {
        this.displayList = displayList;
    }

    @Override
    protected void clear() {
        displayList.clear();
    }

    @Override
    protected void addProcess(Process proc) {
        displayList.add(new ProcessView(proc));
    }
}
