package ch.zhaw.hexboard;
/**
 * This class can be used to get a textual representation of a hex-grid modeled
 * by {@link ch.zhaw.hexboard.HexBoard}.
 * <p>
 * It creates a textual representation of the {@link ch.zhaw.hexboard.HexBoard}
 * that includes all defined fields, edges, corners and annotations.
 * </p>
 * The generation of the textual representation is basically working on a line
 * by line basis. Thereby, the two text lines needed for the diagonal edges are
 * treated like "one line" in that they are created in one step together.
 * <p>
 * The textual representation does not contain the hex-grid as such but only the
 * fields that actually exist on the hex-board. Note that if a field exists,
 * also its corners and edges exist and are therefore shown in the textual
 * representation.
 * </p>
 * <p>
 * This class defines how edges, corners and fields look like (their "label").
 * This is done as follows:</p>
 * <ul>
 * <li>If there is no data object associated with an edge, corner or field,
 * their default representation is used. Note that the default representation of
 * an edge depends on its direction (see below).</li>
 * <li>If there is a data object associated with an edge, corner or field, the
 * {@link ch.zhaw.hexboard.Label} is determined by calling:
 * <ul>
 * <li>EL = {@link #getEdgeLabel(Object)}</li>
 * <li>CL = {@link #getCornerLabel(Object)}</li>
 * <li>UL = {@link #getFieldLabelUpper(Object)}</li>
 * </ul>
 * </li>
 * </ul>
 * <p>In addition to edges, corners and field labels, the hex-board's field
 * annotations are included too. If an annotation exists for one of the corners
 * (N, NW, SW, S, SE, NE), which means that an associated data object exists, it
 * is turned into a {@link ch.zhaw.hexboard.Label} with
 * {@link #getAnnotationLabel(Object)}.</p>
 * <br>
 * <p>Two examples of how that looks like are shown below. The first example shows
 * a case with all edges, corners and the field with no data associated with
 * them. The second one has all edges corner and the upper field label defined
 * by calling the corresponding method for creating the Label for the associated
 * data object.</p>
 *
 * <pre>
 *        DEFAULT                LABELS FROM DATA
 *
 *          (  )                       (CL)
 *        //    \\                   EL    EL
 *    //            \\           EL     N      EL
 * (  )              (  )     (CL) NW        NE (CL)
 *  ||                ||       EL       UL       EL
 *  ||                ||       EL                EL
 * (  )              (  )     (CL) SW        SE (CL)
 *    \\            //           EL     S      EL
 *        \\    //                   EL    EL
 *          (  )                       (CL)
 * </pre>
 * <br>
 * <p>To override the default behavior, which creates a Label using the two first
 * characters of the string returned by the toString() method of the
 * edge/corner/field data object, you might override the respective methods.
 * </p>
 * <br>
 * <p>
 * Finally, a field can be labeled with a lower label (LL) by providing a map of
 * field coordinates and associated labels. An example of a representation with
 * all field annotations, corner labels and field labels defined but default
 * edges is the following:</p>
 *
 * <pre>
 *          (CL)
 *        // N  \\
 *    //            \\
 * (CL) NW        NE (CL)
 *  ||       UL       ||
 *  ||       LL       ||
 * (CL) SW        SE (CL)
 *    \\     S      //
 *        \\    //
 *          (CL)
 * </pre>
 *
 * @param <F>
 * 		See {@link ch.zhaw.hexboard.HexBoard}
 * @param <C>
 * 		See {@link ch.zhaw.hexboard.HexBoard}
 * @param <E>
 * 		See {@link ch.zhaw.hexboard.HexBoard}
 * @param <A>
 * 		See {@link ch.zhaw.hexboard.HexBoard}
 * @author tebe
 */
public class HexBoardTextView<F, C, E, A> {
    private static final java.lang.String ONE_SPACE = " ";

    private static final java.lang.String TWO_SPACES = "  ";

    private static final java.lang.String FOUR_SPACES = "    ";

    private static final java.lang.String FIVE_SPACES = "     ";

    private static final java.lang.String SIX_SPACES = "     ";

    private static final java.lang.String SEVEN_SPACES = "      ";

    private static final java.lang.String NINE_SPACES = "        ";

    private final ch.zhaw.hexboard.HexBoard<F, C, E, A> board;

    private final ch.zhaw.hexboard.Label emptyLabel = new ch.zhaw.hexboard.Label(' ', ' ');

    private final ch.zhaw.hexboard.Label defaultDiagonalEdgeDownLabel = new ch.zhaw.hexboard.Label('\\', '\\');

    private final ch.zhaw.hexboard.Label defaultDiagonalEdgeUpLabel = new ch.zhaw.hexboard.Label('/', '/');

    private final ch.zhaw.hexboard.Label defaultVerticalEdgeLabel = new ch.zhaw.hexboard.Label('|', '|');

    private java.util.Map<java.awt.Point, ch.zhaw.hexboard.Label> fixedLowerFieldLabels;

    /**
     * Creates a view for the specified board.
     *
     * @param board
     * 		the board
     */
    public HexBoardTextView(ch.zhaw.hexboard.HexBoard<F, C, E, A> board) {
        this.fixedLowerFieldLabels = new java.util.HashMap<>();
        this.board = board;
    }

    /**
     * Sets the lower field label for the specified field.
     *
     * @param field
     * 		the field
     * @param label
     * 		the label
     * @throws IllegalArgumentException
     * 		if arguments are null or if the field does
     * 		not exist
     */
    public void setLowerFieldLabel(java.awt.Point field, ch.zhaw.hexboard.Label label) {
        if (((field == null) || (label == null)) || (!board.hasField(field))) {
            throw new java.lang.IllegalArgumentException("Argument(s) must not be null and field must exist.");
        }
        fixedLowerFieldLabels.put(field, label);
    }

    /**
     * Returns a label to be used as label for the edge. This method is called to
     * determine the label for this edge.
     *
     * @param e
     * 		edge data object
     * @return the label
     */
    protected ch.zhaw.hexboard.Label getEdgeLabel(E e) {
        return deriveLabelFromToStringRepresentation(e);
    }

    /**
     * Returns a label to be used as label for the corner. This method is called to
     * determine the label for this corner.
     *
     * @param c
     * 		corner data object
     * @return the label
     */
    protected ch.zhaw.hexboard.Label getCornerLabel(C c) {
        return deriveLabelFromToStringRepresentation(c);
    }

    /**
     * Returns a label to be used as upper label for the field. This method is
     * called to determine the upper label for this field.
     *
     * @param f
     * 		field data object
     * @return the label
     */
    protected ch.zhaw.hexboard.Label getFieldLabelUpper(F f) {
        return deriveLabelFromToStringRepresentation(f);
    }

    /**
     * Returns a label to be used as lower label for the field at this position.
     * This method is called to determine the lower label for this field.
     *
     * @param p
     * 		location of the field
     * @return the label
     */
    private ch.zhaw.hexboard.Label getFieldLabelLower(java.awt.Point p) {
        ch.zhaw.hexboard.Label l = this.fixedLowerFieldLabels.get(p);
        l = (l == null) ? emptyLabel : l;
        return l;
    }

    private ch.zhaw.hexboard.Label deriveLabelFromToStringRepresentation(java.lang.Object o) {
        ch.zhaw.hexboard.Label label = emptyLabel;
        if (o.toString().length() > 0) {
            java.lang.String s = o.toString();
            if (s.length() > 1) {
                return new ch.zhaw.hexboard.Label(s.charAt(0), s.charAt(1));
            } else {
                return new ch.zhaw.hexboard.Label(s.charAt(0), ' ');
            }
        }
        return label;
    }

    /**
     * <p>
     * This method returns a single-line string with all corners and field
     * annotations for a given y-coordinate. It produces the string by iterating
     * over corner positions and appending per corner:
     * </p>
     * <p>
     * "(CL) NE NW " for y%3==1 "(CL) SE SW " for y%3==0
     * </p>
     * <p>
     * Corners/labels that do not exist are replaced by spaces.
     * </p>
     */
    private java.lang.String printCornerLine(int y) {
        java.lang.StringBuilder cornerLine = new java.lang.StringBuilder("");
        int offset = 0;
        if ((y % 2) != 0) {
            cornerLine.append(ch.zhaw.hexboard.HexBoardTextView.NINE_SPACES);
            offset = 1;
        }
        for (int x = offset; x <= board.getMaxCoordinateX(); x = x + 2) {
            java.awt.Point p = new java.awt.Point(x, y);
            ch.zhaw.hexboard.Label cornerLabel;
            // handle corner labels for corners other than north and south corners
            java.awt.Point center;
            ch.zhaw.hexboard.Label first = null;
            ch.zhaw.hexboard.Label second = null;
            switch (y % 3) {
                case 0 :
                    center = new java.awt.Point(x + 1, y - 1);
                    first = this.getAnnotationLabel(board.getFieldAnnotation(center, new java.awt.Point(center.x - 1, center.y + 1)));
                    second = this.getAnnotationLabel(board.getFieldAnnotation(center, new java.awt.Point(center.x + 1, center.y + 1)));
                    break;
                case 1 :
                    center = new java.awt.Point(x + 1, y + 1);
                    first = this.getAnnotationLabel(board.getFieldAnnotation(center, new java.awt.Point(center.x - 1, center.y - 1)));
                    second = this.getAnnotationLabel(board.getFieldAnnotation(center, new java.awt.Point(center.x + 1, center.y - 1)));
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("Not a corner line");
            }
            if (board.hasCorner(p)) {
                cornerLabel = (board.getCorner(p) != null) ? getCornerLabel(board.getCorner(p)) : emptyLabel;
                cornerLine.append("(").append(cornerLabel.getFirst()).append(cornerLabel.getSecond()).append(")");
            } else {
                cornerLine.append(ch.zhaw.hexboard.HexBoardTextView.FOUR_SPACES);
            }
            cornerLine.append(ch.zhaw.hexboard.HexBoardTextView.ONE_SPACE).append(first.getFirst()).append(first.getSecond());
            cornerLine.append(ch.zhaw.hexboard.HexBoardTextView.FIVE_SPACES).append(second.getFirst()).append(second.getSecond()).append(ch.zhaw.hexboard.HexBoardTextView.TWO_SPACES);
        }
        return cornerLine.toString();
    }

    private ch.zhaw.hexboard.Label getAnnotationLabel(A annotation) {
        if (annotation == null) {
            return emptyLabel;
        } else {
            return deriveLabelFromToStringRepresentation(annotation);
        }
    }

    private java.lang.String printMiddlePartOfField(int y) {
        boolean isOffsetRow = ((y - 2) % 6) == 0;
        java.lang.StringBuilder lower = new java.lang.StringBuilder(isOffsetRow ? ch.zhaw.hexboard.HexBoardTextView.NINE_SPACES : "");
        java.lang.StringBuilder upper = new java.lang.StringBuilder(isOffsetRow ? ch.zhaw.hexboard.HexBoardTextView.NINE_SPACES : "");
        int xstart = (isOffsetRow) ? 2 : 1;
        for (int x = xstart; x <= (board.getMaxCoordinateX() + 1); x = x + 2) {
            java.awt.Point edgeStart = new java.awt.Point(x - 1, y - 1);
            java.awt.Point edgeEnd = new java.awt.Point(x - 1, y + 1);
            ch.zhaw.hexboard.Label l = this.emptyLabel;
            if (board.hasEdge(edgeStart, edgeEnd)) {
                E edge = board.getEdge(edgeStart, edgeEnd);
                if (edge != null) {
                    l = this.getEdgeLabel(edge);
                } else {
                    l = this.defaultVerticalEdgeLabel;
                }
            }
            java.awt.Point center = new java.awt.Point(x, y);
            boolean hasFieldWithData = board.hasField(center) && (board.getField(center) != null);
            ch.zhaw.hexboard.Label lowerFieldLabel = (hasFieldWithData) ? getFieldLabelLower(center) : emptyLabel;
            ch.zhaw.hexboard.Label upperFieldLabel = (hasFieldWithData) ? getFieldLabelUpper(board.getField(center)) : emptyLabel;
            lower.append(ch.zhaw.hexboard.HexBoardTextView.ONE_SPACE).append(l.getFirst()).append(l.getSecond()).append(ch.zhaw.hexboard.HexBoardTextView.SEVEN_SPACES);
            lower.append(lowerFieldLabel.getFirst()).append(lowerFieldLabel.getSecond()).append(ch.zhaw.hexboard.HexBoardTextView.SIX_SPACES);
            upper.append(ch.zhaw.hexboard.HexBoardTextView.ONE_SPACE).append(l.getFirst()).append(l.getSecond()).append(ch.zhaw.hexboard.HexBoardTextView.SEVEN_SPACES);
            upper.append(upperFieldLabel.getFirst()).append(upperFieldLabel.getSecond()).append(ch.zhaw.hexboard.HexBoardTextView.SIX_SPACES);
        }
        return (upper + java.lang.System.lineSeparator()) + lower;
    }

    private java.lang.String printDiagonalEdges(int y) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        java.awt.Point edgeStart;
        java.awt.Point edgeEnd;
        ch.zhaw.hexboard.Label annotation = null;
        ch.zhaw.hexboard.Label l;
        boolean isDown = (y % 6) == 0;
        builder.append("   ");
        for (int x = 0; x <= board.getMaxCoordinateX(); x = x + 1) {
            if (isDown) {
                edgeStart = new java.awt.Point(x, y);
                edgeEnd = new java.awt.Point(x + 1, y + 1);
                annotation = getAnnotationLabel(board.getFieldAnnotation(new java.awt.Point(x + 1, y - 1), new java.awt.Point(x + 1, y + 1)));
            } else {
                edgeStart = new java.awt.Point(x, y + 1);
                edgeEnd = new java.awt.Point(x + 1, y);
                annotation = getAnnotationLabel(board.getFieldAnnotation(new java.awt.Point(x + 1, y + 2), new java.awt.Point(x + 1, y)));
            }
            l = determineEdgeLabel(isDown, edgeStart, edgeEnd);
            if (!isDown) {
                builder.append(((((ch.zhaw.hexboard.HexBoardTextView.TWO_SPACES + l.getFirst()) + l.getSecond()) + ch.zhaw.hexboard.HexBoardTextView.TWO_SPACES) + annotation.getFirst()) + annotation.getSecond());
            } else {
                builder.append(((((ch.zhaw.hexboard.HexBoardTextView.TWO_SPACES + l.getFirst()) + l.getSecond()) + ch.zhaw.hexboard.HexBoardTextView.TWO_SPACES) + annotation.getFirst()) + annotation.getSecond());
            }
            isDown = !isDown;
        }
        return builder.toString();
    }

    private ch.zhaw.hexboard.Label determineEdgeLabel(boolean isDown, java.awt.Point edgeStart, java.awt.Point edgeEnd) {
        ch.zhaw.hexboard.Label l;
        if (board.hasEdge(edgeStart, edgeEnd)) {
            // does it have data associated with it?
            if (board.getEdge(edgeStart, edgeEnd) != null) {
                l = this.getEdgeLabel(board.getEdge(edgeStart, edgeEnd));
            } else {
                // default visualization
                l = (isDown) ? this.defaultDiagonalEdgeDownLabel : this.defaultDiagonalEdgeUpLabel;
            }
        } else {
            l = this.emptyLabel;
        }
        return l;
    }

    /* (non-Javadoc)

    @see java.lang.Object#toString()
     */
    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int y = 0; y <= board.getMaxCoordinateY(); y = y + 3) {
            sb.append(printCornerLine(y));
            sb.append(java.lang.System.lineSeparator());
            sb.append(printDiagonalEdges(y));
            sb.append(java.lang.System.lineSeparator());
            sb.append(printCornerLine(y + 1));
            sb.append(java.lang.System.lineSeparator());
            sb.append(printMiddlePartOfField(y + 2));
            sb.append(java.lang.System.lineSeparator());
        }
        return sb.toString();
    }
}