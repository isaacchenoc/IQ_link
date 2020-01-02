package comp1110.ass2.gui;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by tom on 2016/9/26.
 */
public class RootPanel extends Pane implements BasicViewSize {
    /**Implemented by Yuanxin Ye
     * Using the boardCircles to generate the board (that is 24 pegs)
     * */
    public RootPanel(EventHandler<DragEvent> onDragDropped) {
        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        for (char i = 'A'; i < PEGS + 'A'; i++) {
            BoardCircle circle = new BoardCircle(i);
            circle.setId(String.valueOf(i));
            getChildren().add(circle);
        }
        setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
        });
        setOnDragDropped(onDragDropped);
    }
}
