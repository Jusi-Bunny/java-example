package com.example.i18n.enums;

public enum OrderStatus {

    PENDING("order.status.pending"),
    PAID("order.status.paid"),
    CANCELLED("order.status.cancelled");

    private final String messageKey;

    OrderStatus(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}