package io.wear.tether.state;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

import io.wear.tether.BaseHotspotActivity;

public class RequestState extends ChangeHotspotState implements ResultCallback<MessageApi.SendMessageResult> {
    @Override
    public void onStateApplied(BaseHotspotActivity stateContext) {
        super.onStateApplied(stateContext);
        new RequestTetheringTask().execute();
    }

    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(stateContext.googleApiClient).await();

        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }

        return results;
    }

    @Override
    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
        if (sendMessageResult.getStatus().isSuccess()) {
            stateContext.setState(new WaitForResponseState());
        } else {
            stateContext.setState(new FailureState());
        }
    }

    private class RequestTetheringTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            Log.d("S", "getNodes " + nodes.size());

            if (nodes.isEmpty()) {
                stateContext.setState(new ConnectionFailureState());
            }
            for (String node : nodes) {
                Log.d("Message", "To: " + node);
                Wearable.MessageApi.sendMessage(
                        stateContext.googleApiClient,
                        node,
                        stateContext.getRequestToSend(),
                        new byte[0]).setResultCallback(RequestState.this);
            }
            return null;
        }
    }
}
