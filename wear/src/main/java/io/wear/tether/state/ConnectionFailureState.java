package io.wear.tether.state;

import io.wear.tether.R;
import io.wear.tether.WifiTetherActivity;

public class ConnectionFailureState extends WifiTetherActivityState {
    @Override
    public void onStateApplied(WifiTetherActivity stateContext) {
        super.onStateApplied(stateContext);
        stateContext.showFailure(stateContext.getResources().getString(R.string.lb_connection_fail));
        stateContext.finish();
    }
}
