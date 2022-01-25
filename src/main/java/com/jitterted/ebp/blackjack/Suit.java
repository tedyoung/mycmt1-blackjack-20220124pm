package com.jitterted.ebp.blackjack;

import java.util.List;

public enum Suit {
    SPADES("♠"),
    DIAMONDS("♦"),
    HEARTS("♥"),
    CLUBS("♣");

    @Deprecated
    static final List<String> SUITS = List.of("♠", "♦", "♥", "♣");
    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    // SCAFFOLDING: temporary
    @Deprecated
    public static Suit from(String symbol) {
        for (Suit suit : values()) {
            if (suit.symbol().equals(symbol)) {
                return suit;
            }
        }
        return null;
    }

    public String symbol() {
        return symbol;
    }
}
