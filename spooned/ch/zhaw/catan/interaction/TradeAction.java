package ch.zhaw.catan.interaction;
import org.beryx.textio.EnumInputReader;
/**
 * Provides the functionality to initialize a trade.
 *
 * Currently, the only trades allowed are trades with the bank.
 */
public class TradeAction extends ch.zhaw.catan.interaction.Action {
    /**
     * Initializes a new instance of the {@link TradeAction} class.
     *
     * @param game
     * 		The game this action belongs to.
     */
    public TradeAction(ch.zhaw.catan.SiedlerGame game) {
        super(game);
    }

    @java.lang.Override
    public boolean execute() {
        ch.zhaw.catan.InventoryPrinter helper = new ch.zhaw.catan.InventoryPrinter(getGame());
        helper.printPlayerInventory(getIO().getTextTerminal());
        org.beryx.textio.EnumInputReader<ch.zhaw.catan.Config.Resource> reader = getIO().newEnumInputReader(ch.zhaw.catan.Config.Resource.class);
        ch.zhaw.catan.Config.Resource offer = reader.read("Which resource do you want to offer?");
        ch.zhaw.catan.Config.Resource wanted = reader.read("Which resource do you want in return?");
        boolean result = getGame().tradeWithBankFourToOne(offer, wanted);
        if (result) {
            getIO().getTextTerminal().printf("You successfully traded 4 %s for 1 %s\n", offer.name(), wanted.name());
        } else {
            getIO().getTextTerminal().printf("The trade was not successful. You don\'t have enough resources to trade 4 %s for 1 %s\n", offer.name(), wanted.name());
        }
        helper.printPlayerInventory(getIO().getTextTerminal());
        return result;
    }
}