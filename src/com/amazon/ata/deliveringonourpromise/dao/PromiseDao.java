package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.comparators.PromiseAsinComparator;
import com.amazon.ata.deliveringonourpromise.ordermanipulationauthority.OrderManipulationAuthorityClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.ordermanipulationauthority.OrderResult;
import com.amazon.ata.ordermanipulationauthority.OrderResultItem;
import com.amazon.ata.ordermanipulationauthority.OrderShipment;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de DAO pour gérer les promesses de livraison.
 */
public class PromiseDao implements ReadOnlyDao<String, List<Promise>> {
    private List<PromiseClient> promiseClients;
    private OrderManipulationAuthorityClient omaClient;

    /**
     * Constructeur pour la classe PromiseDao.
     *
     * @param promiseClients la liste des clients pour obtenir des promesses de livraison
     * @param omaClient le client pour manipuler les commandes
     */
    public PromiseDao(List<PromiseClient> promiseClients, OrderManipulationAuthorityClient omaClient) {
        this.promiseClients = promiseClients;
        this.omaClient = omaClient;
    }

    /**
     * Récupère toutes les promesses de livraison pour une commande spécifique.
     *
     * @param orderId l'identifiant de la commande
     * @return la liste des promesses de livraison
     */
    @Override
    public List<Promise> get(String orderId) {
        List<Promise> allPromises = new ArrayList<>();

        OrderResult orderResult = omaClient.getCustomerOrderByOrderId(orderId);
        if (orderResult == null) {
            return allPromises;
        }

        for (OrderResultItem item : orderResult.getCustomerOrderItemList()) {
            String customerOrderItemId = item.getCustomerOrderItemId();
            ZonedDateTime itemDeliveryDate = getDeliveryDateForOrderItem(customerOrderItemId);

            for (PromiseClient client : promiseClients) {
                Promise promise = client.getPromise(customerOrderItemId);
                if (promise != null) {
                    promise.setDeliveryDate(itemDeliveryDate);
                    allPromises.add(promise);
                }
            }
        }

        // Trier les promesses par ASIN de manière ascendante
        allPromises.sort(new PromiseAsinComparator());

        return allPromises;
    }

    /**
     * Récupère la date de livraison pour un élément de commande spécifique.
     *
     * @param customerOrderItemId l'identifiant de l'élément de commande du client
     * @return la date de livraison de l'élément de commande
     */
    private ZonedDateTime getDeliveryDateForOrderItem(String customerOrderItemId) {
        OrderResultItem orderResultItem = omaClient.getCustomerOrderItemByOrderItemId(customerOrderItemId);

        if (null == orderResultItem) {
            return null;
        }

        OrderResult orderResult = omaClient.getCustomerOrderByOrderId(orderResultItem.getOrderId());

        for (OrderShipment shipment : orderResult.getOrderShipmentList()) {
            for (OrderShipment.ShipmentItem shipmentItem : shipment.getCustomerShipmentItems()) {
                if (shipmentItem.getCustomerOrderItemId().equals(customerOrderItemId)) {
                    return shipment.getDeliveryDate();
                }
            }
        }

        return null;
    }
}
