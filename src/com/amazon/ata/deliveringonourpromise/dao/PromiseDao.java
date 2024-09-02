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

public class PromiseDao implements ReadOnlyDao<String, List<Promise>> {
    private List<PromiseClient> promiseClients;
    private OrderManipulationAuthorityClient omaClient;

    public PromiseDao(List<PromiseClient> promiseClients, OrderManipulationAuthorityClient omaClient) {
        this.promiseClients = promiseClients;
        this.omaClient = omaClient;
    }

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
