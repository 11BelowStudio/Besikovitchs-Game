package Packij;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameLogic {

    private GameLogic(){}

    //basically chooses what player 2 should do
    public static int chooseCardP2(int[][] gameHistory, int stakeCard){

        if (!validateStake(gameHistory,stakeCard)){
            throw new IllegalArgumentException("this is invalid :(");
            //validateStake checks gameHistory and stakeCard validity, returns false if either are invalid
        }

        //anything about 'opponent' refers to player 1
        //anything about 'bot' refers to player 2 (the bot, makes it easier to write this from the bot's perspective)

        int[] currentState = getGameState(gameHistory);

        int opponentScore = currentState[1];
        int botScore = currentState[2];
        int remainingScore = currentState[3];

        int lostPoints = 91 - remainingScore - opponentScore - botScore;
        //total score of any discarded stake cards

        int pointsToWin = (int) (46 - (Math.round(lostPoints/(double)2)));
        //how many points are needed to have over half the possible points (and therefore ensure a win)

        int opponentPointsToWin = pointsToWin - opponentScore;
        //how many points the opponent needs to reach the point where they win
        int botPointsToWin = pointsToWin - botScore;
        //how many points the bot needs to reach the point where it wins


        //and now taking note of what possible stakes/playable cards there are

        ArrayList<Integer> stakes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
        ArrayList<Integer> opponentCards = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
        ArrayList<Integer> botCards = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
        //removing any stakes/opponent cards/bot cards which have already been played
        for (int[] round : gameHistory) {
            stakes.remove(Integer.valueOf(round[0]));
            opponentCards.remove(Integer.valueOf(round[1]));
            botCards.remove(Integer.valueOf(round[2]));
        }


        int cardToPlay = 0;

        //finding the highest/lowest cards that the bot has
        int botMax = Collections.max(botCards);
        int botMin = Collections.min(botCards);

        //finding the highest/lowest cards that the opponent has
        int opponentMax = Collections.max(opponentCards);
        int opponentMin = Collections.min(opponentCards);


        //will record which cards will be an ez win for the bot, and which can't guarantee a win or a loss
        ArrayList<Integer> botEzWinCards = new ArrayList<>();
        ArrayList<Integer>  botOtherCards = new ArrayList<>();

        //ditto for the opponent
        ArrayList<Integer>  opponentEzWinCards = new ArrayList<>();
        ArrayList<Integer>  opponentOtherCards = new ArrayList<>();

        /*
        goes through the bot cards, trying to identify any cards which will be an instant win, or will not be an instant loss
            Card higher than the opponent's highest card:
                Will instantly win against the opponent, added to ezWinCards (because playing it = gg ez win)
            Card not lower than opponent's lowest card:
                Potentially useful, added to otherCards
        */
        int botOtherMax = 0;
        int botOther2ndMax = 0;

        for (int card : botCards) {
            if (card > opponentMax) {
                botEzWinCards.add(card);
            } else if (card > opponentMin){
                botOtherCards.add(card);
            }
        }
        //finds out the highest and 2nd highest 'otherCard', if there are enough otherCards to do that
        if (!botOtherCards.isEmpty()){
            botOtherMax = Collections.max(botOtherCards);
            if(botOtherCards.size() > 1){
                for (int otherCard: botOtherCards){
                    if (otherCard != botOtherMax && otherCard > botOther2ndMax){
                        botOther2ndMax = otherCard;
                    }
                }
            }
        }


        /*
        ditto but for the opponent instead
         */
        int opponentOtherMax = 0;
        int opponentOther2ndMax = 0;

        for (int card : opponentCards) {
            if (card > botMax) {
                opponentEzWinCards.add(card);
            } else if (card > botMin) {
                opponentOtherCards.add(card);
            }
        }
        if (!opponentOtherCards.isEmpty()){
            opponentOtherMax = Collections.max(opponentOtherCards);
            if(opponentOtherCards.size() > 1){
                for (int otherCard: opponentOtherCards){
                    if (otherCard != opponentOtherMax && otherCard > opponentOther2ndMax){
                        opponentOther2ndMax = otherCard;
                    }
                }
            }
        }


        //now looking at the stakes
        double stakeFutureAvg = 0;

        //upcoming stakes (aka current without what the current stake is)
        ArrayList<Integer> futureStakes = new ArrayList<>();
        futureStakes.addAll(stakes);
        futureStakes.remove(Integer.valueOf(stakeCard));


        //upcoming stakes which each player can win with, if they win this card
        ArrayList<Integer> botWinWin = new ArrayList<>();
        ArrayList<Integer> opponentWinWin = new ArrayList<>();

        //win followed by draw
        ArrayList<Integer> botWinDraw = new ArrayList<>();
        ArrayList<Integer> opponentWinDraw = new ArrayList<>();

        //upcoming stakes which each player can win with, if no-one wins this card
        ArrayList<Integer> botDrawWin = new ArrayList<>();
        ArrayList<Integer> opponentDrawWin = new ArrayList<>();

        //draw followed by draw
        ArrayList<Integer> botDrawDraw = new ArrayList<>();
        ArrayList<Integer> opponentDrawDraw = new ArrayList<>();


        //upcoming stakes which each player can win with if their enemy wins this card
        ArrayList<Integer> botLossWin = new ArrayList<>();
        ArrayList<Integer> opponentLossWin = new ArrayList<>();

        //upcoming stakes which result in a win if this round is lost but the next round is drawn
        ArrayList<Integer> botLossDraw = new ArrayList<>();
        ArrayList<Integer> opponentLossDraw = new ArrayList<>();

        //stakes which are better/worse than current
        ArrayList<Integer> betterStakes = new ArrayList<>();
        ArrayList<Integer> worseStakes = new ArrayList<>();

        //tries to perform some analysis
        for(int card: futureStakes){
            stakeFutureAvg += card;
            //finding upcoming bot win conditions
            if (botPointsToWin  - stakeCard - card<= 0){
                //wins if one of these cards are won + this round is won
                botWinWin.add(card);
                if (botPointsToWin - stakeCard - Math.round(card/2f) <= 0){
                    //wins if this round is won + one of these cards is drawn
                    botWinDraw.add(card);
                }
                if (botPointsToWin - Math.round(stakeCard/2f) - card <= 0){
                    //wins if one of these cards are won but this round is a draw
                    botDrawWin.add(card);
                    if (botPointsToWin - Math.round(stakeCard/2f) - Math.round(card/2f) <= 0){
                        //wins if this round + next round are drawn
                        botDrawDraw.add(card);
                    }
                    if (botPointsToWin - card<= 0) {
                        //wins if one of these cards are won, even if this round is lost
                        botLossWin.add(card);
                        if (botPointsToWin - Math.round(card/2f)<= 0) {
                            //wins if drawing one of these cards, even if this round is lost
                            botLossDraw.add(card);
                        }
                    }
                }
            }
            //finding upcoming opponent win conditions
            if (opponentPointsToWin -  stakeCard - card <= 0) {
                //wins if one of these cards are won + this round is won
                opponentWinWin.add(card);
                if (opponentPointsToWin - stakeCard - Math.round(card/2f) <= 0){
                    //wins if this round is won + one of these cards is drawn
                    opponentWinDraw.add(card);
                }
                if (opponentPointsToWin - Math.round(stakeCard / 2f) - card <= 0) {
                    //wins if one of these cards are won but this round is a draw
                    opponentDrawWin.add(card);
                    if (opponentPointsToWin - Math.round(stakeCard/2f) - Math.round(card/2f) <= 0){
                        //wins if this round + next round are drawn
                        opponentDrawDraw.add(card);
                    }
                    if (opponentPointsToWin - card <= 0) {
                        //wins if one of these cards are won, even if this round is lost
                        opponentLossWin.add(card);
                        if (opponentPointsToWin - Math.round(card/2f) <= 0) {
                            //wins if drawing one of these cards, even if this round is lost
                            opponentLossDraw.add(card);
                        }
                    }
                }
            }
            //working out which upcoming stakes are better/worse than the current stake
            if (stakeCard < card){
                betterStakes.add(card);
            }else{
                worseStakes.add(card);
            }
        }

        if (!futureStakes.isEmpty()){
            //finding average of the upcoming stakes
            stakeFutureAvg /= futureStakes.size();
        }

        //working out if these conditions for a win this turn are met or not
        boolean botCanWin = (botPointsToWin - stakeCard <= 0);
        boolean opponentCanWin = (opponentPointsToWin - stakeCard <= 0);
        boolean botWinIfLost = (botPointsToWin - Math.round(stakeCard/2f) <= 0);
        boolean opponentWinifLost = (opponentPointsToWin - Math.round(stakeCard/2f) <= 0);

        //and now some decisionmaking

        //will check the higher-priority cases first
        if (botCards.size() == 1 ||  botEzWinCards == botCards || opponentEzWinCards == opponentCards ){
            /*
            Just play the highest card if the choice doesn't matter
               Only one card left/all of one player's cards are a guaranteed win or loss
             */
            cardToPlay = botMin;
        } else if (!botEzWinCards.isEmpty()){
            //if the bot has one or more ez win card

            if (botCanWin || opponentCanWin || opponentWinifLost){
                //if the bot can immediately win/can prevent opponent from immediately winning, play ez win
                cardToPlay = botEzWinCards.get(0);

            } else if(botEzWinCards.size() > 1){
                //if the bot has 2 or more ez wins,

                if(futureStakes == botWinWin || futureStakes == opponentWinWin) {
                    //if a 'WinWin' condition is guaranteed for the bot or opponent, play ez win this round, to either secure the win/stop the opponent
                    cardToPlay = botEzWinCards.get(0);
                } else if((botOtherCards.isEmpty()) && (futureStakes == botDrawWin || futureStakes == botDrawDraw || futureStakes == opponentDrawWin || futureStakes == opponentDrawDraw)) {
                    //if there are no 'other' cards, but a 'DrawWin' or 'DrawDraw' victory will happen, play ez win to secure this win/stop opponent
                    cardToPlay = botEzWinCards.get(0);

                }
            } else if(futureStakes == botWinDraw || futureStakes == opponentWinDraw){
                //play ez win if a 'WinDraw' condition is guaranteed for bot or opponent (so a win is secured for bot/prevented for opponent)
                cardToPlay = botEzWinCards.get(0);

            } else if ((!botOtherCards.isEmpty()) && (futureStakes == botDrawWin || futureStakes == botDrawDraw || futureStakes == opponentDrawWin || futureStakes == opponentDrawDraw ||
                    futureStakes == opponentLossWin || futureStakes == opponentLossDraw)){
                //if there are 'other' cards, and a DrawWin/DrawDraw or an enemy LossWin or lossDraw, use the highest 'other', to try to draw this turn, but win next
                cardToPlay = botOtherMax;

            } else if(futureStakes == botLossWin || futureStakes == botLossDraw){
                //if the bot can win via LossWin or lossDraw, play lowest bot card
                //opponent likely to panic and play their highest card this turn, which would then give bot an ez win next round
                cardToPlay = botMin;

            }

        } else if (opponentEzWinCards.isEmpty()){
            //if the opponent also doesn't have any ez wins
            if(futureStakes == botLossWin || futureStakes == botLossDraw){
                //if bot can win if this round is lost, but next round are draws/wins, play lowest
                //opponent likely to panic and play their highest card this turn, which would then give bot an ez win next round
                cardToPlay = botMin;
            } else if(botWinIfLost || futureStakes == botDrawDraw){
                //if bot wins if this card is lost, or if this+next round are draws, play highest
                cardToPlay = botMax;
            } else if(stakeCard == Collections.max(stakes)){
                cardToPlay = botMax;
            }

        }

        if (cardToPlay == 0) {
            //will check these lower-priority cases if the cardToPlay hasn't been changed to a different value from its default value
            if ((botPointsToWin > opponentPointsToWin - stakeCard)) {
                //if the opponent would overtake the bot if the opponent won the stake
                if (!botEzWinCards.isEmpty()) {
                    //play an ez win if bot has ez win
                    cardToPlay = botEzWinCards.get(0);
                } else if (opponentEzWinCards.isEmpty()) {
                    //if there are no ez wins on the field
                    if (botCards.contains(stakeCard)) {
                        //bet the value of the stake card, if the bot can bet that card
                        cardToPlay = stakeCard;
                    } else {
                        //try to get the closest alternative to the stake card
                        cardToPlay = getClosestToStake((stakeCard < stakeFutureAvg), botCards, stakeCard);
                        //will lowball the opponent's bid if this stake is below average
                    }
                } else {
                    //if opponent has an ez win
                    if (botOtherCards.size() > 2) {
                        //if the bot has 3 or more 'other' cards, use the 2nd highest 'other'
                        //the opponent will probably either waste their ez win, or give you the card
                        cardToPlay = botOther2ndMax;
                    } else {
                        //otherwise, bot uses lowest, to bait the opponent wasting one of their ez wins
                        cardToPlay = botMin;
                    }
                }

            } else if ((opponentPointsToWin > botPointsToWin - stakeCard)) {
                //if the bot will overtake the opponent if the bot wins this card
                if (!botEzWinCards.isEmpty()) {
                    //play ez win if the bot has one
                    cardToPlay = botEzWinCards.get(0);
                } else if (opponentEzWinCards.isEmpty()) {
                    //if there are no ez wins in play
                    if (botCards.contains(stakeCard)) {
                        //bet the value of the stake card, if the bot can bet that card
                        cardToPlay = stakeCard;
                    } else {
                        //try to get the closest alternative to the stake card
                        cardToPlay = getClosestToStake((stakeCard < stakeFutureAvg), botCards, stakeCard);
                        //will lowball the opponent's bid if this stake is below average, otherwise will highball bid
                    }
                } else {
                    //if the opponent has at least one ez win
                    if (botOtherCards.size() > 1) {
                        //2 or more bot 'other' cards
                        if (stakeCard < stakeFutureAvg) {
                            //below average stake = lowball
                            cardToPlay = Collections.min(botOtherCards);
                        } else {
                            //above average: 2nd highest other
                            cardToPlay = botOther2ndMax;
                        }
                    } else {
                        //big lowball if 1 or fewer 'other' cards
                        cardToPlay = botMin;
                    }
                }


            } else if (betterStakes.isEmpty() && botEzWinCards.size() > 1) {
                //if there are no better stake cards, and bot has at least 2 ez wins, play ez win
                cardToPlay = botEzWinCards.get(0);
            } else if (worseStakes.isEmpty()) {
                //bet the lowest unused card if there are no worse stake cards
                cardToPlay = botMin;
            } else {
                //if none of the prior conditions are satisfied
                if (botCards.contains(stakeCard)) {
                    //bet the value of the stake card, if the bot can bet that card
                    if (stakeCard != botMax) {
                        cardToPlay = botCards.get(botCards.indexOf(stakeCard));
                    } else{
                        cardToPlay = stakeCard;
                    }
                } else {
                    //try to get the closest alternative to the stake card
                    cardToPlay = getClosestToStake(false,botCards,stakeCard);
                }
            }
        }

        return cardToPlay;


    }

    private static int getClosestToStake(boolean lowball, ArrayList<Integer> botCards, int stakeCard){
        //returns the closest possible alternative to the current stake from the cards that the bot has at its disposal
        //will either return the lowest or the highest alternative, depending on the situation
        int cardToPlay = 0;
        int aboveStake = botCards.get(0);
        int belowStake = botCards.get(1);
        for (int i = 1; i < botCards.size(); i++) {
            if (botCards.get(i - 1) < stakeCard && botCards.get(i) > stakeCard) {
                //if stake is above botCards(i-1) and below botCards(i), these are set to aboveStake and belowStake
                aboveStake = botCards.get(i);
                belowStake = botCards.get(i - 1);
                break;
            }
        }
        //use the lower one if trying to lowball the opponent's bid, higher otherwise
        if (lowball) {
            cardToPlay = belowStake;
        } else {
            cardToPlay = aboveStake;
        }
        return cardToPlay;
    }


    /*
    this version is called by Lowe.BetterGUI.GameFrame, when performing any p2 card choosing
    it'll pretty much turn the 2d arraylist verion of historyArrayList into a 2d array,
    calling chooseCardP2 with that and the stake card, and then returning the result of that
    */

    public static int chooseCardP2(ArrayList<ArrayList<Integer>> historyArrayList, int stakeCard){
        //pretty much turns the 2d arrayList argument into a 2d array
        int[][] arrayBut2d = arrayListInteger2dTo2dArray(historyArrayList);
        //and then calls chooseCardP2 with that (and the stakecard)
        return chooseCardP2(arrayBut2d,stakeCard);
    }

    //this is the thing for calculating the score of player 1 with a gamehistory
    public static int calcPlayer1Score(int[][] gameHistory){
        if (!validateHistory(gameHistory)){
            throw new IllegalArgumentException("this game history is invalid :(");
            //validateHistory checks gameHistory validity, returns false (and throws exception) if it's invalid
        }

        int score = 0;
        for(int i = 0; i < gameHistory.length; i++){
            //checks each turn of gameHistory

            if (gameHistory[i][1] > gameHistory[i][2]){
                score += gameHistory[i][0];
                //stake for that turn only added to player 1's score if player1 played a higher card than player2.
            }
        }

        return score;

    }

    private static boolean validateHistory(int [][] gameHistory){
        /* gameHistory is of size [k][3]
            [[stake],[player1],[player2]]
            k: 0-12 inclusive
         */
        if (gameHistory.length == 0){
            return true;
            //no history: always valid
        } else if (gameHistory.length > 13){
            System.out.println("too big");
            return false;
            //game can only last 13 turns
        }

        //keeping track of all prior stakes/player1/player2 card history
        ArrayList<Integer> stakes = new ArrayList<>();
        ArrayList<Integer> player1 = new ArrayList<>();
        ArrayList<Integer> player2 = new ArrayList<>();

        //System.out.println(Arrays.deepToString(gameHistory));

        for(int i = 0; i < gameHistory.length; i++){
            if (gameHistory[i].length != 3){
                System.out.println("inner array " + i + " too big");
                return false;
                //need 3 values for each turn: any different length is invalid
            } else{
                //will check each card, seeing if they are all either below 1, above 13, or already present in 'stakes','player1', or 'player2'
                //returns false if any of these conditions are true

                if (gameHistory[i][0] < 1 || gameHistory[i][0] > 13 || stakes.contains(gameHistory[i][0])){
                    System.out.println("stake " + i + " is weird");
                    if(stakes.contains(gameHistory[i][0])) {
                        System.out.println(stakes.indexOf(gameHistory[i][0]));
                    }else{
                        System.out.println(gameHistory[i][0] + " is a bad size");
                    }
                    return false;
                }
                if (gameHistory[i][1] < 1 || gameHistory[i][1] > 13 || player1.contains(gameHistory[i][1])){
                    System.out.println("p1 bid " + i + " is weird");
                    return false;
                }
                if (gameHistory[i][2]< 1 || gameHistory[i][2] > 13|| player2.contains(gameHistory[i][2])) {
                    System.out.println("p2 bid " + i + " is weird");
                    return false;
                }
                //values added to stakes/player1/player2 if none of them are invalid
                stakes.add(gameHistory[i][0]);
                player1.add(gameHistory[i][1]);
                player2.add(gameHistory[i][2]);
            }
        }

        return true; //it's valid if it hasn't been found to be invalid
    }

    private static boolean validateStake(int[][] gameHistory, int stakeCard){
        //checks if the game history as a whole is valid before checking the stake card
        if (validateHistory(gameHistory)){

            //records which stakes have already been played so far
            ArrayList<Integer> stakes = new ArrayList<>();
            for(int i = 0; i < gameHistory.length; i++){
                if (stakes.contains(gameHistory[i][0])) {
                    System.out.println("Stake " + i + "is duplicate");
                    return false;
                    //invalid if there are duplicate stakes
                } else {
                    stakes.add(gameHistory[i][0]);
                }
            }
            if (!stakes.contains(stakeCard)){
                //returns 'true' (everything is valid) if the stakeCard is not already present.
                return true;
            }
        }
        //returns 'false' if gamehistory is invalid, or if the stakeCard is a duplicate of a stake which has been played already
        System.out.println("gamehistory invalid, or new stakeCard is duplicate");
        return false;
    }

    static int[] getGameState(int[][] gameHistory){
        /*
        Will evaluate the gameHistory to evaluate what state the game is in
        Outputs:
            Index 0:
                integer to represent the current game state
                    0: nothing has happened
                    3: player 1 has won the last stake card
                    4: player 2 has won the last stake card
                    5: no-one won the last stake card
                    6: player 1 has won
                    7: player 2 has won
                    8: stalemate
            Index 1:
                player 1's score
            Index 2:
                player 2's score
            Index 3:
                score obtainable from currently unused stake cards
         */

        int[] gameState = new int[4];
        if (gameHistory.length == 0){
            gameState = new int[]{0, 0, 0,91};
            return gameState;
        }

        int player1Score = calcPlayer1Score(gameHistory);
        int player2Score;
        int remainingPotentialScore = 91;
        //max score if you somehow win all 13 cards is 91: will be decreased for every stake card which has been played

        int totalScore = 0;

        //swaps the player 1 and player 2 scores, and finds the eventual remaining potential score

        int[][] swappedArray = new int[gameHistory.length][3];

        for(int i = 0; i < gameHistory.length; i++){
            remainingPotentialScore = remainingPotentialScore - gameHistory[i][0];
            totalScore = totalScore + gameHistory[i][0];
            swappedArray[i][0] = gameHistory[i][0];
            swappedArray[i][1] = gameHistory[i][2];
            swappedArray[i][2] = gameHistory[i][1];
        }


        player2Score = calcPlayer1Score(swappedArray); //player 2's score can be worked out like player 1's

        gameState[1] = player1Score;
        gameState[2] = player2Score;
        gameState[3] = remainingPotentialScore;


        if (remainingPotentialScore == 0 && player1Score == player2Score){
            //stalemate if both players have the same score, and there are no points left to earn
            gameState[0] = 8;
        } else if (player1Score > player2Score + remainingPotentialScore){
            //if player1's score is too large for player2 to ever beat, player1 has already won
            gameState[0] = 6;
        } else if(player2Score > player1Score + remainingPotentialScore){
            //ditto for player 2
            gameState[0] = 7;
        } else{
            //if the game isn't over (remember that p1 and p2's positions in gameHistory were swapped)
            if(gameHistory[gameHistory.length-1][1] > gameHistory[gameHistory.length-1][2]){
                //if player 1 played a larger bid for the last stake card than player 1
                gameState[0] = 3;
            } else if(gameHistory[gameHistory.length-1][1] < gameHistory[gameHistory.length-1][2]){
                //if player 2's bid was larger
                gameState[0] = 4;
            } else{
                gameState[0] = 5;
                //nobody won the last stake card if no-one's bid was larger
            }
        }

        return gameState;

    }

    //this is the version of getGameState called by GameFrame
    //essentially takes a 2d arraylist <Integer> version of the int[][]gameHistory argument
    //and uses that to call getGameState with the 2d array
    public static int[] getGameState(ArrayList<ArrayList<Integer>> historyArrayList){
        //essentially converts the ArrayList version of gameHistory to a 2d array
        int[][] arrayBut2d = arrayListInteger2dTo2dArray(historyArrayList);
        //and then calls getGameState with that
        return getGameState(arrayBut2d);
    }

    //pretty much just converts a 2d ArrayList of integers into a 2d array of integers
    //used within the calls to getGameState and chooseCardP2 from GameFrame
    private static int[][] arrayListInteger2dTo2dArray(ArrayList<ArrayList<Integer>> theArrayList) {
        int[][] arrayBut2d = new int[theArrayList.size()][3];
        for (int i = 0; i < theArrayList.size(); i++){
            arrayBut2d[i][0] = theArrayList.get(i).get(0);
            arrayBut2d[i][1] = theArrayList.get(i).get(1);
            arrayBut2d[i][2] = theArrayList.get(i).get(2);
        }
        return arrayBut2d;
    }


}
