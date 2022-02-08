package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class HandTest {

    @Test
    public void handWithValueOf21IsNotBusted() throws Exception {
        Hand hand = Hand.createForTest(List.of(new Card(Suit.HEARTS, "10"),
                                               new Card(Suit.HEARTS, "8"),
                                               new Card(Suit.HEARTS, "3")));

        assertThat(hand.isBusted())
                .isFalse();
    }

    @Test
    public void handWithValueOf22IsBusted() throws Exception {
        Hand hand = Hand.createForTest(List.of(new Card(Suit.HEARTS, "10"),
                                               new Card(Suit.HEARTS, "8"),
                                               new Card(Suit.HEARTS, "4")));

        assertThat(hand.isBusted())
                .isTrue();
    }
}
