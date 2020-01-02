package comp1110.ass2;

import org.junit.Test;

import java.util.Random;

import java.util.Arrays;

import static comp1110.ass2.TestUtility.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by tom(Sicheng Xu) on 12/9/16.
 */


public class PointTest {

    @Test
    public void rightCoordinate(){
        Random r = new Random();
        int i = r.nextInt(PEGS);
        char test = (char) ('A' + i);
        Point randPoint = new Point(test);
        int[] pCoord = {randPoint.y, randPoint.x};
        assertTrue("Coordinate of '" + test + "' is expected to be " + Arrays.toString(coord.get(test)) + " but got " + "" +Arrays.toString(pCoord),
                    Arrays.equals(pCoord, coord.get(test)));
    }


}
