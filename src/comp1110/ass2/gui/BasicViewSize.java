package comp1110.ass2.gui;

/**
 * Created by tom on 16/9/15.
 */
public interface BasicViewSize {
    /**
     * Implemented by JIAYI CHEN
     * Since each classes cannot run without these fields,
     * it is a bit more convenient to have a interface,
     * so that other classes can just use the fields directly
     * */
    String URI_BASE = "assets/";
    int SQUARE_SIZE = 100;
    double ROW_HEIGHT = SQUARE_SIZE * Math.sqrt(3) / 2; // 60 degrees
    int COL_SIZE = 6;
    int PEGS = 4 * 6;  // number of places
}
