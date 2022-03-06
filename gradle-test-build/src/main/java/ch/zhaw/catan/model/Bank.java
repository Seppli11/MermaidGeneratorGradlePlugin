package ch.zhaw.catan.model;

import java.util.Map;

import ch.zhaw.catan.Config.Resource;

/**
 * Represents a bank with its inventory
 * 
 * @author Sebastian Zumbrunn
 */
public class Bank extends ResourceHolder {
    /**
     * Constructor
     */
    public Bank() {
    }

    /**
     * Constructor
     * 
     * @param resources the initial inventory
     */
    public Bank(Map<Resource, Integer> resources) {
        super(resources);
    }
}
