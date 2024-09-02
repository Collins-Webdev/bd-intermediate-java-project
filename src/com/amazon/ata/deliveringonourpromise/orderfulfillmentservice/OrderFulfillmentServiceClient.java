package com.amazon.ata.deliveringonourpromise.orderfulfillmentservice;

import com.amazon.ata.deliveringonourpromise.dao.PromiseClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.orderfulfillmentservice.OrderFulfillmentService;
import com.amazon.ata.orderfulfillmentservice.OrderPromise;

/**
 * Client pour interagir avec le service de gestion des commandes.
 */
public class OrderFulfillmentServiceClient implements PromiseClient {
    private OrderFulfillmentService orderFulfillmentService;

    /**
     * Constructeur pour la classe OrderFulfillmentServiceClient.
     *
     * @param orderFulfillmentService le service de gestion des commandes
     */
    public OrderFulfillmentServiceClient(OrderFulfillmentService orderFulfillmentService) {
        this.orderFulfillmentService = orderFulfillmentService;
    }

    /**
     * Récupère la promesse de livraison pour un élément de commande spécifique.
     *
     * @param customerOrderItemId l'identifiant de l'élément de commande du client
     * @return la promesse de livraison correspondante
     */
    @Override
    public Promise getPromise(String customerOrderItemId) {
        OrderPromise orderPromise = orderFulfillmentService.getOrderPromise(customerOrderItemId);
        if (orderPromise == null) {
            return null;
        }
        return Promise.builder()
                .withPromiseLatestArrivalDate(orderPromise.getPromiseLatestArrivalDate())
                .withCustomerOrderItemId(orderPromise.getCustomerOrderItemId())
                .withPromiseEffectiveDate(orderPromise.getPromiseEffectiveDate())
                .withPromiseLatestShipDate(orderPromise.getPromiseLatestShipDate())
                .withPromiseProvidedBy(orderPromise.getPromiseProvidedBy())
                .withAsin(orderPromise.getAsin())
                .withIsActive(orderPromise.isActive())
                .build();
    }
}
