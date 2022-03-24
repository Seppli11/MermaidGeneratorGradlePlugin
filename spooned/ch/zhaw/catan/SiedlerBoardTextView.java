package ch.zhaw.catan;
import ch.zhaw.catan.model.SiedlerCity;
import ch.zhaw.catan.model.SiedlerSettlement;
import ch.zhaw.catan.model.SiedlerStructure;
import ch.zhaw.hexboard.HexBoardTextView;
import ch.zhaw.hexboard.Label;
/**
 * Renders the {@link SiedlerBoard} to a string.
 * The edge labels display the street. When a player occupies a street, their
 * color is displayed as the street. The corner labels are used for a
 * {@link SiedlerStructure}.
 * The field labels are used for the resource type of the field. The dice value
 * of the fields are displayed in the lower field label which are initialized in
 * the constructor.
 *
 * @author Sebastian Zumbrunn
 */
public class SiedlerBoardTextView extends ch.zhaw.hexboard.HexBoardTextView<ch.zhaw.catan.model.SiedlerField, ch.zhaw.catan.model.SiedlerSettlement, ch.zhaw.catan.model.SiedlerStreet, java.lang.Void> {
    /**
     * The char used for a settlement
     */
    private static final char SETTLEMENT_CHAR = 'S';

    /**
     * the char for a city
     */
    private static final char CITY_CHAR = 'C';

    /**
     * an empty label
     */
    private static final ch.zhaw.hexboard.Label EMPTY_LABEL = new ch.zhaw.hexboard.Label(' ', ' ');

    /**
     * the siedler board. We also have to store it, because {@link HexBoardTextView}
     * doesn't provide a getter
     */
    private ch.zhaw.catan.SiedlerBoard board;

    /**
     * Constructor
     *
     * @param board
     * 		the board
     */
    public SiedlerBoardTextView(ch.zhaw.catan.SiedlerBoard board) {
        super(board);
        this.board = board;
        setDiveValues();
    }

    /**
     * Sets all lower field labels with the dice values of the fields. As those
     * values do not change during the game,
     * this is done once when this text view is initially created.
     */
    private void setDiveValues() {
        for (java.awt.Point fieldPoint : board.getFields()) {
            ch.zhaw.catan.model.SiedlerField field = board.getField(fieldPoint);
            if (field.getDiceValue() >= 0) {
                setLowerFieldLabel(fieldPoint, getLabelForDiceValue(field.getDiceValue()));
            }
        }
    }

    @java.lang.Override
    protected ch.zhaw.hexboard.Label getCornerLabel(ch.zhaw.catan.model.SiedlerSettlement c) {
        if (c == null)
            return ch.zhaw.catan.SiedlerBoardTextView.EMPTY_LABEL;

        return getLabelForStructure(c);
    }

    @java.lang.Override
    protected ch.zhaw.hexboard.Label getEdgeLabel(ch.zhaw.catan.model.SiedlerStreet e) {
        if (e == null)
            return ch.zhaw.catan.SiedlerBoardTextView.EMPTY_LABEL;

        return getLabelForPlayer(e.getOwner());
    }

    @java.lang.Override
    protected ch.zhaw.hexboard.Label getFieldLabelUpper(ch.zhaw.catan.model.SiedlerField f) {
        if (f == null)
            return ch.zhaw.catan.SiedlerBoardTextView.EMPTY_LABEL;

        return createLabelFromObject(f.getFieldType());
    }

    /**
     * Creates a label which shows the player initials.
     *
     * @param player
     * 		the player
     * @return the created label
     */
    private ch.zhaw.hexboard.Label getLabelForPlayer(ch.zhaw.catan.model.Player player) {
        return createLabelFromObject(player.getFaction());
    }

    /**
     * Creates a label for structures. A {@link SiedlerCity} is prepended with C, a
     * {@link SiedlerSettlement} is prepended with a S.
     * The second character of the label is used for a player color letter.
     *
     * @param structure
     * 		the structure to create a label for
     * @return the created label
     */
    private ch.zhaw.hexboard.Label getLabelForStructure(ch.zhaw.catan.model.SiedlerSettlement structure) {
        char structureChar = ch.zhaw.catan.SiedlerBoardTextView.SETTLEMENT_CHAR;
        if (structure instanceof ch.zhaw.catan.model.SiedlerCity) {
            structureChar = ch.zhaw.catan.SiedlerBoardTextView.CITY_CHAR;
        }
        return new ch.zhaw.hexboard.Label(structureChar, getLabelForPlayer(structure.getOwner()).getSecond());
    }

    /**
     * Creates a label for the given dice value. The value is formatted with zero
     * padding and
     * can be maximal 2 digits long. If the int is longer, it is cut off
     *
     * @param diceValue
     * 		the dive value to display
     * @return the label
     */
    private ch.zhaw.hexboard.Label getLabelForDiceValue(int diceValue) {
        java.lang.String diceValueStr = java.lang.String.format("%02d", diceValue);
        return createLabelFromObject(diceValueStr);
    }

    /**
     * Creates a {@link Label} from the string representation of the given object.
     * This function is (almost) a carbon copy of
     * {@link HexBoardTextView#deriveLabelFromToStringRepresentation(Object)}.
     * However, this was necessary, as we are not allowed to modify
     *
     * {@link HexBoardTextView}.
     *
     * @param o
     * 		the object, from which a label is created
     * @return the label
     */
    private ch.zhaw.hexboard.Label createLabelFromObject(java.lang.Object o) {
        ch.zhaw.hexboard.Label label = ch.zhaw.catan.SiedlerBoardTextView.EMPTY_LABEL;
        if ((o != null) && (o.toString().length() > 0)) {
            java.lang.String s = o.toString();
            if (s.length() > 1) {
                return new ch.zhaw.hexboard.Label(s.charAt(0), s.charAt(1));
            } else {
                return new ch.zhaw.hexboard.Label(s.charAt(0), ' ');
            }
        }
        return label;
    }
}