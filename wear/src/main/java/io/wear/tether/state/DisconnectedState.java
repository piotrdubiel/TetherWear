package io.wear.tether.state;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import io.wear.tether.BaseHotspotActivity;

public class DisconnectedState extends ChangeHotspotState implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onStateApplied(BaseHotspotActivity stateContext) {
        super.onStateApplied(stateContext);

        stateContext.setGoogleApiClient(new GoogleApiClient.Builder(stateContext)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build());

        stateContext.getGoogleApiClient().connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        stateContext.setState(new RequestState());
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