package io.wear.tether.state;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.nio.charset.StandardCharsets;

import io.wear.tether.BaseHotspotActivity;
import io.wear.tether.messages.ConfigurationModel;

import static io.wear.tether.messages.MessageConstants.RESULT_FAILURE;
import static io.wear.tether.messages.MessageConstants.RESULT_SUCCESS;
import static io.wear.tether.messages.MessageConstants.STATE_WIFI_TETHER_OFF;
import static io.wear.tether.messages.MessageConstants.STATE_WIFI_TETHER_ON;

public class WaitForResponseState extends ChangeHotspotState implements MessageApi.MessageListener {
    @Override
    public void onStateApplied(BaseHotspotActivity stateContext) {
        super.onStateApplied(stateContext);
        Wearable.MessageApi.addListener(stateContext.getGoogleApiClient(), this);
    }

    @Override
    public void onStateLeaving(BaseHotspotActivity stateContext) {
        super.onStateLeaving(stateContext);
        Wearable.MessageApi.removeListener(stateContext.getGoogleApiClient(), this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(STATE_WIFI_TETHER_ON)) {
            if (messageEvent.getData().length > 0) {
                stateContext.setState(new SuccessState(ConfigurationModel.fromBytes(messageEvent.getData())));
            }
        } else if (messageEvent.getPath().equals(STATE_WIFI_TETHER_OFF)) {
            stateContext.setState(new SuccessState(null));
        } else if (messageEvent.getPath().equals(RESULT_FAILURE)) {
            stateContext.setState(new FailureState());
        }
    }
}
