package comp1110.ass2.gui;

import comp1110.ass2.LinkGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * A very simple viewer for piece placements in the link game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 100;
    private static final int PIECE_IMAGE_SIZE = 3 * SQUARE_SIZE;
    private static final double ROW_HEIGHT = SQUARE_SIZE * 0.8660254; // 60 degrees
    private static final int VIEWER_WIDTH = 750;
    private static final int VIEWER_HEIGHT = 500;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField textField;


    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        /**
         This part is mainly implemented by Isaac (JIAYI CHEN)
         and inspired from the Board Class of Assignment 1
         generates a number of pegs and use the pictures in assets to build a viewer
         Since it's just a simple viewer, the pegs are draw directly using Circle without defining new class
         Then put the pieces into the pegs
         */
        root.getChildren().clear();
        root.getChildren().add(controls);
        for (int i = 0; i < LinkGame.PEGS; i++) {
            double margin_X;
            double margin_Y = 50 + SQUARE_SIZE / 2;
            if (i / LinkGame.COL_SIZE == 0 || i / LinkGame.COL_SIZE == 2) {
                margin_X = 50 + SQUARE_SIZE / 2;
            } else {
                margin_X = SQUARE_SIZE + 50;
            }
            Circle r = new Circle(margin_X + (i % LinkGame.COL_SIZE) * SQUARE_SIZE, margin_Y + (i / LinkGame.COL_SIZE) * ROW_HEIGHT, ROW_HEIGHT / 3);
            r.setFill(Color.rgb(192, 192, 192));
            root.getChildren().add(r);
        }
        for (int j = 0; j < placement.length(); j += 3) {
            PieceView p = new PieceView(placement.substring(j, j + 3));
            root.getChildren().add(p);
        }
    }

    class PieceView extends ImageView {
        PieceView(char piece) {
            if ((piece < 'A' || piece > 'L')) {
                throw new IllegalArgumentException("Bad piece: \"" + piece + "\"");
            }

            setImage(new Image(Viewer.class.getResource(URI_BASE + piece + ".png").toString()));
            setFitHeight(PIECE_IMAGE_SIZE);
            setFitWidth(PIECE_IMAGE_SIZE);
        }

        PieceView(String piecePlacement) {
            this(piecePlacement.charAt(1));
            if (piecePlacement.length() != 3 ||
                    piecePlacement.charAt(0) < 'A' || piecePlacement.charAt(0) > 'X' ||
                    piecePlacement.charAt(2) < 'A' || piecePlacement.charAt(2) > 'L') {
                throw new IllegalArgumentException("Bad piecePlacement string: " + piecePlacement);
            }
            int pos = piecePlacement.charAt(0) - 'A';
            int ori;
            if (piecePlacement.charAt(2) <= 'F') {
                ori = (piecePlacement.charAt(2) - 'A');
                setRotate(60 * ori);
            } else {
                ori = piecePlacement.charAt(2) - 'G';
                setScaleY(-1);
                setRotate(60 * ori);
            }
            double margin_Y = -SQUARE_SIZE + 50;
            double margin_X;
            if (pos / LinkGame.COL_SIZE == 0 || pos / LinkGame.COL_SIZE == 2) {
                margin_X = -SQUARE_SIZE + 50;
            } else {
                margin_X = -0.5 * SQUARE_SIZE + 50;
            }
            int x = (pos % LinkGame.COL_SIZE);
            int y = (pos / LinkGame.COL_SIZE);
            setLayoutX(margin_X + x * SQUARE_SIZE);
            setLayoutY(margin_Y + y * ROW_HEIGHT);
        }
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("LinkGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
