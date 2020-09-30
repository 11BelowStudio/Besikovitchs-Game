package Packij.BetterGUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Card extends JPanel {
    //basically this is a jpanel to represent the cards and such in the game

    protected int value; //keeps track of what value the card holds
    private JLabel valueLabel; //basically its the thing that shows the card value to the player



    public Card(int cardValue){

        this.setLayout(new GridLayout(1,1));

        value = cardValue;
        valueLabel = new JLabel(String.valueOf(cardValue),SwingConstants.CENTER);
        this.setBorder(new EtchedBorder());


        this.add(valueLabel);


    }

}
