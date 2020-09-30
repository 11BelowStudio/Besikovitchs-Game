package Packij.BetterGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerCardPanel extends  CardPanel {

    JPanel wonPanel;
    AttributeLabel scoreCounter;
    JPanel wonCardPanel;

    ArrayList<WonCard> wonCards;

    PlayerCardPanel(GameFrame game, int owner) {
        super(game, owner);
        theLayout.setRows(2);
        this.setLayout(theLayout);

        wonPanel = new JPanel();
        wonPanel.setLayout(new BorderLayout());

        scoreCounter = new AttributeLabel("Player "+ owner + "'s  total score: ", 0);
        wonPanel.add(scoreCounter,BorderLayout.NORTH);

        wonCardPanel = new JPanel();
        wonCardPanel.setLayout(new GridLayout(1,13,1,1));

        wonCards = new ArrayList<>();

        wonPanel.add(wonCardPanel,BorderLayout.CENTER);

        this.add(wonPanel);
    }

    public void addWonCard(int cardValue){
        WonCard justWon = new WonCard(cardValue);
        wonCardPanel.add(justWon);
        scoreCounter.increaseValue(cardValue);
    }


}