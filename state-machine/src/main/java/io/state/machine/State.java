package io.state.machine;

import android.content.Context;
import android.content.Intent;

public class State<CONTEXT extends Context> {
    protected CONTEXT stateContext;
    public void onStateLeaving(CONTEXT stateContext) {
        this.stateContext = stateContext;
    }
    public void onStateApplied(CONTEXT stateContext) {
        this.stateContext = stateContext;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}