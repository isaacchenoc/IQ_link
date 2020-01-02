package comp1110.ass2.gui;

import comp1110.ass2.Piece;
import comp1110.ass2.PieceList;
import javafx.scene.image.Image;

import java.util.Random;

/**
 * Created by tom on 16/9/16.
 */
public class GuiUtils implements BasicViewSize {
    /**
     * get the png from assets
     **/
    public static Image getImageFromShapeRaw(char shapeRaw) {
        return new Image(GuiUtils.class.getResource(URI_BASE + shapeRaw + ".png").toString());
    }

    public static PieceList getStartPlacement(boolean isNeedSolvable) {
        PieceList pieceList = new PieceList();
        for (int i = 0; i < 4; i++) {
            Piece p = new Piece(getRandomPiecePlacement());
            while (!pieceList.add(p)) {
                System.out.println("try " + p.getPiecePlacement());
                p = new Piece(getRandomPiecePlacement());
            }
        }
        return pieceList;
    }

    private static String getRandomPiecePlacement() {
        /**
         * Randomly generate pieceplacments
         * which will be used in starting pieces generation
         **/
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        char first = (char) (r.nextInt('X' - 'A') + 'A');
        sb.append(first);
        char second = (char) (r.nextInt('L' - 'A') + 'A');
        sb.append(second);
        char third;
        if (second == 'A') {
            third = (char) (r.nextInt('F' - 'A') + 'A');
        } else {
            third = (char) (r.nextInt('L' - 'A') + 'A');
        }
        sb.append(third);
        return sb.toString();
    }
}
