package dev.evokerking.registries;

/**
 * Base type for all registry entries.
 * All objects that can be registered must extend this class.
 */
public abstract class RegistryType {
    private Identifier registryId;

    /**
     * Sets the registry identifier for this entry.
     * Called automatically during registration.
     *
     * @param id The identifier
     */
    void setRegistryId(Identifier id) {
        if (this.registryId != null) {
            throw new IllegalStateException("Registry ID already set for " + this.getClass().getSimpleName());
        }
        this.registryId = id;
    }

    /**
     * Gets the registry identifier for this entry.
     *
     * @return The identifier, or null if not yet registered
     */
    public Identifier getRegistryId() {
        return registryId;
    }

    /**
     * Checks if this entry has been registered.
     *
     * @return true if registered
     */
    public boolean isRegistered() {
        return registryId != null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + (registryId != null ? registryId : "unregistered") + "]";
    }
}