package ch.zhaw.catan.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import ch.zhaw.catan.Config.Resource;

/**
 * Abstract class which represents an entity in the game which has an inventory
 * 
 * @author Sebastian Zumbrunn
 */
public abstract class ResourceHolder {
    /**
     * a map which contains the inventory
     */
    private Map<Resource, Integer> resources = new EnumMap<>(Resource.class);

    /**
     * Constructor
     */
    protected ResourceHolder() {
    }

    /**
     * Constructor
     * 
     * @param resources the initial inventory
     */
    protected ResourceHolder(Map<Resource, Integer> resources) {
        this.resources.putAll(resources);
    }

    /**
     * Returns the count of the given resource. If the resource holder doesn't hold
     * the given resource, 0 is returned
     * 
     * @param resource the resource
     * @return the count of the resource
     */
    public int getResourceCount(Resource resource) {
        return resources.getOrDefault(resource, 0);
    }

    /**
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
    public Resource[] getResourceArray() {
        List<Resource> result = new ArrayList<>();

        for (Entry<Resource, Integer> entry : resources.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                result.add(entry.getKey());
            }
        }

        return result.toArray(new Resource[0]);
    }

    /**
     * Sets the count of the given resource
     * 
     * @param resource the resource
     * @param count    a positiv count
     */
    public void setResource(Resource resource, int count) {
        resources.put(resource, count);
    }

    /**
     * Increments the given resource by the given amount
     * 
     * @param resource the resource to increment
     * @param by       by how much the count is incremented. Negative numbers are
     *                 allowed
     * @throws IllegalArgumentException if the counter was decremented below zero
     */
    public void incrementResource(Resource resource, int by) {
        Objects.requireNonNull(resource, "Resource was null");
        int count = resources.getOrDefault(resource, 0) + by;
        if (count < 0) {
            throw new IllegalArgumentException("Counter was decremented to \"" + count + "\" below zero");
        }
        resources.put(resource, count);
    }

    /**
     * Decrements the given resource by the given amount
     * 
     * @param resource the resource to decrement
     * @param by       by how much the count is decremented
     * @throws IllegalArgumentException if the counter was decremented below zero
     */
    public void decrementResource(Resource resource, int by) {
        incrementResource(resource, -by);
    }

    /**
     * Checks whether the resource holder has the resources specified in the
     * {@code resourceMap}.
     *
     * @param resourceMap The resources whose availability to check.
     *
     * @return A value indicating whether the holder has the specified resources.
     */
    public boolean hasResources(Map<Resource, Long> resourceMap) {
        for (Entry<Resource, Long> entry : resourceMap.entrySet()) {
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
     * @param resourceMap The resources to add to the resource holder.
     */
    public void incrementResources(Map<Resource, Long> resourceMap) {
        for (Entry<Resource, Long> entry : resourceMap.entrySet()) {
            incrementResource(entry.getKey(), (int) (long) entry.getValue());
        }
    }

    /**
     * Removes the resources in the specified {@code resourceMap} from the resource
     * holder.
     *
     * @param resourceMap The resources to remove from the resource holder.
     */
    public void decrementResources(Map<Resource, Long> resourceMap) {
        for (Entry<Resource, Long> entry : resourceMap.entrySet()) {
            decrementResource(entry.getKey(), (int) (long) entry.getValue());
        }
    }

    /**
     * @return a copy of the resource map
     */
    protected Map<Resource, Integer> getResourceMap() {
        return new EnumMap<>(resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resources);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ResourceHolder))
            return false;
        ResourceHolder other = (ResourceHolder) obj;
        return Objects.equals(resources, other.resources);
    }

}
