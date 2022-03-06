package ch.zhaw.catan.interaction;

import org.beryx.textio.EnumInputReader;

import ch.zhaw.catan.Config.Resource;
import ch.zhaw.catan.SiedlerGame;
import ch.zhaw.catan.InventoryPrinter;

/**
 * Provides the functionality to initialize a trade.
 *
 * Currently, the only trades allowed are trades with the bank.
 */
public class TradeAction extends Action {
    /**
     * Initializes a new instance of the {@link TradeAction} class.
     *
     * @param game The game this action belongs to.
     */
    public TradeAction(SiedlerGame game) {
        super(game);
    }

    @Override
    public boolean execute() {
        InventoryPrinter helper = new InventoryPrinter(getGame());
        helper.printPlayerInventory(getIO().getTextTerminal());
        EnumInputReader<Resource> reader = getIO().newEnumInputReader(Resource.class);
        Resource offer = reader.read("Which resource do you want to offer?");
        Resource wanted = reader.read("Which resource do you want in return?");
        boolean result = getGame().tradeWithBankFourToOne(offer, wanted);

        if (result) {
            getIO().getTextTerminal().printf("You successfully traded 4 %s for 1 %s\n", offer.name(), wanted.name());
        } else {
            getIO().getTextTerminal().printf(
                    "The trade was not successful. You don't have enough resources to trade 4 %s for 1 %s\n",
                    offer.name(), wanted.name());
        }

        helper.printPlayerInventory(getIO().getTextTerminal());
        return result;
    }

}
