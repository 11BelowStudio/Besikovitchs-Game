package test;

//This is pretty much copied from the sample code


import Packij.GameLogic;

//#START OF INSERTED TEXT (SOURCE: https://lms.walton-rivers.uk/ce291/)

import org.junit.Test;

import static junit.framework.TestCase.*;




/**
 * These aren't the test cases I'm testing you on 'for real' but at least you can use them to check your code works
 * to the brief to some extent.
 */


public class TestExceptions {

    @Test(expected=IllegalArgumentException.class)
    public void testP1PlayedTwice() {

        // this ins't allowed as player 1 has played a two twice.
        int[][] testCase = { {1, 2, 3}, {2, 2, 4} };
        int result = GameLogic.chooseCardP2(testCase, 3);

        // yeah, should have thrown an IllegalArgumentException
        fail();
    }

    @Test
    public void testP1Score() {

        int[][] testCase = { {1, 12, 3}, {8, 11, 4}, {3, 1, 11} };
        int result = GameLogic.calcPlayer1Score(testCase);

        // won round 1 (1), won round 2 (8), lost round 3 (3), score is 1 + 8
        assertEquals(9, result);
    }

}

//#END OF INSERTED TEXT