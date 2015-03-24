package io.state.machine;

import android.content.Context;
import android.util.Log;

public class StateMachine<STATE extends State> implements Stateable<STATE> {
    private static final String TAG = "StateMachine";
    protected STATE currentState;
    protected STATE previousState;

    private Context context;

    public StateMachine(Context context) {
        this.context = context;
    }

    @Override
    public void setState(STATE newState) {
        Log.d(TAG, "Changing state from " + currentState + " to " + newState);
        this.previousState = this.currentState;
        if (previousState != null) {
            this.previousState.onStateLeaving(context);
        }
        this.currentState = newState;
        this.currentState.onStateApplied(context);
    }

    public STATE currentState() {
        return this.currentState;
    }

    public STATE getPreviousState() {
        return this.previousState;
    }
}
