package io.wear.tether.state;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import io.wear.tether.WifiTetherActivity;

public class DisconnectedState extends WifiTetherActivityState implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onStateApplied(WifiTetherActivity stateContext) {
        super.onStateApplied(stateContext);

        stateContext.googleApiClient = new GoogleApiClient.Builder(stateContext)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        stateContext.googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        stateContext.setState(new RequestTetheringState());
    }

    @Override
    public void onConnectionSuspended(int i) {
        stateContext.setState(new ConnectionFailureState());
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        stateContext.setState(new ConnectionFailureState());
    }
}