package comp1110.ass2;

/**
 * Created by tom on 15/8/16.
 */
public class Piece {
    /**
     * Implemented by Sicheng Xu
     * Use Pieceplacement to generate Piece
     * Each piece consists of three parts and each part consists of a point and a unit
     * a point is used to see the position of that part and check whether is out of bound or overlap with other pieces
     * when two points are in the same position, their units are used to check it is valid or not
     * */
    private String piecePlacement;

    private char[] data;
    private Point origin, left, right;
    private Unit originUnit, leftUnit, rightUnit;
    private int orientation;
    private int shapeOffset; // 3, 2, 1;

    public Piece(String piecePlacement) {
        if (!LinkGame.isPiecePlacementWellFormed(piecePlacement)) throw new IllegalArgumentException();

        data = piecePlacement.toCharArray();

        origin = new Point(data[0]);
        orientation = data[2] - 'A';
        left = new Point(origin, orientation % 6);

        if (data[1] >= 'A' && data[1] <= 'C') {
            shapeOffset = 3;
        } else if (data[1] >= 'D' && data[1] <= 'H') {
            shapeOffset = 2;
        } else if (data[1] >= 'I' && data[1] <= 'L') {
            shapeOffset = 1;
        } else {
            throw new IllegalArgumentException();
        }
        int rightOffset = orientation > 5 ? (orientation % 6) - shapeOffset : (orientation % 6) + shapeOffset;
        right = new Point(origin, (rightOffset + 6) % 6);

        String unitPlacement = piecePlacement.substring(1);
        leftUnit = Unit.getUnitFromPlacement(unitPlacement, 0);
        originUnit = Unit.getUnitFromPlacement(unitPlacement, 1);
        rightUnit = Unit.getUnitFromPlacement(unitPlacement, 2);

        this.piecePlacement = piecePlacement;
    }

    /**
     * Check if any point is out of bound
     * If yes, then the piece is out of bound
     * */
    public boolean isOutOfBound() {
        return left.isOutOfBound() || origin.isOutOfBound() || right.isOutOfBound();
    }

    /**
     * @param which 0->left, 1->origin, 2->right
     */
    public Point getPointAt(int which) {
        switch (which) {
            case 0:
                return left;
            case 1:
                return origin;
            case 2:
                return right;
        }
        throw new IllegalArgumentException();
    }

    public char getShapeRaw() {
        return data[1];
    }

    public int getOrientation() {
        return orientation;
    }

    public String getPiecePlacement() {
        return piecePlacement;
    }

    /**
     * @param which 0->left, 1->origin, 2->right
     */
    public Unit getUnitAt(int which) {
        switch (which) {
            case 0:
                return leftUnit;
            case 1:
                return originUnit;
            case 2:
                return rightUnit;
        }
        throw new IllegalArgumentException();
    }

    public int[] getPegs() {
        int[] result = new int[3];
        result[0] = left.getPeg();
        result[1] = origin.getPeg();
        result[2] = right.getPeg();
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Piece)) {
            return false;
        }
        Piece other = (Piece) obj;
        return piecePlacement.equals(other.piecePlacement);
    }
}
