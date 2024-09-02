package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.types.Promise;

/**
 * Interface représentant un client pour obtenir des promesses de livraison.
 */
public interface PromiseClient {
    /**
     * Récupère la promesse de livraison pour un élément de commande spécifique.
     *
     * @param customerOrderItemId l'identifiant de l'élément de commande du client
     * @return la promesse de livraison correspondante
     */
    Promise getPromise(String customerOrderItemId);
}
