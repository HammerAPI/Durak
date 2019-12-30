/* Author: Daniel Hammer
 *
 * Durak Driver
 *
 * Handles the execution of a game of Durak (Russian; "fool")
 *      from the supplies Card, Face, Suit, Player, and Game classes
 * Currently only simulates a playthrough of the game
 * A future version will allow user input and let the user play against NPCs
 *
 * For a list of rules, please see http://www.gamecabinet.com/rules/Durak.html
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Durak{
    
    /**********
     * FIELDS *
     *********/

    private Game durak;
    private Scanner sc;

    // ArrayLists of cards to represent the deck,
    //      attack pile, and defended pile;
    private ArrayList<Card> deck;
    private ArrayList<Card> attack;
    private ArrayList<Card> defended;

    // ArrayLists of players to represent NPCs, a list of attackers, and a list
    //      of players to remove (bypass ConcurrentModificationException)
    private ArrayList<Player> players;
    private ArrayList<Player> attackers;

    // Placeholders to represent the defender (during an attack) 
    // and the loser of the game
    private Player defender;
    private Player loser;

    // Placeholders to represent the cards being used to attack/defend
    private Card currentAttack;
    private Card currentDefense;

    // Indexes for rounds and attackers/defenders
    private int roundCounter, currentDefender, currentAttacker, numAttacks;

    // The number of cards in each player's starting hand
    // Also the minimum number of cards a player must be holding
    private final int INITIAL_HAND_SIZE = 6;
    private final int NUM_PLAYERS = 4;
    private final int MAX_NUM_ATTACKS = 6;




    /***************
     * CONSTRUCTOR *
     * ************/

    public Durak(){
        this.durak = new Game(INITIAL_HAND_SIZE);
        this.sc = new Scanner(System.in);
        
        // Builds a deck of cards
        this.deck = durak.getDeck();
        this.attack = new ArrayList<Card>();
        this.defended = new ArrayList<Card>();

        // Builds a list of players
        this.players = durak.getPlayers();
        this.attackers = new ArrayList<Player>();

        this.roundCounter = 0;
    }




    /*********************
     * PROGRAM EXECUTION *
     * ******************/

    public static void main(String[] args){
        Durak game = new Durak();
        game.playGame();
    }




    // Game logic
    public void playGame(){

        
        // Shuffle the deck and pick a suit to be the trump suit
        durak.shuffle(deck);
        setTrumpSuit();

        // Give all players the INITIAL_HAND amount of cards (6)
        durak.dealCardsToAllPlayers();


        // This is the game loop
        // This will execute until there is either one player left
        //      (who then becomes the lower), or two players end in a draw
        do{
            // Display all of the players and their current cards
            // Print the number of cards remaining in the deck
            // ONLY if hints are enabled!
            if (durak.getHints()){
                durak.displayAllPlayersHands();
                System.out.println("\nThere are " + deck.size()
                + " remaining in the deck\n");
                displayDelay();
            }

            // Commence a series of attacks and defenses
            round();

            // After the round has concluded, deal everyone cards until they
            // all have 6 or there are none left in the deck
            durak.dealCardsToAllPlayers();

            // If a player has no more cards and the deck is empty,
            // remove them from the game
            durak.removeFinishedPlayers();

            // Pause so the user can read easier
            displayDelay();
        } while ((!isGameOver()));
    }




    /*******************
     * LOGICAL METHODS *
     * ****************/

    // Commences a series of attacks and defenses
    public void round(){

        roundCounter++;
        numAttacks = 0;
        System.out.println("\n======================= Round #"
                + roundCounter + " =======================");

        // All player and card lists from the previous round are cleared
        attackers.clear();
        attack.clear();
        defended.clear();

        // A new attacker and new defender are chosen
        currentDefender = roundCounter % (players.size());
        currentAttacker = (currentDefender + 1) < players.size() ?
            currentDefender + 1 : 0;

        // The attacker and defender roles are assigned
        attackers.add(players.get(currentAttacker));
        defender = players.get(currentDefender);
        System.out.println("\tCurrent Defender: " + defender.getName() + "\n");


        // Attacks:
        //
        // The first attack only involves one attacker and one defender
        // The attacker will place down a card from his/her hand
        // If the defender has a card of higher rank OR trump, play it
        // Then comes the second attack, which differs slightly from the first
        // If the initial attacker CAN and WILL attack again, he/she gets to
        // attack first, and then other players can follow suit
        if ((attackers.get(0).willAttack()
                && attackers.get(0).canPlay(defended))
                || numAttacks == 0){

            System.out.println("\nIn attack #" + (++numAttacks) + ":");
            // If the defender fails to defend, the turn is over
            if (attackers.get(0).attack(defender, defended, attack, numAttacks) == false){
                return;
            }
            // If the defender has defended 6 total attacks, the turn is over
            if (numAttacks >= MAX_NUM_ATTACKS){
                System.out.println("\n" + defender.getName() + " has endured"
                        + " the maximum number of attacks and the turn is over");
                return;
            }
            
            // Add all new attackers to the attacker list
            for (Player p : players){
                if (!(attackers.contains(p)) && !(p.equals(defender))){
                    attackers.add(p);
                }
            }

            // Now that the attacker lists is filled, loop through it
            // Let every attacker have as many chances as he/she wants to attack
            for (Player p : attackers){
                while (p.canPlay(defended) && p.willAttack()){
                    System.out.println("\nIn attack #" + (++numAttacks) + ":");
                    
                    // If the defender fails, the turn is over
                    if (p.attack(defender, defended, attack, numAttacks) == false){
                        return;
                    }
                    // If the defender has defended 6 times, the turn is over
                    if (numAttacks >= MAX_NUM_ATTACKS){
                        System.out.println("\n" + defender.getName()
                                + " has endured the maximum number of attacks"
                                + " and the turn is over");
                        return;
                    }
                }
            }
        }
    }




    // Picks a random card from the deck and sets its suit to the Trump Suit
    public void setTrumpSuit(){
        Random rand = new Random();
        int trump = rand.nextInt(52);
        System.out.println("The Trump suit is now "
                + deck.get(trump).getSuit());
        for (Card c : deck){
            if (c.getSuit().equals(deck.get(trump).getSuit())){
                c.setTrump();
            }
        }
    }




    // Determines if anyone has lost or if the game has ended
    public boolean isGameOver(){
        if (players.size() == 1){
            loser = players.get(0);
            loser.setVictory(false);
            System.out.println("\n" + loser.getName() + " is the loser!\n");
            return true;
        }
        else if (players.size() == 0){
            System.out.println("\nThe game is a draw!");
            return true;
        }
        return false;
    }

    public void displayDelay(){
        if (durak.getDisplayDelay()){
            durak.displayDelay();
        }
    }
}
