package comp1110.ass2;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tom on 16/9/11.
 */
public class PieceList {
    /**
     * Since each piece of a placement must be distinct,
     * it is good to use set to represent the placement string
     */
    private HashSet<Piece> pieces;

    public PieceList() {
        pieces = new HashSet<>();
    }

    /**
     * @return is legal
     */
    public boolean add(Piece piece) {
        if (pieces.size() == 0) {
            return !piece.isOutOfBound() && pieces.add(piece);
        }

        for (Piece i : pieces) {
            if (!isValid(piece, i)) {
                return false;
            }
        }
        return pieces.add(piece);
    }

    public void addAll(PieceList list) {
        pieces.addAll(list.getData());
    }

    /**
     * Implemented by JIAYI CHEN
     * Match two pieces
     * return true if these two pieces are not overlapped, that is valid
     **/
    private boolean isValid(Piece a, Piece b) {
        if (a.getShapeRaw() == b.getShapeRaw()) {
            return false;
        }
        int[] aPegs = a.getPegs();
        int[] bPegs = b.getPegs();
        if (a.isOutOfBound() || b.isOutOfBound()) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (aPegs[i] == bPegs[j]) {
                    if (!a.getUnitAt(i).match(b.getUnitAt(j))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean remove(Piece piece) {
        return pieces.remove(piece);
    }

    public void clear() {
        pieces.clear();
    }

    public Set<Piece> getData() {
        return pieces;
    }
}
