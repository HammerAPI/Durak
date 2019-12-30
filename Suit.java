/* Author: Daniel Hammer
 *
 * Suit enumeration
 *
 * Contains the name of a card's suit, as well as a method to retrieve it
 */

public enum Suit{
    DIAMONDS("Diamonds"),
    HEARTS("Hearts"),
    SPADES("Spades"),
    CLUBS("Clubs");




    /**********
     * FIELDS *
     * *******/
    
    private String suit;

    
    
    
    /***************
     * CONSTRUCTOR *
     * ************/

    Suit(String suit){
        this.suit = suit;
    }




    /************
     * MUTATORS *
     * *********/
    public String getSuit(){
        return this.suit;
    }
}
