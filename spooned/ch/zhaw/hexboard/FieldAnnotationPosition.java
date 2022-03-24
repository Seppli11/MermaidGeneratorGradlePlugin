package ch.zhaw.hexboard;
/**
 * This class models an annotation for the hex-fields of the hex-grid defined
 * by @see ch.zhaw.hexboard.HexBoard
 *
 * @author tebe
 */
final class FieldAnnotationPosition {
    private java.awt.Point field;

    private java.awt.Point corner;

    /**
     * Creates a field annotation for the specified field.
     *
     * @param field
     * 		the field to be annotated
     * @param corner
     * 		the location of the annotation
     * @throws IllegalArgumentException
     * 		if arguments are null or not valid
     * 		field/corner coordinates (@see
     * 		ch.zhaw.hexboard.HexBoard).
     */
    public FieldAnnotationPosition(java.awt.Point field, java.awt.Point corner) {
        if (ch.zhaw.hexboard.HexBoard.isCorner(field, corner)) {
            this.field = field;
            this.corner = corner;
        } else {
            throw new java.lang.IllegalArgumentException(((("" + field) + " is not a field coordinate or ") + corner) + " is not a corner of the field.");
        }
    }

    /**
     * Checks whether the provided coordinate matches the position of the annotation
     * within the field.
     *
     * @param p
     * 		the corner coordinate
     * @return true, if they match
     */
    public boolean isCorner(java.awt.Point p) {
        return corner.equals(p);
    }

    /**
     * Checks whether the provided coordinate matches the field coordinate of this
     * annotation.
     *
     * @param p
     * 		a field coordinate
     * @return true, if they match
     */
    public boolean isField(java.awt.Point p) {
        return field.equals(p);
    }

    /* (non-Javadoc)

    @see java.lang.Object#hashCode()
     */
    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (field == null ? 0 : field.hashCode());
        result = (prime * result) + (corner == null ? 0 : corner.hashCode());
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
        ch.zhaw.hexboard.FieldAnnotationPosition other = ((ch.zhaw.hexboard.FieldAnnotationPosition) (obj));
        if (field == null) {
            if (other.field != null) {
                return false;
            }
        } else if (!field.equals(other.field)) {
            return false;
        }
        if (corner == null) {
            if (other.corner != null) {
                return false;
            }
        } else if (!corner.equals(other.corner)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)

    @see java.lang.Object#toString()
     */
    @java.lang.Override
    public java.lang.String toString() {
        return ((("FieldAnnotationPosition [field=" + field) + ", corner=") + corner) + "]";
    }
}