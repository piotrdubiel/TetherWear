package io.wear.tether.messages;

public interface MessageListener {
    void onUnknownError();
    void onConnectionError();
    void onMessageError();
    void onOK();
}
