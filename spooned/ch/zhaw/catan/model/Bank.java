package ch.zhaw.catan.model;
/**
 * Represents a bank with its inventory
 *
 * @author Sebastian Zumbrunn
 */
public class Bank extends ch.zhaw.catan.model.ResourceHolder {
    /**
     * Constructor
     */
    public Bank() {
    }

    /**
     * Constructor
     *
     * @param resources
     * 		the initial inventory
     */
    public Bank(java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> resources) {
        super(resources);
    }
}