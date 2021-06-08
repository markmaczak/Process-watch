package com.codecool.processwatch.gui;

import javafx.collections.ObservableList;

import com.codecool.processwatch.domain.ProcessWatchApp;
import com.codecool.processwatch.os.OsProcessSource;
import com.codecool.processwatch.queries.SelectAll;

public class App extends ProcessWatchApp {

    public App(ObservableList<ProcessView> displayList) {
        super(new OsProcessSource(), new GuiProcessDisplay(displayList), new SelectAll());
    }
}
