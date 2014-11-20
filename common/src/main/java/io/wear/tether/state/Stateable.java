package io.wear.tether.state;

public interface Stateable<STATE extends State> {
    void setState(STATE newState);
}
