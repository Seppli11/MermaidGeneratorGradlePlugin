package ch.zhaw.hexboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * *
 * <p>
 * Tests for the class {@link HexBoard}.
 * </p>
 *
 * @author tebe
 */
public class HexBoardTest {
    private ch.zhaw.hexboard.HexBoard<java.lang.String, java.lang.String, java.lang.String, java.lang.String> board;

    private java.awt.Point[] corner;

    /**
     * Setup for a test - Instantiates a board and adds one field at (7,5).
     *
     * <pre>
     *         0    1    2    3    4    5    6    7    8
     *         |    |    |    |    |    |    |    |    |   ...
     *
     *  0----
     *
     *  1----
     *
     *  2----
     *
     *  3----                                     C
     *                                         /     \
     *  4----                                C         C
     *
     *  5----                                |    F    |        ...
     *
     *  6----                                C         C
     *                                         \     /
     *  7----                                     C
     * </pre>
     */
    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        board = new ch.zhaw.hexboard.HexBoard<>();
        board.addField(new java.awt.Point(7, 5), "00");
        java.awt.Point[] singleField = new java.awt.Point[]{ new java.awt.Point(7, 3), new java.awt.Point(8, 4), new java.awt.Point(8, 6), new java.awt.Point(7, 7), new java.awt.Point(6, 6), new java.awt.Point(6, 4) };
        this.corner = singleField;
    }

    // Edge retrieval
    @org.junit.jupiter.api.Test
    public void edgeTest() {
        for (int i = 0; i < (corner.length - 1); i++) {
            assertNull(board.getEdge(corner[i], corner[i + 1]));
            board.setEdge(corner[i], corner[i + 1], java.lang.Integer.toString(i));
            assertEquals(board.getEdge(corner[i], corner[i + 1]), java.lang.Integer.toString(i));
        }
    }

    @org.junit.jupiter.api.Test
    public void noEdgeCoordinatesTest() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> board.getEdge(new java.awt.Point(2, 2), new java.awt.Point(0, 2)));
    }

    @org.junit.jupiter.api.Test
    public void edgeDoesNotExistTest() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> board.getEdge(new java.awt.Point(0, 2), new java.awt.Point(3, 1)));
    }

    // Corner retrieval
    @org.junit.jupiter.api.Test
    public void cornerTest() {
        for (java.awt.Point p : corner) {
            assertNull(board.getCorner(p));
            board.setCorner(p, p.toString());
            assertEquals(board.getCorner(p), p.toString());
        }
    }

    @org.junit.jupiter.api.Test
    public void noCornerCoordinateTest() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> board.getCorner(new java.awt.Point(2, 2)));
    }

    @org.junit.jupiter.api.Test
    public void cornerDoesNotExistTest() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> board.getCorner(new java.awt.Point(2, 2)));
    }

    // Field addition/retrieval
    @org.junit.jupiter.api.Test
    public void fieldAreadyExistsErrorTest() {
        board.addField(new java.awt.Point(2, 2), "22");
        assertThrows(java.lang.IllegalArgumentException.class, () -> board.addField(new java.awt.Point(2, 2), "22"));
    }

    @org.junit.jupiter.api.Test
    public void fieldRetrievalTest() {
        java.awt.Point field = new java.awt.Point(2, 2);
        board.addField(field, new java.lang.String("22"));
        assertTrue(board.hasField(field));
        assertEquals(board.getField(field), "22");
    }

    @org.junit.jupiter.api.Test
    public void fieldRetrievalWrongCoordinatesOutsideTest() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> board.getField(new java.awt.Point(10, 10)));
    }

    @org.junit.jupiter.api.Test
    public void fieldRetrievalWrongCoordinatesInsideTest() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> board.getField(new java.awt.Point(2, 2)));
    }
}