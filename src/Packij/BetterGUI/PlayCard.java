package Packij.BetterGUI;

import java.awt.*;

public class PlayCard extends Card {

    //The cards which the users can 'play' (and are interacted with basically)

    private int owner; //keeps track of who is using it: 0-stake pile, 1-player 1, 2-player 2
    private boolean used; //whether the card has been used or not
    private GameFrame game;


    public PlayCard(int cardValue, int cardOwner, GameFrame game){
        super(cardValue);

        this.setBackground(new Color(125, 200, 50));
        owner = cardOwner;
        used = false;
        this.game = game;
    }

    public void selectCard(){
        //will return 0 if the card has already been used
        //card turns red and the value of the card is returned if it hasn't been used yet
        if (!used){
            if (isSelectable()) {
                this.setBackground(new Color(200, 50, 50));
                used = true;
                game.cardChosen(value);
            }
        }

    }

    public void autoSelect(){
        //called by the player 2 bot when it doing its thing
        //the player 2 bot logic won't allow duplicate cards to be selected though
        this.setBackground(new Color(200, 50, 50));
        used = true;
    }

    public boolean isSelectable(){
        return (this.owner == game.getGameState());
    } //checks to see if the current gamestate stuff will allow this card to be selected (gamestate int matches owner identifier or not)


    @Override
    public String toString(){
        return (owner + ", " + value);
    }
}
