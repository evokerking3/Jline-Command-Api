package dev.evokerking.registries;

import java.util.*;

/**
 * A generic registry system inspired by Minecraft's registry implementation.
 * Allows registration and retrieval of objects by unique identifiers.
 *
 * @param <T> The type of objects this registry will store (must extend RegistryType)
 */
public class Registry<T extends RegistryType> {
    private final Map<Identifier, T> entries = new LinkedHashMap<>();
    private final Map<T, Identifier> reverseMap = new IdentityHashMap<>();
    private final String registryName;
    private boolean frozen = false;

    /**
     * Creates a new registry with the given name.
     *
     * @param registryName The name of this registry
     */
    public Registry(String registryName) {
        this.registryName = registryName;
    }

    /**
     * Registers an object with the given identifier.
     *
     * @param id The identifier for the object
     * @param value The object to register
     * @return The registered object
     * @throws IllegalStateException if the registry is frozen or the id is already registered
     */
    public T register(Identifier id, T value) {
        if (frozen) {
            throw new IllegalStateException("Registry " + registryName + " is frozen and cannot accept new registrations");
        }
        
        if (entries.containsKey(id)) {
            throw new IllegalStateException("Identifier " + id + " is already registered in " + registryName);
        }
        
        if (value.isRegistered()) {
            throw new IllegalStateException("Object " + value + " is already registered");
        }
        
        entries.put(id, value);
        reverseMap.put(value, id);
        value.setRegistryId(id);
        return value;
    }

    /**
     * Gets an object by its identifier.
     *
     * @param id The identifier to look up
     * @return The registered object, or null if not found
     */
    public T get(Identifier id) {
        return entries.get(id);
    }

    /**
     * Gets the identifier for a registered object.
     *
     * @param value The object to look up
     * @return The identifier, or null if not registered
     */
    public Identifier getId(T value) {
        return reverseMap.get(value);
    }

    /**
     * Checks if an identifier is registered.
     *
     * @param id The identifier to check
     * @return true if the identifier is registered
     */
    public boolean contains(Identifier id) {
        return entries.containsKey(id);
    }

    /**
     * Gets all registered identifiers.
     *
     * @return An unmodifiable set of all registered identifiers
     */
    public Set<Identifier> getIds() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    /**
     * Gets all registered values.
     *
     * @return An unmodifiable collection of all registered values
     */
    public Collection<T> getValues() {
        return Collections.unmodifiableCollection(entries.values());
    }

    /**
     * Gets all entries in this registry.
     *
     * @return An unmodifiable set of all entries
     */
    public Set<Map.Entry<Identifier, T>> getEntries() {
        return Collections.unmodifiableSet(entries.entrySet());
    }

    /**
     * Gets an optional containing the value, or empty if not found.
     *
     * @param id The identifier to look up
     * @return An Optional containing the value if found
     */
    public Optional<T> getOptional(Identifier id) {
        return Optional.ofNullable(get(id));
    }

    /**
     * Gets a value or returns a default if not found.
     *
     * @param id The identifier to look up
     * @param defaultValue The default value to return
     * @return The registered value or the default
     */
    public T getOrDefault(Identifier id, T defaultValue) {
        return entries.getOrDefault(id, defaultValue);
    }

    /**
     * Freezes this registry, preventing further registrations.
     */
    public void freeze() {
        this.frozen = true;
    }

    /**
     * Checks if this registry is frozen.
     *
     * @return true if frozen
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Gets the name of this registry.
     *
     * @return The registry name
     */
    public String getRegistryName() {
        return registryName;
    }

    /**
     * Gets the number of registered entries.
     *
     * @return The size of the registry
     */
    public int size() {
        return entries.size();
    }

    /**
     * Clears all entries from this registry.
     * Only allowed if not frozen.
     *
     * @throws IllegalStateException if the registry is frozen
     */
    public void clear() {
        if (frozen) {
            throw new IllegalStateException("Cannot clear frozen registry " + registryName);
        }
        entries.clear();
        reverseMap.clear();
    }

    /**
     * Iterates over all entries in this registry.
     *
     * @param action The action to perform for each entry
     */
    public void forEach(java.util.function.BiConsumer<Identifier, T> action) {
        entries.forEach(action);
    }

    @Override
    public String toString() {
        return "Registry[" + registryName + ", size=" + entries.size() + ", frozen=" + frozen + "]";
    }
}