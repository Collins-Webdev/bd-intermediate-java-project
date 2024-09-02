package com.amazon.ata.deliveringonourpromise.deliverypromiseservice;

import com.amazon.ata.deliveringonourpromise.dao.PromiseClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliverypromiseservice.service.DeliveryPromiseService;

public class DeliveryPromiseServiceClient implements PromiseClient {
    private DeliveryPromiseService deliveryPromiseService;

    public DeliveryPromiseServiceClient(DeliveryPromiseService deliveryPromiseService) {
        this.deliveryPromiseService = deliveryPromiseService;
    }

    @Override
    public Promise getPromise(String customerOrderItemId) {
        return getDeliveryPromiseByOrderItemId(customerOrderItemId);
    }

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
