package dev.golgolex.golgocloud.common;

import org.jetbrains.annotations.NotNull;

/**
 * The CacheHandler interface defines operations for handling a cache of elements.
 *
 * @param <E> the type of elements in the cache.
 */
public interface CacheHandler<E> {

    /**
     * Updates the cache with the specified element.
     *
     * @param element the element to update the cache with
     * @throws NullPointerException if the element is null
     */
    void updateCached(@NotNull E element);

    /**
     * Puts the specified element into the cache.
     *
     * @param element the element to be added to the cache (must not be null)
     */
    void putCache(@NotNull E element);

    /**
     * Removes the specified element from the cache.
     *
     * @param element the element to be removed from the cache
     * @throws NullPointerException if the specified element is null
     */
    void unCache(@NotNull E element);

}
