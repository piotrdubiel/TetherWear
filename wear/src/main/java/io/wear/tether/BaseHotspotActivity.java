package io.wear.tether;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.WindowManager;

import com.google.android.gms.common.api.GoogleApiClient;

import io.wear.tether.messages.ConfigurationModel;
import io.wear.tether.state.ChangeHotspotState;
import io.state.machine.StateMachine;
import io.state.machine.Stateful;

public abstract class BaseHotspotActivity extends Activity implements Stateful<ChangeHotspotState> {
    public GoogleApiClient googleApiClient;
    protected StateMachine<ChangeHotspotState> stateMachine;
    private String requestToSend;

    public BaseHotspotActivity(String requestToSend) {
        this.requestToSend = requestToSend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        stateMachine = new StateMachine<>(this);
    }


    @Override
    public void setState(ChangeHotspotState state) {
        stateMachine.setState(state);
    }

    public void showFailure(String message) {
        showConfirmation(ConfirmationActivity.FAILURE_ANIMATION, message);
    }

    public void showSuccess(String message) {
        showConfirmation(ConfirmationActivity.SUCCESS_ANIMATION, message);
    }

    private void showConfirmation(int type, String message) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, type);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public String getRequestToSend() {
        return requestToSend;
    }

    public abstract void onSuccess(ConfigurationModel ssid);
}
