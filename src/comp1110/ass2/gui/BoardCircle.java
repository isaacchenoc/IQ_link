package comp1110.ass2.gui;

import comp1110.ass2.Point;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by tom on 16/9/15.
 */
public class BoardCircle extends Circle implements BasicViewSize {

    /**
     * Using a point or a char to generate a peg, which will be displayed on the board
     * */

    public BoardCircle(char pegRaw) {
        this(new Point(pegRaw));
    }

    public BoardCircle(Point point) {
        if (point.isOutOfBound()) throw new IllegalArgumentException();

        double margin = SQUARE_SIZE / 2;

        setCenterX(margin + point.x / 2 * SQUARE_SIZE + (point.x % 2) * margin);
        setCenterY(margin + point.y * ROW_HEIGHT);
        setRadius(ROW_HEIGHT / 3);
        setFill(Color.rgb(192, 192, 192));
        setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
        });
    }
}
