package com.amazon.ata.deliveringonourpromise.deliverypromiseservice;

import com.amazon.ata.deliveringonourpromise.dao.PromiseClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliverypromiseservice.service.DeliveryPromiseService;

/**
 * Client pour interagir avec le service de promesse de livraison.
 */
public class DeliveryPromiseServiceClient implements PromiseClient {
    private DeliveryPromiseService deliveryPromiseService;

    /**
     * Constructeur pour la classe DeliveryPromiseServiceClient.
     *
     * @param deliveryPromiseService le service de promesse de livraison
     */
    public DeliveryPromiseServiceClient(DeliveryPromiseService deliveryPromiseService) {
        this.deliveryPromiseService = deliveryPromiseService;
    }

    /**
     * Récupère la promesse de livraison pour un élément de commande spécifique.
     *
     * @param customerOrderItemId l'identifiant de l'élément de commande du client
     * @return la promesse de livraison correspondante
     */
    @Override
    public Promise getPromise(String customerOrderItemId) {
        return getDeliveryPromiseByOrderItemId(customerOrderItemId);
    }

    /**
     * Récupère la promesse de livraison pour un élément de commande spécifique.
     *
     * @param customerOrderItemId l'identifiant de l'élément de commande du client
     * @return la promesse de livraison correspondante
     */
    public Promise getDeliveryPromiseByOrderItemId(String customerOrderItemId) {
        com.amazon.ata.deliverypromiseservice.service.DeliveryPromise deliveryPromise =
                deliveryPromiseService.getDeliveryPromise(customerOrderItemId);

        if (deliveryPromise == null) {
            return null;
        }

        return Promise.builder()
                .withPromiseLatestArrivalDate(deliveryPromise.getPromiseLatestArrivalDate())
                .withCustomerOrderItemId(deliveryPromise.getCustomerOrderItemId())
                .withPromiseEffectiveDate(deliveryPromise.getPromiseEffectiveDate())
                .withPromiseLatestShipDate(deliveryPromise.getPromiseLatestShipDate())
                .withPromiseProvidedBy(deliveryPromise.getPromiseProvidedBy())
                .withAsin(deliveryPromise.getAsin())
                .build();
    }
}
