package com.patulus.taffystore.server;

public enum ServerState {
    RUNNING("RUNNING"), IDLE("IDLE"), REST("REST");

    private final String value;

    ServerState(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
