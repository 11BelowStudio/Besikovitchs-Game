# Besikovitchs-Game
An implementation of Besikovitch's Game that I had to make for my year 2 challenge week as part of my degree. Took roughly 36 hours between the 13th and the 17th of January 2020

Written in Java 8.

It's supposed to be played with a shuffled suit of 13 cards on hand. You enter the value of the stake card, you make your bid for the stake card, and the bot makes its bid for the stake card.

The bot makes its bid as soon as it notices that you have made your bid, but the bot doesn't get to see what your bid was.

The cards used to make a bid are discarded after use, regardless if they won the card or not.

Highest bid wins the value of the stake card, stake card gets discarded when the bids are identical.

Highest score wins, but the game ends early if there's a situation where a particular player is guaranteed to win and it's impossible for the other player to defeat them.

## Running it

* There are three ways of running this
    * src/test/TestExceptions.java
        * a jUnit test thing for exception handling
    * src/Packij/BotTester.java
        * Simulates 5000 games between the bot and 4 simple agents (RNG, lowest bid, highest bid, bidding stake value)
            * total of 20000 simulated games
        * outputs results to console
    * src/Packij/MainClassForPlayingTheGame.java
        * use this to play the game
            * bottom text
            
yeah have fun I guess