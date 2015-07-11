package io.wear.tether.messages;

import java.nio.charset.StandardCharsets;

public class ConfigurationModel {
    private String ssid;

    private String password;
    public ConfigurationModel(String ssid, String password) {
        this.ssid = ssid;
        this.password = password;
    }

    public byte[] getBytes() {
        byte[] ssidBytes = ssid.getBytes(StandardCharsets.UTF_8);
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[ssidBytes.length + passwordBytes.length + 2];

        data[0] = (byte) ssidBytes.length;
        data[1] = (byte) passwordBytes.length;
        System.arraycopy(ssidBytes, 0, data, 2, ssidBytes.length);
        System.arraycopy(passwordBytes, 0, data, 2 + ssidBytes.length, passwordBytes.length);

        return data;
    }

    public static ConfigurationModel fromBytes(byte[] data) {
        byte[] ssidBytes = new byte[data[0]];
        byte[] passwordBytes = new byte[data[1]];

        System.arraycopy(data, 2, ssidBytes, 0, ssidBytes.length);
        System.arraycopy(data, 2 + ssidBytes.length, passwordBytes, 0, passwordBytes.length);

        return new ConfigurationModel(
                new String(ssidBytes, StandardCharsets.UTF_8),
                new String(passwordBytes, StandardCharsets.UTF_8)
        );
    }

    public String getSsid() {
        return ssid;
    }

    public String getPassword() {
        return password;
    }
}
