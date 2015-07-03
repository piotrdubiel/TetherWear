package io.state.machine;

public interface Stateful<STATE extends State> {
    void setState(STATE newState);
}
