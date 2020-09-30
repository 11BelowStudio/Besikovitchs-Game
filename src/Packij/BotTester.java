package Packij;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 this tests the bot basically
 */
public class BotTester {

    public static void main(String[] args) {

        // build a 'suit' of 13 cards
        int[] possibleCards = new int[13];
        ArrayList<Integer> cardList = new ArrayList<>();

        for (int i=0; i<13; i++) {
            possibleCards[i] = i+1;
            cardList.add(possibleCards[i]);
        }

        ArrayList<Integer> ascendingList = new ArrayList<>(cardList);
        //kept in order going from lowest to highest (to allow for a thing where the lowest possible card is put against the bot)

        ArrayList<Integer> enemyList = new ArrayList<>(cardList);

        ArrayList<Integer> reverseList = new ArrayList<>(cardList);
        Collections.reverse(reverseList); //list of the cards but it's in reverse (so highest possible can be put against the bot)


        //keeping track of winrates
        int decisionWins = 0;
        int stakeBeatsDecision = 0;
        int decisionBeatsStake = 0;
        int lowestWins = 0;
        int lowestLoses = 0;
        int highestWins = 0;
        int highestLoses = 0;


        //and now runs about 5000 simulations of games
        for (int i = 0; i < 5000; i++) {

            Collections.shuffle(enemyList); //shuffling the list for the rng enemy

            Collections.shuffle(cardList); //shuffling the stake cards

            //creating arrays to keep track of game histories
            int[][] history = new int[13][3]; //bot vs rng
            int[][]decisionVsStakeHistory = new int[13][3]; //bot vs stake
            int[][]lowestHistory = new int[13][3]; //bot vs lowest bid
            int[][]highestHistory = new int[13][3]; //bot vs highest bid

            //long startTime = System.nanoTime();

            for (int round = 0; round < possibleCards.length; round++) {

                int stakeCard = cardList.get(round);

                // make a copy of the game histories so the bot can do its thing
                int[][] roundHistory = Arrays.copyOf(history, round); //history for bot vs rng
                int[][] decisionVsStakeRoundHistory = Arrays.copyOf(decisionVsStakeHistory,round); //stake vs rng
                int[][] lowestRoundHistory = Arrays.copyOf(lowestHistory,round); //bot vs lowest
                int[][] highestRoundHistory = Arrays.copyOf(highestHistory,round); //bot vs highest


                int botChoice = GameLogic.chooseCardP2(roundHistory, stakeCard); //bot makes its decision vs rng
                int decisionVsStakeChoice = GameLogic.chooseCardP2(decisionVsStakeRoundHistory, stakeCard); //bot decides vs stake
                int lowestDecision = GameLogic.chooseCardP2(lowestRoundHistory,stakeCard); //bot decides vs lowest
                int highestDecision = GameLogic.chooseCardP2(highestRoundHistory,stakeCard); //bot decides vs highest

                int enemyChoice = enemyList.get(round); //getting the random choice


                // adding to the game histories for these strategies
                history[round] = new int[]{stakeCard, enemyChoice, botChoice}; //bot vs rng
                decisionVsStakeHistory[round]= new int[]{stakeCard,stakeCard,decisionVsStakeChoice}; //bot vs stake
                lowestHistory[round] = new int[]{stakeCard,ascendingList.get(round),lowestDecision}; //bot vs lowest
                highestHistory[round] = new int[]{stakeCard,reverseList.get(round),highestDecision}; //bot vs highest

            }
            //long endTime = System.nanoTime();

            //long totalTime = (endTime - startTime) / 1000000;

            //double timeSeconds = totalTime / (double) 1000;

            System.out.println("\nAnalysis of final game state: ");
            int[] gameInfo = GameLogic.getGameState(history);
            System.out.println("RNG (vs bot): " + gameInfo[1]);
            System.out.println("Bot (vs RNG): " + gameInfo[2]);

            if (gameInfo[2] > gameInfo[1]){
                decisionWins++;
            }



            int[] decisionVsStakeGameInfo = GameLogic.getGameState(decisionVsStakeHistory);
            System.out.println("Stake (vs bot): " + decisionVsStakeGameInfo[1]);
            System.out.println("Bot (vs stake): " + decisionVsStakeGameInfo[2]);

            if (decisionVsStakeGameInfo[1] > decisionVsStakeGameInfo[2]){
                stakeBeatsDecision++;
            } else if(decisionVsStakeGameInfo[1] < decisionVsStakeGameInfo[2]){
                decisionBeatsStake++;
            }

            int[] lowestGameInfo = GameLogic.getGameState(lowestHistory);
            System.out.println("Lowest (vs bot): " + lowestGameInfo[1]);
            System.out.println("Bot (vs lowest): " + lowestGameInfo[2]);

            if (lowestGameInfo[1] > lowestGameInfo[2]){
                lowestWins++;
            } else if(lowestGameInfo[1] <lowestGameInfo[2]){
                lowestLoses++;
            }

            int[] highestGameInfo = GameLogic.getGameState(highestHistory);
            System.out.println("Highest (vs bot): " + highestGameInfo[1]);
            System.out.println("Bot (vs lowest): " + highestGameInfo[2]);

            if (highestGameInfo[1] > highestGameInfo[2]){
                highestWins++;
            } else if(highestGameInfo[1] <highestGameInfo[2]){
                highestLoses++;
            }


            //System.out.println("\n\nThis took roughly " + totalTime + " milliseconds to run the simulation");
            //System.out.println("or about " + timeSeconds + " seconds");
        }

        System.out.println("\n\n\nBot wins vs rng: " + decisionWins);
        //System.out.println("Wins via rng: " + rngWins);
        System.out.println("\nStake wins vs bot: " + stakeBeatsDecision);
        System.out.println("Bot wins vs stake: " + decisionBeatsStake);
        System.out.println("\nLowest wins vs bot: " + lowestWins);
        System.out.println("Bot wins vs lowest: " + lowestLoses);
        System.out.println("\nHighest wins vs bot: " +highestWins);
        System.out.println("Bot wins vs highest: " + highestLoses);

    }

}
