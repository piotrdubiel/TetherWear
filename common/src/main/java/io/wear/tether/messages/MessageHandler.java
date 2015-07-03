package io.wear.tether.messages;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageApi.MessageListener;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

public class MessageHandler implements GoogleApiClient.ConnectionCallbacks, ResultCallback<MessageApi.SendMessageResult> {

    private final GoogleApiClient mGoogleApiClient;
    private final MessageListener listener;
    private final Context context;

    public MessageHandler(Context context, MessageListener listener) {
        this.context = context;
        this.listener = listener;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
    }

    public void sendMessageToNode(String node, String message, byte[] data) {
        new SendMessageTask(node, message, data).execute();
    }

    public void sendMessageToAllNodes(String path, byte[] data) {
        new SendToAllMessageTask(path, data).execute();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mGoogleApiClient, listener);
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void pause() {
        Wearable.MessageApi.removeListener(mGoogleApiClient, listener);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }

        return results;
    }

    @Override
    public void onResult(MessageApi.SendMessageResult sendMessageResult) {

    }

    private class SendToAllMessageTask extends AsyncTask<Void, Void, Void> {

        private final String path;
        private final byte[] data;

        private SendToAllMessageTask( String path, byte[] data) {
            this.path = path;
            this.data = data;
        }

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendMessageToNode(node, path, data);
            }
            return  null;
        }
    }


    private class SendMessageTask extends AsyncTask<Void, Void, Void> {

        private final String node;
        private final String path;
        private final byte[] data;

        private SendMessageTask(String node, String path, byte[] data) {
            this.node = node;
            this.path = path;
            this.data = data;
        }

        @Override
        protected Void doInBackground(Void... args) {
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, node, path, data).setResultCallback(MessageHandler.this);
            return null;
        }
    }
}
