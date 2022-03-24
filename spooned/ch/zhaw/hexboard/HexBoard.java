package ch.zhaw.hexboard;
/**
 * *
 * <p>
 * This class represents a simple generic hexagonal game board.
 * </p>
 * <p>The game board uses a fixed coordinate system which is structured as follows:</p>
 *
 * <pre>
 *         0    1    2    3    4    5    6    7    8
 *         |    |    |    |    |    |    |    |    |   ...
 *
 *  0----  C         C         C         C         C
 *            \   /     \   /     \   /     \   /     \
 *  1----       C         C         C         C         C
 *
 *  2----  F    |    F    |    F    |    F    |    F    |   ...
 *
 *  3----       C         C         C         C         C
 *           /     \   /     \   /     \   /     \   /
 *  4----  C         C         C         C         C
 *
 *  5----  |    F    |    F    |    F    |    F    |    F   ...
 *
 *  6----  C         C         C         C         C
 *           \     /   \     /   \     /   \     /   \
 *  7----       C         C         C         C         C
 *
 *    ...
 * </pre>
 *
 * <p>
 * Fields <strong>F</strong> and corners <strong>C</strong> can be retrieved
 * using their coordinates ({@link java.awt.Point}) on the board. Edges can be
 * retrieved using the coordinates of the two corners they connect.
 * </p>
 *
 * <p>
 * When created, the board is empty (no fields added). To add fields, the
 * #{@link #addField(Point, Object)} function can be used. Edges and corners are
 * automatically created when adding a field. They cannot be created/removed
 * individually. When adding a field, edges and corners that were already
 * created, e.g., because adding an adjacent field already created them, are
 * left untouched.
 * </p>
 *
 * <p>
 * Fields, edges and corners can store an object of the type of the
 * corresponding type parameter each.
 * </p>
 *
 * <p>
 * Furthermore, the hexagonal game board can store six additional objects, so
 * called annotations, for each field. These objects are identified by the
 * coordinates of the field and the corner. Hence, they can be thought of being
 * located between the center and the respective corner. Or in other words,
 * their positions correspond to the positions N, NW, SW, NE, NW, SE and NE in
 * the below visualization of a field.
 * </p>
 *
 * <pre>
 *       SW (C) SE
 *    /      N      \
 *  (C) NW       NE (C)
 *   |       F       |
 *   |               |
 *  (C) SW       SE (C)
 *    \      S      /
 *       NW (C) NE
 * </pre>
 *
 * @param <F>
 * 		Data type for the field data objects
 * @param <C>
 * 		Data type for the corner data objects
 * @param <E>
 * 		Data type for the edge data objects
 * @param <A>
 * 		Data type for the annotation data objects
 * @author tebe
 */
public class HexBoard<F, C extends java.util.List<C>, E> {
    private int maxCoordinateX = 0;

    private int maxCoordinateY = 0;

    private final java.util.Map<java.awt.Point, F> field;

    private final java.util.Map<java.awt.Point, C> corner;

    private final java.util.Map<ch.zhaw.hexboard.Edge, E> edge;

    private final java.util.Map<ch.zhaw.hexboard.FieldAnnotationPosition, ch.zhaw.hexboard.A> annotation;

    /**
     * Constructs an empty hexagonal board.
     */
    public HexBoard() {
        field = new java.util.HashMap<>();
        corner = new java.util.HashMap<>();
        edge = new java.util.HashMap<>();
        annotation = new java.util.HashMap<>();
    }

    /**
     * Adds a field to the board and creates the surrounding (empty) corners and
     * edges if they do not yet exist Note: Corners and edges of a field might
     * already have been created while creating adjacent fields.
     *
     * @param center
     * 		Coordinate of the center of a field on the unit grid
     * @param element
     * 		Data element to be stored for this field
     * @throws IllegalArgumentException
     * 		if center is not the center of a field, the
     * 		field already exists or data is null
     */
    public void addField(java.awt.Point center, F element) {
        if (ch.zhaw.hexboard.HexBoard.isFieldCoordinate(center) && (!field.containsKey(center))) {
            field.put(center, element);
            maxCoordinateX = java.lang.Math.max(center.x + 1, maxCoordinateX);
            maxCoordinateY = java.lang.Math.max(center.y + 2, maxCoordinateY);
            // add (empty) edge, if they do not yet exist
            for (ch.zhaw.hexboard.Edge e : constructEdgesOfField(center)) {
                if (!edge.containsKey(e)) {
                    edge.put(e, null);
                }
            }
            // add (empty) corners, if they do not yet exist
            for (java.awt.Point p : ch.zhaw.hexboard.HexBoard.getCornerCoordinatesOfField(center)) {
                if (!corner.containsKey(p)) {
                    corner.put(p, null);
                }
            }
        } else {
            throw new java.lang.IllegalArgumentException(((("Coordinates are not the center of a field, the field already exists or data is null - (" + center.x) + ", ") + center.y) + ")");
        }
    }

    /**
     * Add an annotation for the specified field and corner.
     *
     * @param center
     * 		the center of the field
     * @param corner
     * 		the corner of the field
     * @param data
     * 		the annotation
     * @throws IllegalArgumentException
     * 		if the field does not exists or when the
     * 		annotation already exists
     */
    public void addFieldAnnotation(java.awt.Point center, java.awt.Point corner, ch.zhaw.hexboard.A data) {
        ch.zhaw.hexboard.FieldAnnotationPosition annotationPosition = new ch.zhaw.hexboard.FieldAnnotationPosition(center, corner);
        if (!annotation.containsKey(annotationPosition)) {
            annotation.put(annotationPosition, data);
        } else {
            throw new java.lang.IllegalArgumentException((((("Annotation: " + annotation) + " already exists for field ") + center) + " and position ") + corner);
        }
    }

    /**
     * Get an annotation for the specified field and corner.
     *
     * @param center
     * 		the center of the field
     * @param corner
     * 		the corner of the field
     * @return the annotation
     * @throws IllegalArgumentException
     * 		if coordinates are not a field and
     * 		corresponding corner coordinate
     */
    public ch.zhaw.hexboard.A getFieldAnnotation(java.awt.Point center, java.awt.Point corner) {
        return annotation.get(new ch.zhaw.hexboard.FieldAnnotationPosition(center, corner));
    }

    /**
     * Get field annotation whose position information includes the specified corner.
     *
     * @param corner
     * 		the corner
     * @return a list with the annotations that are not null
     * @throws IllegalArgumentException
     * 		if corner is not a corner
     */
    public java.util.List<ch.zhaw.hexboard.A> getFieldAnnotationsForCorner(java.awt.Point corner) {
        java.util.List<ch.zhaw.hexboard.A> list = new java.util.LinkedList<>();
        for (java.util.Map.Entry<ch.zhaw.hexboard.FieldAnnotationPosition, ch.zhaw.hexboard.A> entry : annotation.entrySet()) {
            if (entry.getKey().isCorner(corner) && (entry.getValue() != null)) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    /**
     * Get all field annotation of the specified field.
     *
     * @param center
     * 		the field
     * @return a list with the annotations that are not null
     * @throws IllegalArgumentException
     * 		if center is not a field
     */
    public java.util.List<ch.zhaw.hexboard.A> getFieldAnnotationsForField(java.awt.Point center) {
        java.util.List<ch.zhaw.hexboard.A> list = new java.util.LinkedList<>();
        for (java.util.Map.Entry<ch.zhaw.hexboard.FieldAnnotationPosition, ch.zhaw.hexboard.A> entry : annotation.entrySet()) {
            if (entry.getKey().isField(center) && (entry.getValue() != null)) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    /**
     * Determines whether the field at the specified position exists.
     *
     * @param center
     * 		the field
     * @return false, if the field does not exist or the position is not a field
     */
    public boolean hasField(java.awt.Point center) {
        if (!ch.zhaw.hexboard.HexBoard.isFieldCoordinate(center)) {
            return false;
        }
        return field.containsKey(center);
    }

    static boolean isFieldCoordinate(java.awt.Point position) {
        boolean isYFieldCoordinateEven = ((position.y - 2) % 6) == 0;
        boolean isYFieldCoordinateOdd = ((position.y - 5) % 6) == 0;
        boolean isXFieldCoordinateEven = (position.x % 2) == 0;
        boolean isXFieldCoordinateOdd = ((position.x - 1) % 2) == 0;
        return (((position.y >= 2) && (position.x >= 1)) && (isYFieldCoordinateEven && isXFieldCoordinateEven)) || (isYFieldCoordinateOdd && isXFieldCoordinateOdd);
    }

    static boolean isCornerCoordinate(java.awt.Point p) {
        // On the horizontal center lines, no edge points exist
        boolean isOnFieldCenterLineHorizontal = ((p.y - 2) % 3) == 0;
        // On the vertical center lines, edge points exist
        boolean isOnFieldCenterLineVerticalOdd = (((p.x - 1) % 3) == 0) && ((p.x % 2) == 0);
        boolean isOnFieldCenterLineVerticalEven = (((p.x - 1) % 3) == 0) && (((p.x - 1) % 2) == 0);
        boolean isNotAnEdgePointOnFieldCentralVerticalLine = (isOnFieldCenterLineVerticalOdd && (!(((p.y % 6) == 0) || (((p.y + 2) % 6) == 0)))) || (isOnFieldCenterLineVerticalEven && (!((((p.y + 5) % 6) == 0) || (((p.y + 3) % 6) == 0))));
        return !(isOnFieldCenterLineHorizontal || isNotAnEdgePointOnFieldCentralVerticalLine);
    }

    private java.util.List<ch.zhaw.hexboard.Edge> constructEdgesOfField(java.awt.Point position) {
        ch.zhaw.hexboard.Edge[] e = new ch.zhaw.hexboard.Edge[6];
        e[0] = new ch.zhaw.hexboard.Edge(new java.awt.Point(position.x, position.y - 2), new java.awt.Point(position.x + 1, position.y - 1));
        e[1] = new ch.zhaw.hexboard.Edge(new java.awt.Point(position.x + 1, position.y - 1), new java.awt.Point(position.x + 1, position.y + 1));
        e[2] = new ch.zhaw.hexboard.Edge(new java.awt.Point(position.x + 1, position.y + 1), new java.awt.Point(position.x, position.y + 2));
        e[3] = new ch.zhaw.hexboard.Edge(new java.awt.Point(position.x, position.y + 2), new java.awt.Point(position.x - 1, position.y + 1));
        e[4] = new ch.zhaw.hexboard.Edge(new java.awt.Point(position.x - 1, position.y + 1), new java.awt.Point(position.x - 1, position.y - 1));
        e[5] = new ch.zhaw.hexboard.Edge(new java.awt.Point(position.x - 1, position.y - 1), new java.awt.Point(position.x, position.y - 2));
        return java.util.Arrays.asList(e);
    }

    private static java.util.List<java.awt.Point> getCornerCoordinatesOfField(java.awt.Point position) {
        java.awt.Point[] corner = new java.awt.Point[6];
        corner[0] = new java.awt.Point(position.x, position.y - 2);
        corner[1] = new java.awt.Point(position.x + 1, position.y - 1);
        corner[2] = new java.awt.Point(position.x + 1, position.y + 1);
        corner[3] = new java.awt.Point(position.x, position.y + 2);
        corner[4] = new java.awt.Point(position.x - 1, position.y - 1);
        corner[5] = new java.awt.Point(position.x - 1, position.y + 1);
        return java.util.Collections.unmodifiableList(java.util.Arrays.asList(corner));
    }

    protected static java.util.List<java.awt.Point> getAdjacentCorners(java.awt.Point position) {
        java.awt.Point[] corner = new java.awt.Point[3];
        if ((position.y % 3) == 0) {
            corner[0] = new java.awt.Point(position.x, position.y - 2);
            corner[1] = new java.awt.Point(position.x + 1, position.y + 1);
            corner[2] = new java.awt.Point(position.x - 1, position.y + 1);
        } else {
            corner[0] = new java.awt.Point(position.x, position.y + 2);
            corner[1] = new java.awt.Point(position.x + 1, position.y - 1);
            corner[2] = new java.awt.Point(position.x - 1, position.y - 1);
        }
        return java.util.Collections.unmodifiableList(java.util.Arrays.asList(corner));
    }

    /**
     * Returns all non-null corner data elements.
     *
     * @return the non-null corner data elements
     */
    public java.util.List<C> getCorners() {
        java.util.List<C> result = new java.util.LinkedList<>();
        for (C c : this.corner.values()) {
            if (c != null) {
                result.add(c);
            }
        }
        return java.util.Collections.unmodifiableList(result);
    }

    protected java.util.Set<java.awt.Point> getCornerCoordinates() {
        return java.util.Collections.unmodifiableSet(this.corner.keySet());
    }

    private static java.util.List<java.awt.Point> getAdjacentFields(java.awt.Point corner) {
        java.awt.Point[] field = new java.awt.Point[3];
        if ((corner.y % 3) == 0) {
            field[0] = new java.awt.Point(corner.x, corner.y + 2);
            field[1] = new java.awt.Point(corner.x + 1, corner.y - 1);
            field[2] = new java.awt.Point(corner.x - 1, corner.y - 1);
        } else {
            field[0] = new java.awt.Point(corner.x, corner.y - 2);
            field[1] = new java.awt.Point(corner.x + 1, corner.y + 1);
            field[2] = new java.awt.Point(corner.x - 1, corner.y + 1);
        }
        return java.util.Collections.unmodifiableList(java.util.Arrays.asList(field));
    }

    /**
     * Returns the data for the field denoted by the point.
     *
     * @param center
     * 		the location of the field
     * @return the stored data (or null)
     * @throws IllegalArgumentException
     * 		if the requested field does not exist
     */
    public F getField(java.awt.Point center) {
        if (field.containsKey(center)) {
            return field.get(center);
        } else {
            throw new java.lang.IllegalArgumentException("No field exists at these coordinates: " + center);
        }
    }

    /**
     * Returns the fields with non-null data elements.
     *
     * @return the list with the (non-null) field data
     */
    public java.util.List<java.awt.Point> getFields() {
        java.util.List<java.awt.Point> result = new java.util.LinkedList<>();
        for (java.util.Map.Entry<java.awt.Point, F> e : field.entrySet()) {
            if (e.getValue() != null) {
                result.add(e.getKey());
            }
        }
        return java.util.Collections.unmodifiableList(result);
    }

    /**
     * Returns the field data of the fields that touch this corner.
     * <p>
     * If the specified corner is not a corner or none of the fields that touch this
     * corner have a non-null data element, an empty list is returned.
     * </p>
     *
     * @param corner
     * 		the location of the corner
     * @return the list with the (non-null) field data
     */
    public java.util.List<F> getFields(java.awt.Point corner) {
        java.util.List<F> result = new java.util.LinkedList<>();
        if (ch.zhaw.hexboard.HexBoard.isCornerCoordinate(corner)) {
            for (java.awt.Point f : ch.zhaw.hexboard.HexBoard.getAdjacentFields(corner)) {
                if (field.get(f) != null) {
                    result.add(field.get(f));
                }
            }
        }
        return java.util.Collections.unmodifiableList(result);
    }

    /**
     * Returns the data for the edge denoted by the two points.
     *
     * @param p1
     * 		first point
     * @param p2
     * 		second point
     * @return the stored data (or null)
     */
    public E getEdge(java.awt.Point p1, java.awt.Point p2) {
        ch.zhaw.hexboard.Edge e = new ch.zhaw.hexboard.Edge(p1, p2);
        if (edge.containsKey(e)) {
            return edge.get(e);
        } else {
            return null;
        }
    }

    /**
     * Stores the data for the edge denoted by the two points.
     *
     * @param p1
     * 		first point
     * @param p2
     * 		second point
     * @param data
     * 		the data to be stored
     * @throws IllegalArgumentException
     * 		if the two points do not identify an
     * 		EXISTING edge of the field
     */
    public void setEdge(java.awt.Point p1, java.awt.Point p2, E data) {
        ch.zhaw.hexboard.Edge e = new ch.zhaw.hexboard.Edge(p1, p2);
        if (edge.containsKey(e)) {
            edge.put(e, data);
        } else {
            throw new java.lang.IllegalArgumentException("Edge does not exist => no data can be stored: " + e);
        }
    }

    /**
     * Returns the data for the corner denoted by the point.
     *
     * @param location
     * 		the location of the corner
     * @return the data stored for this node (or null)
     * @throws IllegalArgumentException
     * 		if the requested corner does not exist
     */
    public C getCorner(java.awt.Point location) {
        if (corner.containsKey(location)) {
            return corner.get(location);
        } else {
            throw new java.lang.IllegalArgumentException("No corner exists at the coordinates: " + location);
        }
    }

    /**
     * Stores the data for the edge denoted by the two points.
     *
     * @param location
     * 		the location of the corner
     * @param data
     * 		the data to be stored
     * @return the old data entry (or null)
     * @throws IllegalArgumentException
     * 		if there is no corner at this location
     */
    public C setCorner(java.awt.Point location, C data) {
        C old = corner.get(location);
        if (corner.containsKey(location)) {
            corner.put(location, data);
            return old;
        } else {
            throw new java.lang.IllegalArgumentException("Corner does not exist => no data can be stored: " + location);
        }
    }

    /**
     * Returns the (non-null) corner data elements of the corners that are direct
     * neighbors of the specified corner.
     * <p>
     * Each corner has three direct neighbors, except corners that are located at
     * the border of the game board.
     * </p>
     *
     * @param center
     * 		the location of the corner for which to return the direct
     * 		neighbors
     * @return list with non-null corner data elements
     */
    public java.util.List<C> getNeighboursOfCorner(java.awt.Point center) {
        java.util.List<C> result = new java.util.LinkedList<>();
        for (java.awt.Point c : ch.zhaw.hexboard.HexBoard.getAdjacentCorners(center)) {
            C temp = corner.get(c);
            if (temp != null) {
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * Returns the (non-null) edge data elements of the edges that directly connect
     * to that corner.
     * <p>
     * Each corner has three edges connecting to it, except edges that are located
     * at the border of the game board.
     * </p>
     *
     * @param corner
     * 		corner for which to get the edges
     * @return list with non-null edge data elements of edges connecting to the
    specified edge
     */
    public java.util.List<E> getAdjacentEdges(java.awt.Point corner) {
        java.util.List<E> result = new java.util.LinkedList<>();
        for (java.util.Map.Entry<ch.zhaw.hexboard.Edge, E> e : this.edge.entrySet()) {
            if (e.getKey().isEdgePoint(corner) && (e.getValue() != null)) {
                result.add(e.getValue());
            }
        }
        return result;
    }

    /**
     * Returns the (non-null) data elements of the corners of the specified field.
     *
     * @param center
     * 		the location of the field
     * @return list with non-null corner data elements
     */
    public java.util.List<C> getCornersOfField(java.awt.Point center) {
        java.util.List<C> result = new java.util.LinkedList<>();
        for (java.awt.Point c : ch.zhaw.hexboard.HexBoard.getCornerCoordinatesOfField(center)) {
            C temp = getCorner(c);
            if (temp != null) {
                result.add(temp);
            }
        }
        return result;
    }

    int getMaxCoordinateX() {
        return maxCoordinateX;
    }

    int getMaxCoordinateY() {
        return maxCoordinateY;
    }

    /**
     * Checks whether there is a corner at that specified location.
     *
     * @param location
     * 		the location to check
     * @return true, if there is a corner at this location
     */
    public boolean hasCorner(java.awt.Point location) {
        if (!ch.zhaw.hexboard.HexBoard.isCornerCoordinate(location)) {
            return false;
        }
        return corner.containsKey(location);
    }

    /**
     * Checks whether there is an edge between the two points.
     *
     * @param p1
     * 		first point
     * @param p2
     * 		second point
     * @return true, if there is an edge between the two points
     */
    public boolean hasEdge(java.awt.Point p1, java.awt.Point p2) {
        if (ch.zhaw.hexboard.Edge.isEdge(p1, p2)) {
            return edge.containsKey(new ch.zhaw.hexboard.Edge(p1, p2));
        } else {
            return false;
        }
    }

    static boolean isCorner(java.awt.Point field, java.awt.Point corner) {
        return ch.zhaw.hexboard.HexBoard.isFieldCoordinate(field) && ch.zhaw.hexboard.HexBoard.getCornerCoordinatesOfField(field).contains(corner);
    }
}