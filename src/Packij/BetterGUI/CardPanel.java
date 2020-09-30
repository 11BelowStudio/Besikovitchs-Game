package Packij.BetterGUI;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;

class CardPanel extends JPanel {


    GridLayout theLayout;

    //JPanel chosenPanel;


    private AttributeLabel chosenCard;

    ArrayList<PlayCard> playCards;



    CardPanel(GameFrame game, int owner){


        this.setBorder(new EtchedBorder());

        theLayout = new GridLayout(1,1);
        this.setLayout(theLayout);

        //setting up the panel for the card choices

        JPanel playPanel = new JPanel();
        playPanel.setLayout(new BorderLayout());

        JPanel playHeaderPanel = new JPanel();
        playHeaderPanel.setLayout(new BorderLayout());

        chosenCard = new AttributeLabel();
        switch (owner){
            case 0:
                chosenCard.setAttributeName("The stake card is: ");
                break;
            case 1:
                chosenCard.setAttributeName("Player 1's chosen card is: ");
                break;
            case 2:
                chosenCard.setAttributeName("Player 2's chosen card is: ");
                break;
        }

        playHeaderPanel.add(chosenCard,BorderLayout.NORTH);

        JLabel playTitle = new JLabel("Cards in deck (green: unused, red: used)");
        playHeaderPanel.add(playTitle,BorderLayout.SOUTH);

        playPanel.add(playHeaderPanel,BorderLayout.NORTH);
        //and now the cards
        JPanel playCardsPanel = new JPanel();
        playCardsPanel.setLayout(new GridLayout(1,13,1,1));

        playCards = new ArrayList<>(); //this will hold the card objects

        //instantiating all the card objects, and adding them to PlayCardsPanel
        for(int i = 1; i<14;i++){
            playCards.add(new PlayCard(i,owner, game));
            playCards.get(i-1).addMouseListener(new MouseHandler());
            playCardsPanel.add(playCards.get(i-1));
        }
        playPanel.add(playCardsPanel,BorderLayout.CENTER);
        //playCardsPanel added to the middle of PlayCardsPanel

        this.add(playPanel);

    }

    void cardChosen(int cardValue){
        chosenCard.showValue(cardValue);
    }

    void resetValue(){
        chosenCard.clearDisplayValue();
    }
}
