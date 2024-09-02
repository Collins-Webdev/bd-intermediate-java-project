package com.amazon.ata.deliveringonourpromise.orderfulfillmentservice;

import com.amazon.ata.deliveringonourpromise.dao.PromiseClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.orderfulfillmentservice.OrderFulfillmentService;
import com.amazon.ata.orderfulfillmentservice.OrderPromise;

public class OrderFulfillmentServiceClient implements PromiseClient {
    private OrderFulfillmentService orderFulfillmentService;

    public OrderFulfillmentServiceClient(OrderFulfillmentService orderFulfillmentService) {
        this.orderFulfillmentService = orderFulfillmentService;
    }

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
