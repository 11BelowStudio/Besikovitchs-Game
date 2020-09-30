package Packij.BetterGUI;

import Packij.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class GameFrame extends JFrame {


    InstructionLabel instructionLabel;
    PanelForCardPanels cardPanels;
    CardPanel stakePanel;
    PlayerCardPanel player1Panel;
    BotCardPanel player2Panel;

    private JPanel resetPanel;

    private JButton resetButton;



    private int[] stateArray;

    private int gameState;
    private int turnCount;

    private int player1Score;
    private int player2Score;

    public MouseHandler mouseHandler;

    private ArrayList<ArrayList<Integer>> turnHistoryArrayList;
    private ArrayList<Integer> currentTurnArrayList;


    int currentStake;
    int currentPlayer1;
    int currentPlayer2;



    public GameFrame(){
        this.setTitle("Besikovitch's Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());



        mouseHandler = new MouseHandler();

        setupPanels();

        this.setVisible(true);


        this.setSize(800,600);
        this.setPreferredSize(this.getSize());

        this.revalidate();
        this.pack();

        startGame();

    }

    private void setupPanels(){

        //setting up the instruction label
        if (instructionLabel!=null){
            this.remove(instructionLabel);
            instructionLabel = null;
        }
        instructionLabel = new InstructionLabel(this);
        this.add(instructionLabel,BorderLayout.NORTH);

        if(cardPanels!=null){
            this.remove(cardPanels);
            cardPanels = null;
            stakePanel = null;
            player1Panel = null;
            player2Panel = null;
        }
        //creates the panels to hold the cards
        stakePanel = new CardPanel(this,0);

        player1Panel = new PlayerCardPanel(this,1);

        player2Panel = new BotCardPanel(this,2);

        //creates the panel holding the aforementioned panels
        cardPanels = new PanelForCardPanels(this);

        //adds that to the frame
        this.add(cardPanels,BorderLayout.CENTER);

        //and now the restart button
        if (resetPanel != null){
            this.remove(resetPanel);
            resetPanel = null;
        }
        resetPanel = new JPanel();
        resetButton = new JButton("Restart game");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
            //restarts the game when pressed
        });

        resetPanel.add(resetButton);

        this.add(resetPanel,BorderLayout.SOUTH);



    }

    private void resetGame(){
        setupPanels();
        this.revalidate();
        this.pack();
        startGame();

    }

    private void startGame(){
        turnHistoryArrayList = new ArrayList<>();
        currentTurnArrayList = new ArrayList<>();
        this.stateArray = new int[3];
        gameState = 0;
        turnCount = 0;


    }

    private void startTurn(){
        gameState = 0;
        currentTurnArrayList = new ArrayList<>();
        stakePanel.resetValue();
        player1Panel.resetValue();
        player2Panel.resetValue();
    }

    void cardChosen(int value){
        boolean startNewTurn = false;
        boolean avoidDuplicateDialog = false;
        /*
        What each gameState value means:
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
        switch (gameState){
            case 0:
                stakePanel.cardChosen(value);
                //gameHistory[turnCount][0] = value;
                currentTurnArrayList.add(value);
                gameState = 1;
                break;
            case 1:
                player1Panel.cardChosen(value);
                //gameHistory[turnCount][1] = value;
                currentTurnArrayList.add(value);
                gameState = 2;
                avoidDuplicateDialog = true;
                cardChosen(GameLogic.chooseCardP2(turnHistoryArrayList,currentTurnArrayList.get(0)));
                break;
            case 2:
                player2Panel.cardChosen(value);
                player2Panel.autoSelectCard(value);
                currentTurnArrayList.add(value);
                turnHistoryArrayList.add(currentTurnArrayList);
                stateArray = GameLogic.getGameState(turnHistoryArrayList);
                gameState = stateArray[0];
                player1Score = stateArray[1];
                player2Score = stateArray[2];

                cardChosen(currentTurnArrayList.get(0)); //stake card used to update the state
                avoidDuplicateDialog = true;
                //that was a recursive call, so ofc there's going to be a second dialog box popping up when it re-enters this call
                //so imma just avoid the second dialog box completely
                break;
            case 3:
                //p1 wins stake card
                player1Panel.addWonCard(value);
                startNewTurn = true;
                break;
            case 4:
                //p2 wins stake card
                player2Panel.addWonCard(value);
                startNewTurn = true;
                break;
            case 5:
                //no-one wins stake
                startNewTurn = true;
                break;
            case 6:
                //p1 wins game
                player1Panel.addWonCard(value);
                break;
            case 7:
                //p2 wins game
                player2Panel.addWonCard(value);
                break;
            case 8:
                //stalemate
                break;

        }
        if (!avoidDuplicateDialog){instructionLabel.updateLabel(gameState);}
        //this is here to prevent the dialog box being shown twice at the end of the game basically
        if (startNewTurn){ startTurn(); }
    }



    int getGameState(){
        return gameState;
    }

    public static void main(String args[]){
        GameFrame game = new GameFrame();
    }
}
