package com.amazon.ata.deliveringonourpromise;

import com.amazon.ata.deliveringonourpromise.activity.GetPromiseHistoryByOrderIdActivity;
import com.amazon.ata.deliveringonourpromise.dao.OrderDao;
import com.amazon.ata.deliveringonourpromise.dao.PromiseClient;
import com.amazon.ata.deliveringonourpromise.dao.PromiseDao;
import com.amazon.ata.deliveringonourpromise.data.OrderDatastore;
import com.amazon.ata.deliveringonourpromise.deliverypromiseservice.DeliveryPromiseServiceClient;
import com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient;
import com.amazon.ata.deliveringonourpromise.ordermanipulationauthority.OrderManipulationAuthorityClient;
import com.amazon.ata.deliveringonourpromise.promisehistoryservice.PromiseHistoryClient;
import com.amazon.ata.deliverypromiseservice.service.DeliveryPromiseService;
import com.amazon.ata.orderfulfillmentservice.OrderFulfillmentService;
import com.amazon.ata.ordermanipulationauthority.OrderManipulationAuthority;

import java.util.Arrays;
import java.util.List;

/**
 * Provides inversion of control for the DeliveringOnOurPromise project by instantiating all of the
 * dependencies needed by the Shell and its dependency classes.
 */
public class App {
    /* don't instantiate me */
    private App() {}

    /**
     * Fetch a new PromiseHistoryClient with all of its dependencies loaded for use in the Shell!
     * @return fully loaded PromiseHistoryClient, ready for service! (er, client)
     */
    public static PromiseHistoryClient getPromiseHistoryClient() {
        return new PromiseHistoryClient(getPromiseHistoryByOrderIdActivity());
    }

    /* helpers */

    /**
     * Get an instance of GetPromiseHistoryByOrderIdActivity.
     * @return GetPromiseHistoryByOrderIdActivity instance
     */
    public static GetPromiseHistoryByOrderIdActivity getPromiseHistoryByOrderIdActivity() {
        return new GetPromiseHistoryByOrderIdActivity(getOrderDao(), getPromiseDao());
    }

    // DAOs
    /**
     * Get an instance of OrderDao.
     * @return OrderDao instance
     */
    public static OrderDao getOrderDao() {
        return new OrderDao(getOrderManipulationAuthorityClient());
    }

    /**
     * Get an instance of PromiseDao.
     * @return PromiseDao instance
     */
    public static PromiseDao getPromiseDao() {
        List<PromiseClient> clients = Arrays.asList(
                getDeliveryPromiseServiceClient(),
                getOrderFulfillmentServiceClient()
        );
        return new PromiseDao(clients, getOrderManipulationAuthorityClient());
    }

    // service clients
    /**
     * Get an instance of OrderManipulationAuthorityClient.
     * @return OrderManipulationAuthorityClient instance
     */
    public static OrderManipulationAuthorityClient getOrderManipulationAuthorityClient() {
        return new OrderManipulationAuthorityClient(getOrderManipulationAuthority());
    }

    /**
     * Get an instance of DeliveryPromiseServiceClient.
     * @return DeliveryPromiseServiceClient instance
     */
    public static DeliveryPromiseServiceClient getDeliveryPromiseServiceClient() {
        return new DeliveryPromiseServiceClient(getDeliveryPromiseService());
    }

    /**
     * Get an instance of OrderFulfillmentServiceClient.
     * @return OrderFulfillmentServiceClient instance
     */
    public static OrderFulfillmentServiceClient getOrderFulfillmentServiceClient() {
        return new OrderFulfillmentServiceClient(getOrderFulfillmentService());
    }

    // dependency services
    /**
     * Get an instance of OrderManipulationAuthority.
     * @return OrderManipulationAuthority instance
     */
    public static OrderManipulationAuthority getOrderManipulationAuthority() {
        return new OrderManipulationAuthority(getOrderDatastore());
    }

    /**
     * Get an instance of DeliveryPromiseService.
     * @return DeliveryPromiseService instance
     */
    public static DeliveryPromiseService getDeliveryPromiseService() {
        return new DeliveryPromiseService(getOrderDatastore());
    }

    /**
     * Get an instance of OrderFulfillmentService.
     * @return OrderFulfillmentService instance
     */
    public static OrderFulfillmentService getOrderFulfillmentService() {
        return new OrderFulfillmentService(getOrderDatastore(), getDeliveryPromiseService());
    }

    // sample data
    /**
     * Get an instance of OrderDatastore.
     * @return OrderDatastore instance
     */
    public static OrderDatastore getOrderDatastore() {
        return OrderDatastore.getDatastore();
    }
}
