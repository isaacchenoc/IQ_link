package comp1110.ass2;

/**
 * Created by tom on 16/9/11.
 */
public class Point {
    /**
     * Implemented by Tom Xu
     Build up a coordinate system for the board game,
     which is particularly useful when deal with the pieces on the edge
     */
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(char peg) {
        if (peg < 'A' || peg > 'X') throw new IllegalArgumentException();
        int distance = peg - 'A';
        y = distance / 6;
        x = distance % 6 * 2 + y % 2;
    }

    public Point(Point origin, int direction) {
        switch (direction) {
            case 0:
                x = origin.x - 2;
                y = origin.y;
                break;
            case 1:
                x = origin.x - 1;
                y = origin.y - 1;
                break;
            case 2:
                x = origin.x + 1;
                y = origin.y - 1;
                break;
            case 3:
                x = origin.x + 2;
                y = origin.y;
                break;
            case 4:
                x = origin.x + 1;
                y = origin.y + 1;
                break;
            case 5:
                x = origin.x - 1;
                y = origin.y + 1;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getPeg() {
        if (isOutOfBound()) {
            return -1;
        }
        return y * 6 + x / 2;
    }

    public char getPegRaw() {
        int peg = getPeg();
        if (peg != -1) {
            return (char) (peg + 'A');
        }
        return (char) -1;
    }

    /**
     * Since it is a coordinate system, we can tell it is out of bound when x or y is not in the range
     * */
    public boolean isOutOfBound() {
        return x < 0 || x > 11 || y < 0 || y > 3;
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + "y";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Point)) {
            return false;
        }
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }
}
