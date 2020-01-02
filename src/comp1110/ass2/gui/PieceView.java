package comp1110.ass2.gui;

import comp1110.ass2.Piece;
import comp1110.ass2.Point;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;

/**
 * Created by tom on 16/9/15.
 */
public class PieceView extends ImageView implements BasicViewSize {
    /**
     * Implemented by Yuanxin Ye
     * The imageView that will be placed on the board or the panel
     *
     * Different constructors are used in different situations
     * */
    private char shape;
    private int orientation;
    private Piece mPiece = null;
    private boolean isPlaced = false;

    private static final int PIECE_IMAGE_SIZE = 3 * SQUARE_SIZE;

    public PieceView(char shape) {
        //the constructor for the panel
        setFitHeight(PIECE_IMAGE_SIZE);
        setFitWidth(PIECE_IMAGE_SIZE);
        setImage(GuiUtils.getImageFromShapeRaw(shape));
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                rotate();
            }
        });
        setOnDragDetected(event -> {
            Dragboard dragboard = startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(getData());
            dragboard.setContent(content);
            dragboard.setDragView(getImageRaw());
            setOpacity(0); // we actually have not delete it, but hide it
            System.out.println("hide");
        });
        setOnDragDone(event -> {
            if (!isPlaced) {
                setOpacity(1);
                System.out.println("show not on board");
            }
        });
        this.shape = shape;
    }

    public PieceView(Piece piece) {
        // the constructor for board
        mPiece = piece;
        shape = piece.getShapeRaw();
        orientation = piece.getOrientation();
        setFitHeight(PIECE_IMAGE_SIZE);
        setFitWidth(PIECE_IMAGE_SIZE);
        setImage(GuiUtils.getImageFromShapeRaw(piece.getShapeRaw()));
        Point origin = piece.getPointAt(1);
        setLayoutX((origin.x / 2 - (origin.y % 2 == 0 ? 1 : 0.5)) * SQUARE_SIZE);
        setLayoutY(origin.y * ROW_HEIGHT - SQUARE_SIZE);
        if (orientation > 5) {
            setScaleY(-1);
        } else {
            setScaleY(1);
        }
        setRotate(60 * orientation);
        setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
        });
    }

    public PieceView(Piece piece, OnDragStartListener l) {
        // the constructor is for the draggable situations
        this(piece);
        setOnDragDetected(event -> {
            Dragboard dragboard = startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(getData());
            dragboard.setContent(content);
            dragboard.setDragView(getImageRaw());
            isPlaced = false;
            if (l != null) {
                l.onDragStart(PieceView.this);
            }
        });
    }

    /**
     * @return two char without position
     */
    public String getData() {
        int oRaw;
        if (shape == 'A' && orientation > 5) {
            oRaw = orientation % 6;
            switch (oRaw) {
                case 0:
                case 1:
                case 2:
                    oRaw = oRaw + 3;
                    break;
                case 3:
                case 4:
                case 5:
                    oRaw = oRaw - 3;
                    break;
            }
        } else {
            oRaw = orientation;
        }
        return "" + shape + (char) (oRaw + 'A');
    }

    public Piece getPiece() {
        return mPiece;
    }

    private void rotate() {
        orientation = (orientation + 1) % 12;
        if (orientation > 5) {
            setScaleY(-1);
        } else {
            setScaleY(1);
        }
        setRotate(60 * orientation);
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public Image getImageRaw() {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        return snapshot(sp, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PieceView)) {
            return false;
        }
        PieceView other = (PieceView) obj;
        return shape == other.shape && orientation == other.orientation;
    }

    public interface OnDragStartListener {
        void onDragStart(PieceView pieceView);
    }
}
