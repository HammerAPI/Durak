/* Author: Daniel Hammer
 *
 * Card class
 *
 * Contains attributes to create a unique playing card, such as
 *      Face, Suit, Rank, and whether or not the card is a trump suit
 *      A trump suit is more valuable than any other card
 *
 * Also contains methods to compare two cards, and display as a string
 */

@SuppressWarnings("overrides")
public class Card implements Comparable<Card>{

    /**********
     * FIELDS *
     * *******/

    private Suit suit;
    private Face face;
    private int rank;
    private boolean isTrump;




    /**************
     * CONTRUCTOR *
     * ***********/
    
    public Card(Suit suit, Face face, int rank){
        this.suit = suit;
        this.face = face;
        this.rank = rank;
        this.isTrump = false;
    }




    /************
     * MUTATORS *
     * *********/

    public Suit getSuit(){
        return this.suit;
    }

    public Face getFace(){
        return this.face;
    }

    public int getRank(){
        return this.rank;
    }
    
    public void setTrump(){
        this.rank = rank + 13;
        this.isTrump = true;
    }
    
    public boolean isTrump(){
        return isTrump;
    }




    /*****************
     * LOGIC METHODS *
     * **************/

    // Returns positive if 'this' card is bigger,
    // negative if 'c' card is bigger, and 0 if they are equal
    @Override
    public int compareTo(Card c){
        return this.getRank() > c.getRank() ? 1 :
            this.getRank() < c.getRank() ? -1 : 0;
    }

    // Returns true if the 'this' is equal to 'other', else false
    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        else{
            Card c = (Card) other;
            return this.rank == c.getRank()
                && this.suit == c.getSuit()
                && this.face == c.getFace()
                && this.isTrump == c.isTrump();
        }
    }

    // Displays the card's Face and Suit
    public String toString(){
        return isTrump ? this.face + " of " + this.suit + " (T)"
            : this.face + " of " + this.suit;
    }
}
