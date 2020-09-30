package Packij.BetterGUI;

import java.awt.*;

public class WonCard extends Card {

    //Subclass of 'Card' for any cards which a player has 'won'

    WonCard(int value){
        super(value);
        this.setBackground(new Color(50, 200, 200));
    }
}
