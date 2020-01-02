package comp1110.ass2.gui;

import comp1110.ass2.LinkGame;
import comp1110.ass2.Piece;
import comp1110.ass2.PieceList;
import comp1110.ass2.Point;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Board extends Application implements BasicViewSize {

    /**
     * Implemented by the whole group
     *
     * methods here are implemented separately
     * and in the main method, all methods are joint together to construct the game
     *
     * */
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;

    private Group board = new Group();
    private Group root = new Group();
    private GridPane gridPane = new GridPane();
    private PieceList pieceList = new PieceList();

    private boolean accept = false; // Whether board has accepted the piece
    private PieceList mStartList;

    // FIXME Task 11: Implement hints

    // FIXME Task 12: Generate interesting starting placements


    /**
     * drag a piece into the board
     * */
    private void place(String piecePlacement) {
        if (!LinkGame.isPiecePlacementWellFormed(piecePlacement)) {
            return;
        }
        getPanePieceView(piecePlacement.charAt(1) - 'A').setPlaced(true);
        System.out.println("new one");
        Piece newPiece = new Piece(piecePlacement);
        PieceView newPieceView = new PieceView(newPiece, pieceView -> {
            temporaryRemovePiece(pieceView);
            pieceList.remove(pieceView.getPiece());
            accept = false;
        });
        newPieceView.setId(piecePlacement.charAt(1) + "");
        newPieceView.setOnDragDone(event -> {
            if (!accept) {
                PieceView p = (PieceView) event.getSource();
                remove(p);
            }
        });
        newPieceView.setOnDragDropped(pieceDragDroppedHandler);
        if (pieceList.add(newPiece)) {
            root.getChildren().add(newPieceView);
            accept = true;
        } else {
            accept = false;
            remove(newPieceView);
        }
    }

    private PieceView getPanePieceView(int position) {
        return (PieceView) gridPane.getChildren().get(position);
    }

    private void temporaryRemovePiece(PieceView piece) {
        root.getChildren().remove(piece);
    }

    private void remove(PieceView pieceView) {
        System.out.println("remove");
        root.getChildren().remove(pieceView);
        pieceList.remove(pieceView.getPiece());
        PieceView p = getPanePieceView(pieceView.getData().charAt(0) - 'A');
        p.setPlaced(false);
        p.setOpacity(1);
    }

    private void removeAllPieceView() {
        ArrayList<Node> nodes = root.getChildren().stream().filter(n -> n instanceof PieceView).collect(Collectors.toCollection(ArrayList::new));
        root.getChildren().removeAll(nodes);
    }

    /**
     * create an empty board
     */
    private void makeBoard() {
        root.getChildren().add(new RootPanel(pieceDragDroppedHandler));
    }

    private void placePieceList(PieceList pieceList) {
        this.pieceList.addAll(pieceList);
        for (Piece p : pieceList.getData()) {
            PieceView pieceView = new PieceView(p);
            pieceView.setOnDragDropped(pieceDragDroppedHandler);
            root.getChildren().add(pieceView);
        }
    }

    private void hideBottomPiece(PieceList pieceList) {
        for (Piece piece : pieceList.getData()) {
            gridPane.getChildren().get(piece.getShapeRaw() - 'A').setOpacity(0);
        }
    }

    private void showAllBottomPiece() {
        for (Node n : gridPane.getChildren()) {
            n.setOpacity(1);
        }
    }

    private void makeScrollPanel() {
        ScrollPane sp = new ScrollPane();
        for (char shape = 'A'; shape <= 'L'; shape++) {
            PieceView pieceView = new PieceView(shape);
            pieceView.setId(String.valueOf(shape));
            gridPane.add(pieceView, shape - 'A', 0);
        }
        sp.setContent(gridPane);
        sp.setPrefWidth(BOARD_WIDTH);
        sp.setPrefHeight(SQUARE_SIZE * 3 + 20);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setLayoutY(BOARD_HEIGHT - SQUARE_SIZE * 3 - 20);
        root.getChildren().add(sp);
    }

    private void addClearButton() {
        Button button = new Button("Clear");
        button.setLayoutX(3 * BOARD_WIDTH / 4);
        button.setLayoutY(3 * BOARD_HEIGHT / 12);
        button.setMinWidth(BOARD_WIDTH / 4.5);
        button.prefHeight(BOARD_HEIGHT / 8);
        button.setFont(Font.font("Verdana", 40));
        button.setOnMouseClicked(event -> {
            pieceList.clear();
            removeAllPieceView();
            showAllBottomPiece();
            hideBottomPiece(mStartList);
            placePieceList(mStartList);
        });
        board.getChildren().add(button);
    }

    private void addRestartButton() {
        Button button = new Button("Restart");
        button.setLayoutX(3 * BOARD_WIDTH / 4);
        button.setLayoutY(3 * BOARD_HEIGHT / 12 - BOARD_HEIGHT / 7);
        button.setMinWidth(BOARD_WIDTH / 4.5);
        button.prefHeight(BOARD_HEIGHT / 8);
        button.setFont(Font.font("Verdana", 40));
        button.setOnMouseClicked(event -> {
            gridPane.getChildren().clear();
            pieceList.clear();
            root.getChildren().clear();
            restart();
        });
        board.getChildren().add(button);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(this.getClass().getSimpleName());
        Scene scene = new Scene(board, BOARD_WIDTH, BOARD_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        board.getChildren().add(root);
        addClearButton();
        addRestartButton();
        restart();
    }

    private void restart() {
        makeBoard();
        makeScrollPanel();

        mStartList = GuiUtils.getStartPlacement(false);
        placePieceList(mStartList);
        hideBottomPiece(mStartList);
    }

    private EventHandler<DragEvent> pieceDragDroppedHandler = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            Point p = getPoint(event);
            place(p.getPegRaw() + event.getDragboard().getString());
        }

        private Point getPoint(DragEvent event) {
            int y = (int) ((event.getSceneY() + ROW_HEIGHT / 2) / ROW_HEIGHT + 0.5) - 1;
            int x = (int) ((event.getSceneX() + SQUARE_SIZE / 2) / SQUARE_SIZE * 2 - 1) - y % 2;
            return new Point(x, y);
        }
    };
}