package comp1110.ass2;


/**
 * Created by tom on 15/8/16.
 */
public class Unit {
    /**
     * Implemented by JIAYI CHEN
     * the unit of a piece, whether is ring or ball
     * this class is embedded into piece
     * Since unit can either be ball or ring, simply we can use boolean to represent unit
     */
    private boolean ball = false;
    private boolean[] connection = new boolean[]{
            /**
             * Similarly, there are six orientations for the connection
             * and for each orientation, it either has connection or doesn't have connection
             * So, here we can use boolean*/
            false,
            false,
            false,
            false,
            false,
            false,
    };
    private int connectionSide; // 0-5 means 6 different orientations
    private boolean hasTwoConnection = false;   // Similar with above, use boolean to represent the unit has two connections or not

    /**
     *      Implemented by JIAYI CHEN
     * @param which 0->left, 1->origin, 2->right
     */
    public static Unit getUnitFromPlacement(String unitPlacement, int which) {
        if (unitPlacement.length() != 2) throw new IllegalArgumentException();
        int shape = unitPlacement.charAt(0) - 'A';
        Unit result = new Unit(getFirstParam(shape, which), secondParms[shape][which], getThirdParam(shape, which));
        int orientation = unitPlacement.charAt(1) - 'A';
        result.change(orientation % 6, orientation > 5);
        return result;
    }

    /**
     * Implemented by JIAYI CHEN
     * @param shape -> which piece, from A to L (0-11)
     *        which -> what the index, that is left, origin, right
     * Notice that different pieces can share the same property here, so we can just group them up
     * */
    private static boolean getFirstParam(int shape, int witch) {
        if (shape == 0 || shape == 6 || shape == 10) {
            return firstParms[0][witch];
        } else if (shape == 8 || shape == 9) {
            return firstParms[1][witch];
        } else if (shape == 7) {
            return firstParms[2][witch];
        } else {
            return firstParms[3][witch];
        }
    }
    /**
     * @param shape -> which piece, from A to L (0-11)
     *        which -> what the index, that is left, origin, right
     * Notice that different pieces can share the same property here, so we can just group them up
     * */

    private static boolean getThirdParam(int shape, int witch) {
        if (shape == 0 || shape == 8 || shape == 9) {
            return thirdParms[0][witch];
        } else {
            return thirdParms[1][witch];
        }
    }

    /**
     *For the firstParms, there are intrinsically four different groups
     *So, given the shape and index, the method getFirstParam can return the result that whether is ball or ring
     * */
    private static boolean[][] firstParms = new boolean[][]{
            {true, false, true},
            {true, true, false},
            {false, false, false},
            {true, false, false},
    };


    /**
     * Similar with the firstParms, here we store the connectionsides
     * */
    private static int[][] secondParms = new int[][]{
            {3, 1, 0},
            {3, -1, 2},
            {3, -1, 1},
            {3, -1, 4},
            {3, -1, 0},
            {3, -1, 1},
            {3, 5, 5},
            {1, -1, 2},
            {3, 1, 1},
            {3, 1, 0},
            {3, 4, 4},
            {3, 4, 3},
    };

    /**
     * Similar with the firstParms and seconParms
     * */
    private static boolean[][] thirdParms = new boolean[][]{
            {false, true, false},
            {false, false, false},
    };

    /**
     * Implemented by JIAYI CHEN
     * isBall(FirstParam): the unit is ball or ring
     * connectionSide(secondParms): Where is the connection
     * hasTwoConnection(ThirdParam): whether has two connections or not
     * */
    public Unit(boolean isBall, int connectionSide, boolean hasTwoConnection) {
        ball = isBall;
        this.connectionSide = connectionSide;
        if (connectionSide == -1) return; // is ring

        this.hasTwoConnection = hasTwoConnection;
        connection[connectionSide] = true;
        if (hasTwoConnection) {
            connection[(connectionSide + 1) % 6] = true;
        }
    }

    public boolean isBall() {
        return ball;
    }

    public boolean isRing() {
        if (ball) return false;
        for (boolean b : connection) {
            if (b) {
                return false;
            }
        }
        return true;
    }

    public void change(int rotate, boolean isNeedReflection) {
        if (connectionSide == -1) return;
        if (rotate == 0 && !isNeedReflection) return;

        connection = new boolean[]{ // reset
                false,
                false,
                false,
                false,
                false,
                false,
        };

        connectionSide = isNeedReflection ? (6 - connectionSide + rotate) % 6 : (connectionSide + rotate) % 6;
        connection[connectionSide] = true;
        if (hasTwoConnection) {
            connection[(connectionSide + 1) % 6] = true;
        }
    }
    /**
     * match with another unit
     * it only returns true if one is ball and other is ring and they have the same connectionsides
     * */
    public boolean match(Unit other) {
        if (isRing() || other.isRing()) return false;
        if (isBall() == other.isBall()) return false;
        for (int i = 0; i < 6; i++) {
            if (connection[i] && other.connection[i]) {
                if (hasTwoConnection && other.hasTwoConnection) {
                    return connection[i + 1] == connection[i + 1];
                } else if (!hasTwoConnection && !other.hasTwoConnection) {
                    return true;
                } else {
                    if (hasTwoConnection) {
                        return !isBall();
                    } else {
                        return !other.isBall();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "isBall = " + ball + " connectionSide = " + connectionSide + " hasTwoConnection = " + hasTwoConnection;
    }
}
