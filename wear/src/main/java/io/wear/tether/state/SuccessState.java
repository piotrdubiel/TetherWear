package io.wear.tether.state;

import io.wear.tether.BaseHotspotActivity;
import io.wear.tether.messages.ConfigurationModel;

public class SuccessState extends ChangeHotspotState {
    private final ConfigurationModel config;

    public SuccessState(ConfigurationModel config) {
        this.config = config;
    }

    @Override
    public void onStateApplied(BaseHotspotActivity stateContext) {
        super.onStateApplied(stateContext);
        stateContext.onSuccess(config);
        stateContext.finish();
    }
}
