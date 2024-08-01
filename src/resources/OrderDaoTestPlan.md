package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.types.Order;
import com.amazon.ata.deliveringonourpromise.ordermanipulationauthority.OrderManipulationAuthorityClient;
import com.amazon.ata.ordermanipulationauthority.OrderManipulationAuthority;
import com.amazon.ata.ordermanipulationauthority.OrderResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderDaoTest {

    @Mock
    private OrderManipulationAuthorityClient mockOmaClient;

    @Mock
    private OrderManipulationAuthority mockOmaService;

    private OrderDao orderDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockOmaClient.getCustomerOrderByOrderId(anyString())).thenReturn(new OrderResult());
        orderDao = new OrderDao(mockOmaClient);
    }

    @Test
    public void get_forKnownOrderId_returnsOrder() {
        // GIVEN
        String knownOrderId = "111-7497023-2960775";
        OrderResult mockOrderResult = new OrderResult();
        mockOrderResult.setOrderId(knownOrderId);
        when(mockOmaClient.getCustomerOrderByOrderId(knownOrderId)).thenReturn(mockOrderResult);

        // WHEN
        Order result = orderDao.get(knownOrderId);

        // THEN
        assertNotNull(result);
        assertEquals(knownOrderId, result.getOrderId());
    }

    @Test
    public void get_forUnknownOrderId_returnsNull() {
        // GIVEN
        String unknownOrderId = "999-9999999-9999999";
        when(mockOmaClient.getCustomerOrderByOrderId(unknownOrderId)).thenReturn(null);

        // WHEN
        Order result = orderDao.get(unknownOrderId);

        // THEN
        assertNull(result);
    }

    @Test
    public void get_forNullOrderId_throwsException() {
        // GIVEN
        String nullOrderId = null;

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> orderDao.get(nullOrderId));
    }

    @Test
    public void get_forKnownOrderId_returnsCorrectOrder() {
        // GIVEN
        String knownOrderId = "111-7497023-2960775";
        OrderResult mockOrderResult = new OrderResult();
        mockOrderResult.setOrderId(knownOrderId);
        mockOrderResult.setCustomerId("customer123");
        mockOrderResult.setMarketplaceId("marketplace456");
        when(mockOmaClient.getCustomerOrderByOrderId(knownOrderId)).thenReturn(mockOrderResult);

        // WHEN
        Order result = orderDao.get(knownOrderId);

        // THEN
        assertNotNull(result);
        assertEquals(knownOrderId, result.getOrderId());
        assertEquals("customer123", result.getCustomerId());
        assertEquals("marketplace456", result.getMarketplaceId());
    }
}