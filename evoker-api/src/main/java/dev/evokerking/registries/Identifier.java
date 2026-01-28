package dev.evokerking.registries;

import java.util.Objects;

/**
 * Represents a namespaced identifier (e.g., "minecraft:stone").
 * Used to uniquely identify registered objects.
 */
public class Identifier implements Comparable<Identifier> {
    private final String namespace;
    private final String path;

    /**
     * Creates an identifier from a namespace and path.
     *
     * @param namespace The namespace (e.g., "minecraft")
     * @param path The path (e.g., "stone")
     */
    public Identifier(String namespace, String path) {
        this.namespace = validateNamespace(namespace);
        this.path = validatePath(path);
    }

    /**
     * Creates an identifier from a string in the format "namespace:path".
     * If no colon is present, assumes "minecraft" namespace.
     *
     * @param id The identifier string
     */
    public Identifier(String id) {
        int colonIndex = id.indexOf(':');
        if (colonIndex >= 0) {
            this.namespace = validateNamespace(id.substring(0, colonIndex));
            this.path = validatePath(id.substring(colonIndex + 1));
        } else {
            this.namespace = "minecraft";
            this.path = validatePath(id);
        }
    }

    private static String validateNamespace(String namespace) {
        if (namespace.isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be empty");
        }
        if (!namespace.matches("[a-z0-9_.-]+")) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        }
        return namespace;
    }

    private static String validatePath(String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
        if (!path.matches("[a-z0-9_.-/]+")) {
            throw new IllegalArgumentException("Invalid path: " + path);
        }
        return path;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Identifier)) return false;
        Identifier other = (Identifier) obj;
        return namespace.equals(other.namespace) && path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public int compareTo(Identifier other) {
        int namespaceCompare = this.namespace.compareTo(other.namespace);
        if (namespaceCompare != 0) {
            return namespaceCompare;
        }
        return this.path.compareTo(other.path);
    }
}