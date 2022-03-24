package ch.zhaw.catan;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
public class Dummy {
    public enum Actions {

        SHOW,
        QUIT;}

    private void run() {
        ch.zhaw.catan.SiedlerGame game = new ch.zhaw.catan.SiedlerGame(5, 3);
        org.beryx.textio.TextIO textIO = org.beryx.textio.TextIoFactory.getTextIO();
        org.beryx.textio.TextTerminal<?> textTerminal = textIO.getTextTerminal();
        ch.zhaw.catan.SiedlerBoard board = game.getBoard();
        java.util.Map<java.awt.Point, ch.zhaw.hexboard.Label> lowerFieldLabel = new java.util.HashMap<>();
        lowerFieldLabel.put(new java.awt.Point(2, 2), new ch.zhaw.hexboard.Label('0', '9'));
        ch.zhaw.catan.SiedlerBoardTextView view = new ch.zhaw.catan.SiedlerBoardTextView(board);
        for (java.util.Map.Entry<java.awt.Point, ch.zhaw.hexboard.Label> e : lowerFieldLabel.entrySet()) {
            view.setLowerFieldLabel(e.getKey(), e.getValue());
        }
        boolean running = true;
        while (running) {
            switch (ch.zhaw.catan.Dummy.getEnumValue(textIO, ch.zhaw.catan.Dummy.Actions.class)) {
                case SHOW :
                    textTerminal.println(view.toString());
                    break;
                case QUIT :
                    running = false;
                    break;
                default :
                    throw new java.lang.IllegalStateException("Internal error found - Command not implemented.");
            }
        } 
        textIO.dispose();
    }

    public static <T extends java.lang.Enum<T>> T getEnumValue(org.beryx.textio.TextIO textIO, java.lang.Class<T> commands) {
        return textIO.newEnumInputReader(commands).read("What would you like to do?");
    }

    public static void main(java.lang.String[] args) {
        new ch.zhaw.catan.Dummy().run();
    }
}