package io.wear.tether.messages;

public class Message {
    private String node;
    private String path;
    private byte[] data;

    public Message(String node, String path, byte[] data) {
        this.node = node;
        this.path = path;
        this.data = data;
    }

    public Message(String node, String path) {
        this.node = node;
        this.path = path;
        data = new byte[0];
    }
}
