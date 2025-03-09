package com.github.renas.gamification;

public enum Rewards {
    TASK_COMPLETED(50),
    CHATBOT_USED(20);

    private final int xpValue;

    Rewards(int xpValue) {
        this.xpValue = xpValue;
    }

    public int getXpValue() {
        return xpValue;
    }
}
