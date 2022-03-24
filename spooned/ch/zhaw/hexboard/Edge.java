package ch.zhaw.hexboard;
/**
 * This class models an edge on @see ch.zhaw.hexboard.HexBoard.
 * <p>
 * Edges are non-directional and can be created by providing the two points that
 * span an edge on the hex-grid defined by @see ch.zhaw.hexboard.HexBoard
 * </p>
 *
 * @author tebe
 */
final class Edge {
    private java.awt.Point start;

    private java.awt.Point end;

    /**
     * Creates an edge between the two points.
     *
     * @param p1
     * 		first point
     * @param p2
     * 		second point
     * @throws IllegalArgumentException
     * 		if the points are not non-null or not a
     * 		valid point for an edge on the grid defined
     * 		by @see ch.zhaw.hexboard.HexBoard
     */
    public Edge(java.awt.Point p1, java.awt.Point p2) {
        if (ch.zhaw.hexboard.Edge.isEdge(p1, p2)) {
            if ((p1.x > p2.x) || ((p1.x == p2.x) && (p1.y > p2.y))) {
                this.start = new java.awt.Point(p2);
                this.end = new java.awt.Point(p1);
            } else {
                this.start = new java.awt.Point(p1);
                this.end = new java.awt.Point(p2);
            }
        } else {
            throw new java.lang.IllegalArgumentException(((("Coordinates " + p1) + " and ") + p2) + " are not coordinates of an edge.");
        }
    }

    static boolean isEdge(java.awt.Point p1, java.awt.Point p2) {
        boolean isEdge = false;
        if ((((p1 != null) && (p2 != null)) && ch.zhaw.hexboard.HexBoard.isCornerCoordinate(p1)) && ch.zhaw.hexboard.HexBoard.isCornerCoordinate(p2)) {
            int xdistance = java.lang.Math.abs(p1.x - p2.x);
            int ydistance = java.lang.Math.abs(p1.y - p2.y);
            boolean isVerticalEdge = (xdistance == 0) && (ydistance == 2);
            boolean isDiagonalEdge = (xdistance == 1) && (ydistance == 1);
            isEdge = isVerticalEdge || isDiagonalEdge;
        }
        return isEdge;
    }

    public boolean isEdgePoint(java.awt.Point p1) {
        return start.equals(p1) || end.equals(p1);
    }

    /* (non-Javadoc)

    @see java.lang.Object#hashCode()
     */
    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (end == null ? 0 : end.hashCode());
        result = (prime * result) + (start == null ? 0 : start.hashCode());
        return result;
    }

    /* (non-Javadoc)

    @see java.lang.Object#equals(java.lang.Object)
     */
    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ch.zhaw.hexboard.Edge other = ((ch.zhaw.hexboard.Edge) (obj));
        if (end == null) {
            if (other.end != null) {
                return false;
            }
        } else if (!end.equals(other.end)) {
            return false;
        }
        if (start == null) {
            if (other.start != null) {
                return false;
            }
        } else if (!start.equals(other.start)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)

    @see java.lang.Object#toString()
     */
    @java.lang.Override
    public java.lang.String toString() {
        return ((("Edge [start=" + start) + ", end=") + end) + "]";
    }
}