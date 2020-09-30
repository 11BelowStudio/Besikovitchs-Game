package Packij.BetterGUI;

public class BotCardPanel extends PlayerCardPanel {

    BotCardPanel(GameFrame game, int owner) {
        super(game, owner);
    }

    public void autoSelectCard(int cardToSelect){
        //basically automatically selects the card
        playCards.get(cardToSelect-1).autoSelect();
    }

}
