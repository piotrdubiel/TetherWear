package io.wear.tether.state;

import io.wear.tether.BaseHotspotActivity;
import io.wear.tether.R;

public class ConnectionFailureState extends ChangeHotspotState {
    @Override
    public void onStateApplied(BaseHotspotActivity stateContext) {
        super.onStateApplied(stateContext);
        stateContext.showFailure(stateContext.getResources().getString(R.string.lb_connection_fail));
        stateContext.finish();
    }
}
