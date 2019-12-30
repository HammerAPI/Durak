/* Author: Daniel Hammer
 *
 * Face enumerations
 *
 * Contains the name of a card's face and its value, as well as methods
 * to retrieves these fields
 */

public enum Face{
	TWO("Two", 1),
	THREE("Three", 2),
	FOUR("Four", 3),
	FIVE("Five", 4),
	SIX("Six", 5),
	SEVEN("Seven", 6),
	EIGHT("Eight", 7),
	NINE("Nine", 8),
	TEN("Ten", 9),
	JACK("Jack", 10),
	QUEEN("Queen", 11),
	KING("King", 12),
	ACE("Ace", 13);

    /**********
     * FIELDS *
     * *******/

    private String name;
    private int rank;
    
    


    /***************
     * CONSTRUCTOR *
     * ************/

    Face(String name, int rank){
        this.name = name;
        this.rank = rank;
    }
    



    /************
     * MUTATORS *
     * *********/

    public String getFace(){
        return this.name;
    }
    
    public int getRank(){
        return this.rank;
    }
}
