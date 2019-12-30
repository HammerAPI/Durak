/* Author: Daniel Hammer
 *
 * Game class
 *
 * Contains the methods needed to handle a standard card game
 * Not all methods/fields will be used in every card game
 * Most of what exists here is here to keep the individual game files smaller
 */

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game{
    
    /**********
     * FIELDS *
     * *******/

    // A final size of the deck. No calculations should exceed this!
    private final int DECK_SIZE = Face.values().length * Suit.values().length;
    private final int INITIAL_HAND_SIZE;
    
    private int numPlayers;
    private boolean enableHints;
    private boolean enableDisplayDelay;
    private boolean willUserPlay;

    private ArrayList<Card> deck;
    private ArrayList<Player> players;

    private Scanner sc = new Scanner(System.in);



    /**************
     * CONTRUCTOR *
     * ***********/
    
    // Called by Durak user-input simulation
    public Game(int initialHand){
        settings();

        this.deck = new ArrayList<Card>(DECK_SIZE);
        this.deck = buildDeck();

        this.players = new ArrayList<Player>(numPlayers);
        this.players = buildPlayerList(numPlayers);
        
        if (this.willUserPlay == true){
            createUser();
        }

        if (DECK_SIZE / numPlayers < initialHand){
            initialHand = (DECK_SIZE / numPlayers);
        }
        this.INITIAL_HAND_SIZE = initialHand;
        System.out.println("\n\n\n\n\n\n\n\n");
    }




    /************
     * MUTATORS *
     * *********/

    public ArrayList<Card> getDeck(){
        return this.deck;
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public Player getPlayerByIndex(int index){
        return this.players.get(index);
    }

    public Player getPlayerByName(String name){
        for (Player p : players){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public void setHints(boolean bool){
        this.enableHints = bool;
    }

    public boolean getHints(){
        return this.enableHints;
    }

    public void setDisplayDelay(boolean bool){
        this.enableDisplayDelay = bool;
    }

    public boolean getDisplayDelay(){
        return this.enableDisplayDelay;
    }

    public void letUserPlay(boolean bool){
        this.willUserPlay = bool;
    }

    public boolean isUserPlaying(){
        return this.willUserPlay;
    }
    



    /*****************
     * LOGIC METHODS *
     * **************/

    // Builds an ArrayList of Cards containing 4 suits of 13 cards each
    public ArrayList<Card> buildDeck(){
        for (Suit s : Suit.values()){
            for (Face f : Face.values()){
                deck.add(new Card(s, f, f.getRank()));
            }
        }
        return deck;
    }

    // Builds an ArrayList of players with default parameters
    public ArrayList<Player> buildPlayerList(int numPlayers){
        for (int i = 0; i < numPlayers; i++){
            Player newPlayer = new Player("Player #" + (i + 1), 100, 100);
            players.add(newPlayer);
        }
        return players;
    }

    // Removes a card from the top of the deck and returns it
    public Card deal(){
        return deck.remove(deck.size() - 1);
    }

    // Shuffles the deck of cards in a random order
    public void shuffle(ArrayList<Card> cards){
        Collections.shuffle(cards, new Random());
    }

    // Sorts all players' hands
    public void sortPlayersHands(){
        for (Player p : players){
            p.sortHand();
        }
    }

    // Deal cards to all players until either
    // A - every player has at least 6 cards
    // B - the deck is empty
    public void dealCardsToAllPlayers(){
        int fullHands = 0;
        while (fullHands < players.size()){
            for (Player p : players){
                if (p.getHand().size() < INITIAL_HAND_SIZE
                        && deck.size() != 0){
                    p.addCard(deal());
                }
                else{
                    fullHands++;
                }
            }
        }
    }

    // Removes players from the game if their hands and the deck are empty
    public void removeFinishedPlayers(){
        ArrayList<Player> toRemove = new ArrayList<Player>();
        for (Player p : players){
            if (p.getHand().size() == 0 && this.deck.size() == 0){
                toRemove.add(p);
                p.setVictory(true);
                System.out.println("\n" + p.getName() + " has no more cards"
                        + " and is done with the game");
            }
        }
        // Avoids ConcurrentModificationException
        for (Player p : toRemove){
            players.remove(p);
        }
        toRemove.clear();
    }

    /*****************
     * INPUT METHODS *
     * **************/

    public int howManyPlayers(){
        System.out.print("How many players in total for this game?\n> ");
        numPlayers = Integer.parseInt(sc.nextLine());
        return numPlayers;
    }

    public void createUser(){
        System.out.print("Enter your name: ");
        players.get(0).setName(sc.nextLine());
        players.get(0).setUser(true);
    }

    public void settings(){
        int input;
        System.out.println("==================== SETTINGS ====================");
        System.out.println("Let a user play? (1-yes / 0-no)");
        input = Integer.parseInt(sc.nextLine());

        this.willUserPlay = (input == 1) ? true : false;

        System.out.println("Enable hints? (1-yes / 0-no)");
        input = Integer.parseInt(sc.nextLine());
        
        this.enableHints = (input == 1) ? true : false;

        System.out.println("Enable display delay? (1-yes / 0-no)");
        input = Integer.parseInt(sc.nextLine());

        this.enableDisplayDelay = (input == 1) ? true : false;

        howManyPlayers();
    }

    public void displayDelay(){
        System.out.print("\n\nDisplay Delay - Press '0' to continue\n\n");
        int input;
        do{
            input = Integer.parseInt(sc.nextLine());
        } while (input != 0);
    }


    /*******************
     * DISPLAY METHODS *
     * ****************/

    // Displays the contents of the passed pile
    public void displayPile(ArrayList<Card> pile){
        for (Card c : pile){
            System.out.println(c);
        }
    }

    // Displays all hands of all players
    public void displayAllPlayersHands(){
        System.out.println("\nDisplaying all players' hands:");
        for (Player p : players){
            System.out.println();
            p.displayHand();
        }
    }
}
