package com.patulus.taffystore.customer;

public enum CustomerType {
    CUST_NORMAL("NORMAL"), CUST_VIP("VIP");

    private final String value;

    CustomerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
