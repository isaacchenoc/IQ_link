package comp1110.ass2;


import comp1110.ass2.gui.BasicViewSize;

import java.util.*;

/**
 * This class provides the text interface for the Link Game
 *
 * The game is based directly on Smart Games' IQ-Link game
 * (http://www.smartgames.eu/en/smartgames/iq-link)
 */
public class LinkGame implements BasicViewSize{

    /**
     * Determine whether a piece placement is well-formed according to the following:
     * - it consists of exactly three characters
     * - the first character is in the range A .. X  -- Origin
     * - the second character is in the range A .. L -- Piece
     * - the third character is in the range A .. F if the second character is A, otherwise
     *   in the range A .. L                         -- Orientation
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    public static boolean isPiecePlacementWellFormed(String piecePlacement) {
        /**This method is implement by Yuanxin Ye*/
        return piecePlacement.length() == 3 && piecePlacement.charAt(0) <= 'X' && piecePlacement.charAt(0) >= 'A'
                && piecePlacement.charAt(1) <= 'L' && piecePlacement.charAt(0) >= 'A'
                && ((piecePlacement.charAt(1) == 'A' && piecePlacement.charAt(2) <= 'F') || (piecePlacement.charAt(1) != 'A'
                && piecePlacement.charAt(2) <= 'L'));
    }

    /**
     * Determine whether a placement string is well-formed:
     *  - it consists of exactly N three-character piece placements (where N = 1 .. 12);
     *  - each piece placement is well-formed
     *  - no piece appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    static boolean isPlacementWellFormed(String placement) {
                /**This method is implement by Yuanxin Ye*/
        if (placement == null || placement.length() == 0) {return false;}
        if (placement.length() %3 == 0 && placement.length()<=36) {
            for (int i = 1; i < placement.length(); i = i + 3) {
                if (isPiecePlacementWellFormed(placement.substring(i - 1, i + 2))) {
                    for (int j = 1; j < placement.length(); j = j + 3) {
                        if (i != j) {
                            if (placement.charAt(i) == placement.charAt(j)) {
                                return false;
                            }
                        }
                    }
                } else return false;
            }
        }
        else return false;
        return true;
    }

    /**
     * Return a array of peg locations according to which pegs the given piece placement touches.
     * The values in the array should be ordered according to the units that constitute the
     * piece.
     * The code needs to account for the origin of the piece, the piece shape, and the piece
     * orientation.
     * @param piecePlacement A valid string describing a piece placement
     * @return An array of integers corresponding to the pegs which the piece placement touches,
     * listed in the normal order of units for that piece.   The value 0 corresponds to
     * peg 'A', 1 to peg 'B', etc.
     */
    static int[] getPegsForPiecePlacement(String piecePlacement) {
        /**
         This method is implemented by Sicheng Xu
         As the game is played on a coordinate system, the edge cases can be easily solved
         */
        if (piecePlacement.length() != 3) throw new IllegalArgumentException();
        Piece piece = new Piece(piecePlacement);
        return piece.getPegs();
    }


    /**
     * Determine whether a placement is valid.  To be valid, the placement must be well-formed
     * and each piece must correctly connect with each other.
     *
     * @param placement A placement string
     * @return True if the placement is valid
     */
    public static boolean isPlacementValid(String placement) {
        /**this method is implemented by Sicheng Xu*/
        if (placement.length() % 3 != 0) throw new IllegalArgumentException();
        PieceList list = new PieceList();
        int position = 0;
        while (position + 3 <= placement.length()) { // split placement
            String piecePlacement = placement.substring(position, position + 3);
            if (!isPiecePlacementWellFormed(piecePlacement)) {
                return false;
            }
            if (!list.add(new Piece(piecePlacement))) {
                return false;
            }
            position += 3;
        }
        return true;
    }

    /**
     * Return an array of all solutions given a starting placement.
     *
     * @param placement  A valid piece placement string.
     * @return An array of strings, each describing a solution to the game given the
     * starting point provied by placement.
     */
    static String[] getSolutions(String placement) {
        // FIXME Task 10: determine all solutions to the game, given a particular starting placement
        /**
         * Implemented by JIAYI CHEN
         * The core idea here is to use recursion to build the valid placements
         * In each step, one pieceplacement will be added to the input placement
         *
         * In the first few steps, the pieces and pegs which can never be used will be found out
         * and they will not be used in the later recursions
         *
         * Once the placement has the length of 36, the recursion will terminate
         * */
        if (placement.length()==36) {       // base case
            if (!isPlacementValid(placement)) {return null;}
            String[] itself = {placement};
            return itself;
        }
        List<Integer> ps = new ArrayList<>();       // all pieces(ps), which will get filtered
        for (int i =0; i<12; i++) {
            ps.add(i);
        }
        List<Integer> eps = new ArrayList<>();      // all pegs(eps), which will get filtered
        for (int i =0; i<24; i++) {
            eps.add(i);
        }
        List<Integer> tps = new ArrayList<>();    // taken pegs(tps), which can no longer be placed with piece
        for (int i = 0; i<placement.length(); i+=3) {
            // filter ps and eps to get the pegs and pieces which can be selected
            int pn = placement.charAt((i+1));
            int index_piece = ps.indexOf (pn -'A');
            int index_peg = eps.indexOf (placement.charAt(i)-'A');
            Piece p = new Piece(placement.substring(i,i+3));
            ps.remove(index_piece);
            if (placement.charAt(1+i)!= 'A') {
                tps.add(eps.get(index_peg));
                eps.remove(index_peg);
            }
            if (p.getUnitAt(0).isRing()) {
                int[] pegs = p.getPegs();
                int index_remove = eps.indexOf(pegs[0]);
                tps.add(eps.get(index_remove));
                eps.remove(index_remove);
            }
            if (p.getUnitAt(2).isRing()) {
                int[] pegs = p.getPegs();
                int index_remove = eps.indexOf(pegs[2]);
                tps.add(eps.get(index_remove));
                eps.remove(index_remove);
            }
        }

        Set<Integer> nearpoints = new HashSet<>();  // find the points near the taken points
        for (Integer i : tps) {
            if(i%6!=0&&eps.contains(i+1)) nearpoints.add(i+1);
            if((i+1)%6!=0&&eps.contains(i-1)) nearpoints.add(i-1);
            if(i>=0 && i<=17&&eps.contains(i+6)) nearpoints.add(i+6);
            if(i>=6 && i<=23&&eps.contains(i-6)) nearpoints.add(i-6);
            if((i>=6 && i<=23)&&((i+1)%6!=0)&&eps.contains(i-5)) nearpoints.add(i-5);
            if((i>=0 && i<=17)&&((i+1)%6!=0)&&eps.contains(i+7))nearpoints.add(i+7);
            if((i>=6 && i<=23)&&(i%6!=0)&&eps.contains(i-7))nearpoints.add(i-7);
            if((i>=0 && i<=17)&&(i%6!=0)&&eps.contains(i+5)) nearpoints.add(i+5);
        }

        List<String> validPieces = new ArrayList<>();
        for (Integer i : nearpoints ) {    // generate possible piece
            for (Integer j : ps) {
                int oris = (j==0) ? 6 : 12;
                for (int k =0; k<oris; k++) {
                    String validPiece = ""+((char) ('A'+i))+ ((char) ('A'+j))+((char) ('A'+k));
                    Piece checkp = new Piece(validPiece);
                    if (!checkp.isOutOfBound()) {
                        String add = placement+validPiece; // add the piece into the string
                        if (isPlacementValid(add)) {
                            validPieces.add(add);   // if valid, the string is added into the list
                        }
                    }
                }
            }
        }
        List<String> listvps = new ArrayList<>();
        for (String s : validPieces) {
            for (String v : Arrays.asList(getSolutions(s))) {
                // check if there are equivalent strings
                int count =0;
                int stringno = 0;
                while (stringno<listvps.size() && count==0) {
                    if (areIdentical(listvps.get(stringno),v)) {
                        count++;
                    }
                    stringno++;
                }
                if (count==0) listvps.add(v);
            }
        }
        return  listvps.toArray(new String[0]);
    }

    private static boolean areIdentical(String p1, String p2) {
        /**
         * Implemeented by JIAYI CHEN
         * This method is used to check whether two placements are equivalent
         * That is see whether two strings will lead to the same pattern
         * */
        if (p1.equals(p2)) return true;
        if (p1.length()==p2.length()) {
            for (int i =0 ; i< p1.length(); i+=3) {
                int count = 0;
                for (int j=0; j< p2.length(); j+=3) {
                    if (p1.substring(i,i+3).equals(p2.substring(j,j+3))) {
                        count++;
                    }
                }
                if (count !=1) return false;
            }
        }
        return true;
    }
}