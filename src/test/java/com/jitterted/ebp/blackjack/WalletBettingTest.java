package com.jitterted.ebp.blackjack;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WalletBettingTest {

    @Test
    public void walletWithBalance12WhenBet8ThenBalanceIs4() throws Exception {
        Wallet wallet = createWalletWithInitialAmountOf(12);

        wallet.bet(8);

        assertThat(wallet.balance())
                .isEqualTo(12 - 8);
    }

    @Test
    public void walletWith27Bet7AndBet9ThenBalanceIs11() throws Exception {
        Wallet wallet = createWalletWithInitialAmountOf(27);

        wallet.bet(7);
        wallet.bet(9);

        assertThat(wallet.balance())
                .isEqualTo(27 - 7 - 9);
    }

    @Test
    public void betFullBalanceThenWalletIsEmpty() throws Exception {
        Wallet wallet = createWalletWithInitialAmountOf(73);

        wallet.bet(73);

        assertThat(wallet.isEmpty())
                .isTrue();
    }

    @Test
    public void betMoreThanBalanceThrowsException() {
        Wallet wallet = createWalletWithInitialAmountOf(10);

        assertThatThrownBy(() -> {
            wallet.bet(11);
        }).isInstanceOf(IllegalStateException.class);
    }

    private Wallet createWalletWithInitialAmountOf(int amount) {
        Wallet wallet = new Wallet();
        wallet.addMoney(amount);
        return wallet;
    }

    // wallet.canBet(11)
    // wallet.balance() >= 11

}



