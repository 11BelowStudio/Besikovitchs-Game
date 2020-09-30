package Packij.BetterGUI;

import javax.swing.*;

public class InstructionLabel extends JLabel {

    GameFrame game;

    String messageText;

    InstructionLabel(GameFrame game){
        this.game = game;
        messageText = "";
        this.setText("Please select a stake card");

    }

    public int updateLabel(int gameStage){
        int newStage = gameStage;
        boolean showDialog = false;
        /*
        What each gameStage value means:
            0: stake card selection
            1: player 1 card selection
            2: player 2 card selection
            3: player 1 won the stake card - select new stake
            4: player 2 won the stake card - select new stake
            5: draw for the stake card - select new stake
            6: player 1 wins game - need to restart
            7: player 2 wins game - need to restart
            8: game ends in a draw - need to restart
            9: waiting for game to restart.
        */

        switch (gameStage){
            case 0:
                messageText ="Please select a stake card";
                break;
            case 1:
                messageText ="Player 1, please choose a card to play";
                break;
            case 2:
                messageText ="Player 2, please choose a card to play";
                break;
            case 3:
                messageText = "Player 1 has won the stake card! \n" +
                        "Please select a new stake card.";
                newStage = 0;
                showDialog = true;
                break;
            case 4:
                messageText ="Player 2 has won the stake card! \n"+
                        "Please select a new stake card.";
                newStage = 0;
                showDialog = true;
                break;
            case 5:
                messageText ="Nobody won the stake card! \n"+
                        "Please select a new stake card";
                newStage = 0;
                showDialog = true;
                break;
            case 6:
                messageText ="Player 1 has won the game! \n" +
                        "Please use the 'reset' button to restart the game.";
                newStage = 9;
                showDialog = true;
                break;
            case 7:
                messageText ="Player 2 has won the game! \n"+
                        "Please use the 'reset' button to restart the game.";
                newStage = 9;
                showDialog = true;
                break;
            case 8:
                messageText ="Stalemate! \n"+
                        "Please use the 'reset' button to restart the game.";
                newStage = 9;
                showDialog = true;
                break;

        }
        this.setText(messageText);
        if (showDialog){showMessage();}
        return newStage;
    }

    private void showMessage(){
        JOptionPane.showMessageDialog(game, messageText);
    }

}
