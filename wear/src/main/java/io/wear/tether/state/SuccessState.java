package io.wear.tether.state;

import io.wear.tether.BaseHotspotActivity;

public class SuccessState extends ChangeHotspotState {
    @Override
    public void onStateApplied(BaseHotspotActivity stateContext) {
        super.onStateApplied(stateContext);
        stateContext.onSuccess();
        stateContext.finish();
    }
}
