package com.jitterted.ebp.blackjack;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.List;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

    private final Deck deck;

    private final Hand dealerHand = new Hand();
    private final Hand playerHand = new Hand();
    private int playerBalance = 0;
    private int playerBetAmount;

    public static void main(String[] args) {
        displayWelcomeScreen();
        playGame();
        resetScreen();
    }

    private static void playGame() {
        Game game = new Game();
        game.initialDeal();
        game.play();
    }

    private static void resetScreen() {
        System.out.println(ansi().reset());
    }

    private static void displayWelcomeScreen() {
        AnsiConsole.systemInstall();
        System.out.println(ansi()
                                   .bgBright(Ansi.Color.WHITE)
                                   .eraseScreen()
                                   .cursor(1, 1)
                                   .fgGreen().a("Welcome to")
                                   .fgRed().a(" JitterTed's")
                                   .fgBlack().a(" BlackJack game"));
        System.out.println(ansi()
                                   .cursor(3, 1)
                                   .fgBrightBlack().a("Hit [ENTER] to start..."));

        System.console().readLine();
    }

    public Game() {
        deck = new Deck();
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
    }

    private void dealRoundOfCards() {
        // players first: that's the rule of dealing in Blackjack
        playerHand.drawCard(deck);
        dealerHand.drawCard(deck);
    }

    public void play() {
        boolean playerBusted = playerTurn();

        dealerTurn(playerBusted);

        displayFinalGameState();

        GameOutcome gameOutcome = determineOutcome(playerBusted);
        playerBalance += gameOutcome.payoffAmount(playerBetAmount);
    }

    private GameOutcome determineOutcome(boolean playerBusted) {
        if (playerBusted) {
            System.out.println("You Busted, so you lose.  ????");
            return GameOutcome.PLAYER_LOSES;
        } else if (dealerHand.isBusted()) {
            System.out.println("Dealer went BUST, Player wins! Yay for you!! ????");
            return GameOutcome.PLAYER_WINS;
        } else if (playerHand.beats(dealerHand)) {
            System.out.println("You beat the Dealer! ????");
            return GameOutcome.PLAYER_WINS;
        } else if (dealerHand.pushes(playerHand)) {
            System.out.println("Push: You tie with the Dealer. ????");
            return GameOutcome.PLAYER_PUSHES;
        } else {
            System.out.println("You lost to the Dealer. ????");
            return GameOutcome.PLAYER_LOSES;
        }
    }

    private boolean playerTurn() {
        // get Player's decision: hit until they stand, then they're done (or they go bust)
        boolean playerBusted = false;
        while (!playerBusted) {
            displayGameState();
            String playerChoice = inputFromPlayer().toLowerCase();
            if (playerStands(playerChoice)) {
                break;
            }
            if (playerHits(playerChoice)) {
                playerHand.drawCard(deck);
                if (playerHand.isBusted()) {
                    playerBusted = true;
                }
            } else {
                System.out.println("You need to [H]it or [S]tand");
            }
        }
        return playerBusted;
    }

    private boolean playerHits(String playerChoice) {
        return playerChoice.startsWith("h");
    }

    private boolean playerStands(String playerChoice) {
        return playerChoice.startsWith("s");
    }

    private void dealerTurn(boolean playerBusted) {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>=stand)
        if (!playerBusted) {
            while (dealerHand.shouldDealerHit()) {
                dealerHand.drawCard(deck);
            }
        }
    }

    @Deprecated
    public int handValueOf(List<Card> hand) {
        int handValue = hand
                .stream()
                .mapToInt(Card::rankValue)
                .sum();

        // does the hand contain at least 1 Ace?
        boolean hasAce = hand
                .stream()
                .anyMatch(card -> card.rankValue() == 1);

        // if the total hand value <= 11, then count the Ace as 11 by adding 10
        if (hasAce && handValue < 11) {
            handValue += 10;
        }

        return handValue;
    }

    private String inputFromPlayer() {
        System.out.println("[H]it or [S]tand?");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void displayGameState() {
        eraseScreen();
        System.out.println("Dealer has: ");
        System.out.println(dealerHand.faceUpCard().display()); // first card is Face Up

        // second card is the hole card, which is hidden
        displayBackOfCard();

        displayPlayerHand();
    }

    private void displayPlayerHand() {
        System.out.println();
        System.out.println("Player has: ");
        playerHand.display();
        System.out.println(" (" + playerHand.value() + ")");
    }

    private void displayFinalGameState() {
        eraseScreen();
        System.out.println("Dealer has: ");
        dealerHand.display();
        System.out.println(" (" + dealerHand.value() + ")");

        displayPlayerHand();
    }

    private void eraseScreen() {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
    }

    private void displayBackOfCard() {
        System.out.print(
                ansi()
                        .cursorUp(7)
                        .cursorRight(12)
                        .a("?????????????????????????????????").cursorDown(1).cursorLeft(11)
                        .a("?????????????????????????????????").cursorDown(1).cursorLeft(11)
                        .a("?????? J I T ??????").cursorDown(1).cursorLeft(11)
                        .a("?????? T E R ??????").cursorDown(1).cursorLeft(11)
                        .a("?????? T E D ??????").cursorDown(1).cursorLeft(11)
                        .a("?????????????????????????????????").cursorDown(1).cursorLeft(11)
                        .a("?????????????????????????????????"));
    }

    public int playerBalance() {
        return playerBalance;
    }

    public void playerDeposits(int depositAmount) {
        playerBalance += depositAmount;
    }

    public void playerBets(int betAmount) {
        playerBalance -= betAmount;
        playerBetAmount = betAmount;
    }

    private void payoff(double multiplier) {
        playerBalance += playerBetAmount * multiplier;
    }

    public void playerWins() {
        payoff(2);
    }

    public void playerLoses() {
        payoff(0);
    }

    public void playerPushes() {
        payoff(1);
    }

    public void playerWinsBlackjack() {
        payoff(2.5);
    }
}


