package com.amazon.ata.deliveringonourpromise.dao;

/**
 * DAO interface to abstract calls.
 *
 * @param <I> The type of the input parameter (usually the ID)
 * @param <O> The type of the output object
 */
public interface ReadOnlyDao<I, O> {

    /**
     * Get object method to be implemented.
     * @param orderId Order Id
     * @return Object abstracted object
     */
    O get(I orderId);
}

