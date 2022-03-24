package ch.zhaw.hexboard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * *
 * <p>
 * This class performs tests for the class {@link Edge}.
 * </p>
 *
 * @author tebe
 */
public class EdgeTest {
    private java.awt.Point[] hexagon22 = new java.awt.Point[]{ new java.awt.Point(2, 0), new java.awt.Point(3, 1), new java.awt.Point(3, 3), new java.awt.Point(2, 4), new java.awt.Point(1, 3), new java.awt.Point(1, 1) };

    private java.awt.Point[] hexagon75 = new java.awt.Point[]{ new java.awt.Point(7, 3), new java.awt.Point(8, 4), new java.awt.Point(8, 6), new java.awt.Point(7, 7), new java.awt.Point(6, 6), new java.awt.Point(6, 4) };

    @org.junit.jupiter.api.Test
    public void createValidEdge() {
        new ch.zhaw.hexboard.Edge(new java.awt.Point(0, 0), new java.awt.Point(1, 1));
    }

    @org.junit.jupiter.api.Test
    public void edgeEqualityStartEndPointReversed() {
        for (int i = 0; i < (hexagon22.length - 1); i++) {
            assertEquals(new ch.zhaw.hexboard.Edge(hexagon22[i], hexagon22[i + 1]), new ch.zhaw.hexboard.Edge(hexagon22[i + 1], hexagon22[i]));
        }
        for (int i = 0; i < (hexagon75.length - 1); i++) {
            assertEquals(new ch.zhaw.hexboard.Edge(hexagon75[i], hexagon75[i + 1]), new ch.zhaw.hexboard.Edge(hexagon75[i + 1], hexagon75[i]));
        }
    }

    @org.junit.jupiter.api.Test
    public void notEquals() {
        assertNotEquals(new ch.zhaw.hexboard.Edge(hexagon22[0], hexagon22[1]), new ch.zhaw.hexboard.Edge(hexagon22[1], hexagon22[2]));
    }

    @org.junit.jupiter.api.Test
    public void createWithBothArgumentsNull() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(null, null));
    }

    @org.junit.jupiter.api.Test
    public void createWithFirstArgumentNull() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(null, new java.awt.Point(1, 0)));
    }

    @org.junit.jupiter.api.Test
    public void createWithSecondArgumentNull() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(1, 0), null));
    }

    @org.junit.jupiter.api.Test
    public void createWithStartAndEndpointIdentical() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(hexagon22[0], hexagon22[0]));
    }

    @org.junit.jupiter.api.Test
    public void notAnEdgeHorizontalOddTop() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(5, 7), new java.awt.Point(7, 7)));
    }

    @org.junit.jupiter.api.Test
    public void notAnEdgeHorizontalOddMiddle() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(3, 2), new java.awt.Point(5, 2)));
    }

    @org.junit.jupiter.api.Test
    public void notAnEdgeHorizontalOddBottom() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(5, 3), new java.awt.Point(7, 3)));
    }

    @org.junit.jupiter.api.Test
    public void notAnEdgeHorizontalEvenTop() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(4, 4), new java.awt.Point(6, 4)));
    }

    @org.junit.jupiter.api.Test
    public void notAnEdgeHorizontalEvenMiddle() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(2, 5), new java.awt.Point(4, 5)));
    }

    @org.junit.jupiter.api.Test
    public void notAnEdgeHorizontalEvenBottom() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(4, 6), new java.awt.Point(6, 6)));
    }

    @org.junit.jupiter.api.Test
    public void notAnEdgeVerticalEven() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(7, 7), new java.awt.Point(7, 3)));
    }

    @org.junit.jupiter.api.Test
    public void notAnEdgeVerticalOdd() {
        assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(new java.awt.Point(6, 4), new java.awt.Point(6, 0)));
    }
}