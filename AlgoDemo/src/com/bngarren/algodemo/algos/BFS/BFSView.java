package com.bngarren.algodemo.algos.BFS;

import com.bngarren.algodemo.GridAlgoView;

public class BFSView extends GridAlgoView {

    private final static int GRID_SIZE = 10;

    public BFSView() {
        super(GRID_SIZE);
    }


    @Override
    protected String getDescription() {
        return "Breadth-First Search algorithm";
    }

    @Override
    public String getTitle() {
        return "BFS";
    }


}
