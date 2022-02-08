package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WalletTest {

    @Test
    public void newWalletIsEmpty() throws Exception {
        // GIVEN a wallet, WHEN it's NEW
        Wallet wallet = new Wallet();

        // THEN it is EMPTY
        assertThat(wallet.isEmpty())
                .isTrue();
    }

    @Test
    public void newWalletAddMoneyThenIsNotEmpty() throws Exception {
        Wallet wallet = new Wallet();

        wallet.addMoney(1);

        assertThat(wallet.isEmpty())
                .isFalse();
    }

}


