## Durak Simulator

A simulator for the popular Russian card game Durak (Russian: "fool");


### Information

This program simulates a game of Durak with a selectable number of players.
All action, from shuffling the deck to determining whether or not a player will attack/defend is randomly generated.

A typical game follows a simple procedure:
1. All players are dealt six cards each from a shuffled deck of fifty-two cards
2. A 'trump' card is picked and used to determine what the 'trump' suit is for the game
3. One player is selected to be the defender, and another is selected to be the attacker
4. The attacker lays down a card and the defender must either play a card with the same suit and higher rank or any trump card (if the attack is a trump, only a trump of a higher rank can defeat it)
5. If the attack was successfully defeated, the attacker can choose to attack again, in which case all remaining players (except the defender) can also attack
6. Attacks may place a maximum of six total cards for the defender to defend, given that each new card matches the suit of a card played previously in the attack
7. If at any point the defender cannot or chooses not to defend, he/she must take up all cards currently on the pile
8. After a round, all players must draw from the deck until they each have at least six cards in hand, or until the deck is empty
9. If the deck is empty and a player has played all of his/her cards, that player is done playing
10. Steps 3-9 are repeated for a new attacker and defender until there is one player left- that player is the loser/fool
11. If an attack ends and all remaining players have no cards and the deck is empty, the game ends in a draw

More information on the rules of Durak can be found here http://www.gamecabinet.com/rules/Durak.html


### Features

User-controlled:
- Play against NPCs or run a simulation of a game.

Random:
- Everything from shuffling the deck to determining whether a player will attack or defend to players picking what cards to play is based on random generation.

Settings:
- Want to see what cards your opponents have? Check the `hints` flag upon running to display extra information in-game.

Fast:
- An entire game simulation will run in less than a second. Checking the `delay display` flag will enable delay, letting you read more information each turn.


### To run:

Download and compile the files.
Run Durak.java in the command line.
The program will output a randomized simulation of the game.


#### TODO:

- Restructure attack method to handle players alternating attacks.
    Currently, whoever begins the attack will attack until they are done,
    only then can new players attack
    It should be revised so that after the initial attacker's 2nd attack, any
    player can join in on the attack
