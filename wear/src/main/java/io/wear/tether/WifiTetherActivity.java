package io.wear.tether;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

import io.wear.tether.state.DisconnectedState;
import io.wear.tether.state.State;
import io.wear.tether.state.StateMachine;
import io.wear.tether.state.Stateable;
import io.wear.tether.state.WifiTetherActivityState;

import static io.wear.tether.messages.MessageConstants.*;

public class WifiTetherActivity extends Activity implements Stateable<WifiTetherActivityState> {
    private static final int REQUEST_RESOLVE_ERROR = 1000;

    public GoogleApiClient googleApiClient;
    StateMachine<WifiTetherActivityState> stateMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        stateMachine = new StateMachine<WifiTetherActivityState>(this);
    }

    @Override
    public void setState(WifiTetherActivityState state) {
        stateMachine.setState(state);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setState(new DisconnectedState());
    }
//
//    @Override
//    protected void onStop() {
//        if (!resolvingError) {
//            Wearable.MessageApi.removeListener(googleApiClient, this);
//            googleApiClient.disconnect();
//        }
//        super.onStop();
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.d("A", "Google API Client was connected");
//        resolvingError = false;
//        Wearable.MessageApi.addListener(googleApiClient, this);
//        new SendMessageTask(REQUEST_WIFI_TETHER_ON).execute();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onMessageReceived(MessageEvent messageEvent) {
//        if (messageEvent.getPath().equals(RESULT_SUCCESS)) {
//            showSuccess(getResources().getString(R.string.lb_wifi_tether_on));
//        } else if (messageEvent.getPath().equals(RESULT_FAILURE)) {
//            showFailure(getResources().getString(R.string.lb_wifi_tether_fail));
//        }
//    }
//
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

//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        if (resolvingError) {
//            return;
//        } else if (result.hasResolution()) {
//            try {
//                resolvingError = true;
//                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
//            } catch (IntentSender.SendIntentException e) {
//                // There was an error with the resolution intent. Try again.
//                googleApiClient.connect();
//            }
//        } else {
//            showFailure(getResources().getString(R.string.lb_connection_fail));
//            resolvingError = false;
//            Wearable.MessageApi.removeListener(googleApiClient, this);
//        }
//    }
//
//    private class SendMessageTask extends AsyncTask<Void, Void, Void> {
//        private String path;
//
//        private SendMessageTask(String path) {
//            this.path = path;
//        }
//
//        @Override
//        protected Void doInBackground(Void... args) {
//            Collection<String> nodes = getNodes();
//            for (String node : nodes) {
//                sendMessage(node, path);
//            }
//            return null;
//        }
//    }
//
//    private Collection<String> getNodes() {
//        HashSet<String> results = new HashSet<String>();
//        NodeApi.GetConnectedNodesResult nodes =
//                Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
//
//        for (Node node : nodes.getNodes()) {
//            results.add(node.getId());
//        }
//
//        return results;
//    }
//
//    private void sendMessage(String node, String path) {
//        Wearable.MessageApi.sendMessage(googleApiClient, node, path, new byte[0])
//                .setResultCallback(this);
//    }
//
//    @Override
//    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
//        if (!sendMessageResult.getStatus().isSuccess()) {
//            showFailure(getResources().getString(R.string.lb_message_fail));
//        }
//    }
//

}
