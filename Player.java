/* Author: Daniel Hammer
 *
 * A player class
 *
 * Contains information to construct a Non-Player Character in a card game
 * Includes play chance, a list of cards as the player's hand, and methods to
 * simulate a player playing, dealing, and receiving cards
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Player{
    
    /**********
     * FIELDS *
     *********/

    private String playerName;
    private boolean isVictorious;
    private boolean isUser;
    private ArrayList<Card> hand;
    private final int PERCENT_TO_ATTACK;
    private final int PERCENT_TO_DEFEND;

    private Scanner sc = new Scanner(System.in);



    /***************
     * CONSTRUCTOR *
     * ************/

    // NPC Constructor
    public Player(String playerName, int atk, int def){
        this.playerName = playerName;
        this.isVictorious = false;
        this.hand = new ArrayList<Card>();
        this.PERCENT_TO_ATTACK = atk;
        this.PERCENT_TO_DEFEND = def;
    }

    // User constructor
    public Player(){
        this.playerName = "User";
        this.isVictorious = false;
        this.isUser = true;
        this.hand = new ArrayList<Card>();
        this.PERCENT_TO_ATTACK = 100;
        this.PERCENT_TO_DEFEND = 100;
    }

    

    /************
     * MUTATORS *
     * *********/

    public String getName(){
        return this.playerName;
    }

    public void setName(String playerName){
        this.playerName = playerName;
    }
    
    public ArrayList<Card> getHand(){
        return this.hand;
    }

    public boolean getVictory(){
        return this.isVictorious;
    }
    
    public void setVictory(boolean bool){
        this.isVictorious = bool;
    }

    public boolean isUser(){
        return this.isUser;
    }

    public void setUser(boolean bool){
        this.isUser = bool;
    }

    public void removeCard(Card card){
        this.hand.remove(card);
    }

    public void addCard(Card card){
        this.hand.add(card);
        Collections.sort(hand);
    }
    
    public void drawCard(ArrayList<Card> pile){
        this.hand.add(pile.remove(pile.size() - 1));
        Collections.sort(hand);
    }




    /***********
     * CHANCES *
     * ********/

    // Both methods function identically:
    // Generates a random number between 1 and 100
    // If the number is less than the percent needed, return true
    // else return false
    public boolean willAttack(){
        Random rand = new Random();
        int r = rand.nextInt(100) + 1;

        return (r < PERCENT_TO_ATTACK) ? true : false;
    }

    public boolean willDefend(){
        Random rand = new Random();
        int r = rand.nextInt(100) + 1;

        return (r < PERCENT_TO_DEFEND) ? true : false;
    }




    /****************
     * PLAY METHODS *
     * *************/

    // Returns true if the player has a card with the same suit as any card
    // in the passed pile (usually the 'defended' pile)
    // else returns false
    public boolean canPlay(ArrayList<Card> pile){
        if (pile.size() == 0){
            return true;
        }
        for (Card c : this.hand){
            for (Card def : pile){
                if (c.getSuit().equals(def.getSuit())){
                    return true;
                }
            }
        }
        return false;
    }




    // Plays a card from the player's hand
    public Card playAnAttack(ArrayList<Card> pile){

        if (isUser){

            Card currentAttack = null;
            int handIndex = 0;
            ArrayList<Integer> legalCardChoices = new ArrayList<Integer>();

            // If the user is initiating the attack, let the user pick from any
            //      card in his/her hand
            if (pile.size() == 0){
                    System.out.println("\tYou may attack with any card from your hand");
                    System.out.println("\n\tEnter a number to play that card");

                    for (Card c : this.hand){
                        System.out.println("\t" + (handIndex + 1) + " | " + c);
                        handIndex++;
                    }
                    // Get a valid card choice from the user
                do{
                    System.out.print("\n\t> ");
                    handIndex = Integer.parseInt(sc.nextLine()) - 1;
                } while (handIndex >= this.hand.size() || handIndex < 0);

                currentAttack = this.hand.get(handIndex);
                this.removeCard(currentAttack);
            }

            // If the user is joining a pre-existing attack, let the user pick from
            //      any card in his/her hand that is legal for the attack
            else{
                ArrayList<Suit> legalSuitsToPlay = new ArrayList<Suit>();

                // Compile a list of legal suits
                for (Card c : pile){
                    if (!(legalSuitsToPlay.contains(c.getSuit()))){
                        legalSuitsToPlay.add(c.getSuit());
                    }
                }

                System.out.println("\n\tYou may attack with any of the following cards"
                            + " or press '0' to pass");

                for (Card c : this.hand){
                    if(legalSuitsToPlay.contains(c.getSuit())){
                        System.out.println("\t" + (handIndex + 1)+ " | " + c);
                        legalCardChoices.add(handIndex);
                    }
                    handIndex++;
                }

                // Get a valid card choice from the user
                do{
                    System.out.print("\n\t> ");
                    handIndex = Integer.parseInt(sc.nextLine()) - 1;
                    if (handIndex == -1){
                        return null;
                    }
                } while (!(legalCardChoices.contains(handIndex)));

                currentAttack = this.hand.get(handIndex);
                this.removeCard(currentAttack);
            }
            System.out.println();
            return currentAttack;
        }

        // If the player is NOT the user, randomly pick a card from the hand
        else{
            Random rand = new Random();
            int r = rand.nextInt(this.hand.size());
            System.out.println(this.getName() + " played " + this.hand.get(r));
            return this.hand.remove(r);
        }
    }




    // Sorts a player's hand
    public void sortHand(){
        Collections.sort(this.hand);
    }




    // If the player has a higher card in hand than the one being played,
    // or if the player has a trump card and, if the card being played is also
    // a trump, the player's card is higher, return true, else return false
    public boolean hasHigherCard(Card toBeat){
        for (Card c : this.hand){
            if (c.getSuit().equals(toBeat.getSuit())){
                if (c.getRank() > toBeat.getRank()){
                    return true;
                }
            }
            else if (c.isTrump()){
                if (toBeat.isTrump() && c.getRank() < toBeat.getRank()){
                    return false;
                }
                return true;
            }
        }
        return false;
    }




    // Loops through the player's cards
    // If the player has a higher card than the one being played, return it
    // If the player has a trump card and, if the card being played
    //      is also a trump, the player's card is higher, return it
    // The double loop ensures that players will retain trump cards if possible
    public Card returnHigherCard(Card toBeat){

        if (isUser){

            Card currentDefense;
            int handIndex = 0;
            ArrayList<Integer> legalCardChoices = new ArrayList<Integer>(0);

            System.out.println("\n\tYou may defend with any of the following cards"
                            + " or press '0' to forfeit and pick up the attack");

            // Build a list of legal cards to play
            for (Card c : this.hand){
                if ((c.getSuit().equals(toBeat.getSuit())
                        || (c.isTrump() && toBeat.isTrump()))
                        && (c.getRank() > toBeat.getRank())
                        || (c.isTrump() && !(toBeat.isTrump()))){

                    System.out.println("\t" + (handIndex + 1) + " | " + c);
                    legalCardChoices.add(handIndex);
                }
                handIndex++;
            }

            // Get a valid card choice from the user
            do{
                System.out.print("\n\t> ");
                handIndex = Integer.parseInt(sc.nextLine()) - 1;
                if (handIndex == -1){
                    return null;
                }
            } while (!(legalCardChoices.contains(handIndex)));

            currentDefense = this.hand.get(handIndex);
            this.removeCard(currentDefense);
            return currentDefense;
        }

        // If the player is NOT the user
        else{
            // Randomly choose a higher-ranking card to play
            for (Card c : this.hand){
                if (c.getSuit().equals(toBeat.getSuit())){
                    if (c.getRank() > toBeat.getRank()){
                        this.hand.remove(c);
                        return c;
                    }
                }
            }
            // If a higher ranking card cannot be found,
            // randomly choose a valid trump
            for (Card c : this.hand){
                if (c.isTrump()){
                    if (toBeat.isTrump() && c.getRank() < toBeat.getRank()){
                        return null;
                    }
                    this.hand.remove(c);
                    return c;
                }
            }
        }
        return null;
    }




    // Returns true if the attack was defended, false if not
    public boolean attack(Player defender, ArrayList<Card> defended,
            ArrayList<Card> attack, int atk){

        Card currentAttack;
        Card currentDefense;
        boolean hasPlayed = false;

        // If this IS the first attack of the round
        if (atk == 1){
            System.out.print("\t");

            currentAttack = this.playAnAttack(defended);

            // If the defender has a higher card than the one being played,
            // AND the defender chooses to attack, then attack
            if (defender.hasHigherCard(currentAttack)
                    && defender.willDefend()){

                // Set the current defense to a higher card from the defender's hand
                currentDefense = defender.returnHigherCard(currentAttack);
                if (currentDefense == null){
                    defender.addCard(currentAttack);
                    return false;
                }
                System.out.print("\tBut " + defender.getName()
                        + " has a higher card of ");
                System.out.println(currentDefense);

                // Attack successfully defended,
                // So add those cards to the defended pile
                defended.add(currentAttack);
                defended.add(currentDefense);
            }
            // If either the defender cannot counter the attack or chose not to
            // Pick up the attack card and end the turn
            else{
                System.out.println("\t" + defender.getName()
                        + " will not fight the attack");
                defender.addCard(currentAttack);
                return false;
            }
        }

        // If this is NOT the first attack of the round
        else{

            if (isUser){
                currentAttack = this.playAnAttack(defended);

                // If the user entered '0' to pass on an attack
                if (currentAttack == null)
                {
                    return false;
                }
                attack.add(currentAttack);
            }
            
            // If the attacker is NOT the user
            else{
                // Loop through the attackers hand, returning a card with the same suit
                //      as one that has already been played
                for (Card c : this.hand){
                    for (Card def : defended){
                        if (hasPlayed == false){
                            if (c.getSuit().equals(def.getSuit())){
                                System.out.println("\t" + this.getName()
                                        + " played " + c);
                                attack.add(c);
                                hasPlayed = true;
                            }
                        }
                    }
                }
                // Remove the played card from the attacker's hand
                for (Card c : attack){
                    if (this.getHand().contains(c)){
                        this.removeCard(c);
                    }
                }
            }
        }




        // The defender must now defend, if the attack list is not empty
        if (attack.size() != 0){
            currentAttack = attack.remove(0);

            if (!(defended.contains(currentAttack))){

                // If the defender has a higher card, play it
                // and add to the pile of successfully defended cards
                if (defender.hasHigherCard(currentAttack)){
                    currentDefense = defender.returnHigherCard(currentAttack);

                    // If the user entered '0' and chose to pick up the attack
                    if (currentDefense == null){
                        defender.addCard(currentAttack);

                        for (Card pickUp : attack){
                            if (!(defender.getHand().contains(pickUp))){
                                defender.addCard(pickUp);
                            }
                        }
                        for (Card pickUp : defended){
                            if (!(defender.getHand().contains(pickUp))){
                                defender.addCard(pickUp);
                            }
                        }
                        return false;
                    }
                    System.out.print("\tBut " + defender.getName()
                            + " has a higher card of ");
                    System.out.println(currentDefense);
                    defended.add(currentDefense);
                    defended.add(currentAttack);
                }
                // Otherwise, pick up all the cards on the table
                else{
                    System.out.println("\t" + defender.getName()
                            + " will not fight the attack");
                    defender.addCard(currentAttack);
                    for (Card pickUp : attack){
                        if (!(defender.getHand().contains(pickUp))){
                            defender.addCard(pickUp);
                        }
                    }
                    for (Card pickUp : defended){
                        if (!(defender.getHand().contains(pickUp))){
                            defender.addCard(pickUp);
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }




    /*****************
     * PRINT METHODS *
     * **************/

    // Displays the player's name and all cards in his/her hand
    public void displayHand(){
        System.out.println(playerName + "'s hand: " + hand.size());
        for (Card c : this.hand){
            System.out.println("\t" + c);
        }
    }

    // Returns a string of the player's name and all cards in his/her hand
    public String toString(){
        return this.playerName + this.hand;
    }
}
