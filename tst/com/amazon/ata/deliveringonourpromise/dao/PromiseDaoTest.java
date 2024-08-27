package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.App;
import com.amazon.ata.deliveringonourpromise.deliverypromiseservice.DeliveryPromiseServiceClient;
import com.amazon.ata.deliveringonourpromise.ordermanipulationauthority.OrderManipulationAuthorityClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PromiseDaoTest {

    private PromiseDao dao;

    private OrderManipulationAuthorityClient omaClient = App.getOrderManipulationAuthorityClient();
    private DeliveryPromiseServiceClient dpsClient = App.getDeliveryPromiseServiceClient();

    // undelivered
    private String shippedOrderId;
    private String shippedOrderItemId;
    private Promise shippedDeliveryPromise;

    // delivered
    private String deliveredOrderId;
    private String deliveredOrderItemId;
    private Promise deliveredDeliveryPromise;
    private ZonedDateTime deliveredDeliveryDate;

    @BeforeEach
    public void setup() {
        // order fixtures: these are specific known test orders.
        shippedOrderId = "900-3746401-0000002";
        deliveredOrderId = "900-3746401-0000003";

        shippedOrderItemId = omaClient
                .getCustomerOrderByOrderId(shippedOrderId)
                .getCustomerOrderItemList()
                .get(0)
                .getCustomerOrderItemId();
        shippedDeliveryPromise = dpsClient.getPromise(shippedOrderItemId);

        deliveredOrderItemId = omaClient
                .getCustomerOrderByOrderId(deliveredOrderId)
                .getCustomerOrderItemList()
                .get(0)
                .getCustomerOrderItemId();
        deliveredDeliveryPromise = dpsClient.getPromise(deliveredOrderItemId);
        deliveredDeliveryDate = omaClient
                .getCustomerOrderByOrderId(deliveredOrderId)
                .getOrderShipmentList().get(0)
                .getDeliveryDate();

        List<PromiseClient> clients = Arrays.asList(dpsClient);
        dao = new PromiseDao(clients, omaClient);
    }

    @Test
    public void get_nonexistentOrderItemId_returnsEmptyList() {
        // GIVEN - invalid/nonexistent order item ID

        // WHEN
        List<Promise> promises = dao.get("123");

        // THEN
        assertTrue(promises.isEmpty());
    }
}