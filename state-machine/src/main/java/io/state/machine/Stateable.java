package io.state.machine;

public interface Stateable<STATE extends State> {
    void setState(STATE newState);
}
