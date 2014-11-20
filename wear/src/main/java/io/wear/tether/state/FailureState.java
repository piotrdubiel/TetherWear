package io.wear.tether.state;

import io.wear.tether.R;
import io.wear.tether.WifiTetherActivity;

public class FailureState extends WifiTetherActivityState {
    @Override
    public void onStateApplied(WifiTetherActivity stateContext) {
        super.onStateApplied(stateContext);
        stateContext.showFailure(stateContext.getResources().getString(R.string.lb_message_fail));
        stateContext.finish();
    }
}
