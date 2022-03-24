package ch.zhaw.catan.model;
/**
 * Abstract class which represents an entity in the game which has an inventory
 *
 * @author Sebastian Zumbrunn
 */
public abstract class ResourceHolder {
    /**
     * a map which contains the inventory
     */
    private java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> resources = new java.util.EnumMap<>(ch.zhaw.catan.Config.Resource.class);

    /**
     * Constructor
     */
    protected ResourceHolder() {
    }

    /**
     * Constructor
     *
     * @param resources
     * 		the initial inventory
     */
    protected ResourceHolder(java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> resources) {
        this.resources.putAll(resources);
    }

    /**
     * Returns the count of the given resource. If the resource holder doesn't hold
     * the given resource, 0 is returned
     *
     * @param resource
     * 		the resource
     * @return the count of the resource
     */
    public int getResourceCount(ch.zhaw.catan.Config.Resource resource) {
        return resources.getOrDefault(resource, 0);
    }

    /**
     *
     *
     * @return Gets the number of resources which are in possession of the player.
     */
    public int getAllResourcesCount() {
        return getResourceArray().length;
    }

    /**
     * Gets an array representation of the resources.
     *
     * @return An array representation of the resources.
     */
    public ch.zhaw.catan.Config.Resource[] getResourceArray() {
        java.util.List<ch.zhaw.catan.Config.Resource> result = new java.util.ArrayList<>();
        for (java.util.Map.Entry<ch.zhaw.catan.Config.Resource, java.lang.Integer> entry : resources.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                result.add(entry.getKey());
            }
        }
        return result.toArray(new ch.zhaw.catan.Config.Resource[0]);
    }

    /**
     * Sets the count of the given resource
     *
     * @param resource
     * 		the resource
     * @param count
     * 		a positiv count
     */
    public void setResource(ch.zhaw.catan.Config.Resource resource, int count) {
        resources.put(resource, count);
    }

    /**
     * Increments the given resource by the given amount
     *
     * @param resource
     * 		the resource to increment
     * @param by
     * 		by how much the count is incremented. Negative numbers are
     * 		allowed
     * @throws IllegalArgumentException
     * 		if the counter was decremented below zero
     */
    public void incrementResource(ch.zhaw.catan.Config.Resource resource, int by) {
        java.util.Objects.requireNonNull(resource, "Resource was null");
        int count = resources.getOrDefault(resource, 0) + by;
        if (count < 0) {
            throw new java.lang.IllegalArgumentException(("Counter was decremented to \"" + count) + "\" below zero");
        }
        resources.put(resource, count);
    }

    /**
     * Decrements the given resource by the given amount
     *
     * @param resource
     * 		the resource to decrement
     * @param by
     * 		by how much the count is decremented
     * @throws IllegalArgumentException
     * 		if the counter was decremented below zero
     */
    public void decrementResource(ch.zhaw.catan.Config.Resource resource, int by) {
        incrementResource(resource, -by);
    }

    /**
     * Checks whether the resource holder has the resources specified in the
     * {@code resourceMap}.
     *
     * @param resourceMap
     * 		The resources whose availability to check.
     * @return A value indicating whether the holder has the specified resources.
     */
    public boolean hasResources(java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Long> resourceMap) {
        for (java.util.Map.Entry<ch.zhaw.catan.Config.Resource, java.lang.Long> entry : resourceMap.entrySet()) {
            if (getResourceCount(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the resources in the specified {@code resourceMap} to the resource
     * holder.
     *
     * @param resourceMap
     * 		The resources to add to the resource holder.
     */
    public void incrementResources(java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Long> resourceMap) {
        for (java.util.Map.Entry<ch.zhaw.catan.Config.Resource, java.lang.Long> entry : resourceMap.entrySet()) {
            incrementResource(entry.getKey(), ((int) ((long) (entry.getValue()))));
        }
    }

    /**
     * Removes the resources in the specified {@code resourceMap} from the resource
     * holder.
     *
     * @param resourceMap
     * 		The resources to remove from the resource holder.
     */
    public void decrementResources(java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Long> resourceMap) {
        for (java.util.Map.Entry<ch.zhaw.catan.Config.Resource, java.lang.Long> entry : resourceMap.entrySet()) {
            decrementResource(entry.getKey(), ((int) ((long) (entry.getValue()))));
        }
    }

    /**
     *
     *
     * @return a copy of the resource map
     */
    protected java.util.Map<ch.zhaw.catan.Config.Resource, java.lang.Integer> getResourceMap() {
        return new java.util.EnumMap<>(resources);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(resources);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof ch.zhaw.catan.model.ResourceHolder))
            return false;

        ch.zhaw.catan.model.ResourceHolder other = ((ch.zhaw.catan.model.ResourceHolder) (obj));
        return java.util.Objects.equals(resources, other.resources);
    }
}