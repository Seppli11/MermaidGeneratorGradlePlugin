package ch.zhaw.catan.model;
/**
 * This class represents a field on the game board. It also stores information
 * about said field such as its type and dice value.
 *
 * @author Jonas Costa
 */
public class SiedlerField {
    /**
     * Specifies what type of field this is.
     */
    private ch.zhaw.catan.Config.Land fieldType;

    /**
     * Specifies what number must be rolled to retrieve resources from this field.
     */
    private int diceValue;

    /**
     * Creates a new game board field.
     *
     * @param fieldType
     * 		Which resource can be received from this field.
     * @param diceValue
     * 		Which value must be rolled to receive resource cards.
     */
    public SiedlerField(ch.zhaw.catan.Config.Land fieldType, int diceValue) {
        this.fieldType = fieldType;
        this.diceValue = diceValue;
    }

    /**
     * Gets field's dice value.
     *
     * @return Which value must be rolled to receive resource cards.
     */
    public int getDiceValue() {
        return diceValue;
    }

    /**
     * Gets a field's type.
     *
     * @return Which resource will be distributed when a matching dice value is
    rolling.
     */
    public ch.zhaw.catan.Config.Land getFieldType() {
        return fieldType;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(diceValue, fieldType);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof ch.zhaw.catan.model.SiedlerField))
            return false;

        ch.zhaw.catan.model.SiedlerField other = ((ch.zhaw.catan.model.SiedlerField) (obj));
        return (diceValue == other.diceValue) && (fieldType == other.fieldType);
    }
}