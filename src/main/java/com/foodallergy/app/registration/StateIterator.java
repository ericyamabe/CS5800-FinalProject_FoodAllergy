package com.foodallergy.app.registration;

import java.util.ArrayList;
import java.util.Iterator;

public class StateIterator implements Iterator {
    private int indexInCollection = 0;
    private int stateCollectionSize;
    private ArrayList<String> stateList;

    public StateIterator(States allStates) {
        this.stateList = allStates.getStates();
        this.stateCollectionSize = stateList.size();
    }

    @Override
    public boolean hasNext() {
        String state = null;
        if (indexInCollection < stateCollectionSize) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public String next() {
        if(this.hasNext()) {
            return stateList.get(indexInCollection++);
        }

        return null;
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }
}
