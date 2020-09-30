package Packij.BetterGUI;

import javax.swing.*;
import java.awt.*;

class PanelForCardPanels extends JPanel {

    private GameFrame game;

    //basically this holds the card-related panels, and uses gridbaglayout to make it look better

    //in a seperate class to prevent it from clogging up the game initialisation code
    PanelForCardPanels(GameFrame game){
        this.game = game;
        this.setLayout(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 5;
        c.ipady = 5;
        c.weightx = 1;
        c.weighty = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        this.add(game.stakePanel,c);


        c.weighty = 1;
        c.gridy = 1;
        this.add(game.player1Panel,c);


        c.gridy = 2;
        this.add(game.player2Panel,c);

    }
}
