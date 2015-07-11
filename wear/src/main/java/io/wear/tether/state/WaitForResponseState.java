package io.wear.tether.state;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.nio.charset.StandardCharsets;

import io.wear.tether.BaseHotspotActivity;
import io.wear.tether.messages.ConfigurationModel;

import static io.wear.tether.messages.MessageConstants.RESULT_FAILURE;
import static io.wear.tether.messages.MessageConstants.RESULT_SUCCESS;

public class WaitForResponseState extends ChangeHotspotState implements MessageApi.MessageListener {
    @Override
    public void onStateApplied(BaseHotspotActivity stateContext) {
        super.onStateApplied(stateContext);
        Wearable.MessageApi.addListener(stateContext.googleApiClient, this);
    }

    @Override
    public void onStateLeaving(BaseHotspotActivity stateContext) {
        super.onStateLeaving(stateContext);
        Wearable.MessageApi.removeListener(stateContext.googleApiClient, this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(RESULT_SUCCESS)) {
            if (messageEvent.getData().length > 0) {
                stateContext.setState(new SuccessState(ConfigurationModel.fromBytes(messageEvent.getData())));
            }
            else {
                stateContext.setState(new SuccessState(null));
            }
        } else if (messageEvent.getPath().equals(RESULT_FAILURE)) {
            stateContext.setState(new FailureState());
        }
    }
}
