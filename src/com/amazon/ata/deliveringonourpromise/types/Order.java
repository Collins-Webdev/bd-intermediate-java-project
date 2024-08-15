package com.amazon.ata.deliveringonourpromise.types;

import com.amazon.ata.ordermanipulationauthority.OrderCondition;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Represents an order in the system.
 */
public class Order {
    private String orderId;
    private String customerId;
    private String marketplaceId;
    private OrderCondition condition;
    private List<OrderItem> customerOrderItemList;
    private String shipOption;
    private ZonedDateTime orderDate;

    private Order() {
        this.customerOrderItemList = new ArrayList<>();
    }

    /**
     * Returns a new Builder instance for creating an Order.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the order ID.
     *
     * @return the order ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Gets the customer ID.
     *
     * @return the customer ID
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Gets the marketplace ID.
     *
     * @return the marketplace ID
     */
    public String getMarketplaceId() {
        return marketplaceId;
    }

    /**
     * Gets the order condition.
     *
     * @return the order condition
     */
    public OrderCondition getCondition() {
        return condition;
    }

    /**
     * Gets the list of customer order items.
     *
     * @return an unmodifiable list of customer order items
     */
    public List<OrderItem> getCustomerOrderItemList() {
        return Collections.unmodifiableList(customerOrderItemList);
    }

    /**
     * Gets the shipping option.
     *
     * @return the shipping option
     */
    public String getShipOption() {
        return shipOption;
    }

    /**
     * Gets the order date.
     *
     * @return the order date
     */
    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", marketplaceId='" + marketplaceId + '\'' +
                ", condition=" + condition +
                ", customerOrderItemList=" + customerOrderItemList +
                ", shipOption='" + shipOption + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }

    /**
     * Builder class for creating Order instances.
     */
    public static class Builder {
        private String orderId;
        private String customerId;
        private String marketplaceId;
        private OrderCondition condition;
        private List<OrderItem> customerOrderItemList;
        private String shipOption;
        private ZonedDateTime orderDate;

        /**
         * Sets the order ID.
         *
         * @param orderId the order ID to set
         * @return the Builder instance
         */
        public Builder withOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        /**
         * Sets the customer ID.
         *
         * @param customerId the customer ID to set
         * @return the Builder instance
         */
        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        /**
         * Sets the marketplace ID.
         *
         * @param marketplaceId the marketplace ID to set
         * @return the Builder instance
         */
        public Builder withMarketplaceId(String marketplaceId) {
            this.marketplaceId = marketplaceId;
            return this;
        }

        /**
         * Sets the order condition.
         *
         * @param condition the order condition to set
         * @return the Builder instance
         */
        public Builder withCondition(OrderCondition condition) {
            this.condition = condition;
            return this;
        }

        /**
         * Sets the list of customer order items.
         *
         * @param customerOrderItemList the list of customer order items to set
         * @return the Builder instance
         */
        public Builder withCustomerOrderItemList(List<OrderItem> customerOrderItemList) {
            this.customerOrderItemList = new ArrayList<>(customerOrderItemList);
            return this;
        }

        /**
         * Sets the shipping option.
         *
         * @param shipOption the shipping option to set
         * @return the Builder instance
         */
        public Builder withShipOption(String shipOption) {
            this.shipOption = shipOption;
            return this;
        }

        /**
         * Sets the order date.
         *
         * @param orderDate the order date to set
         * @return the Builder instance
         */
        public Builder withOrderDate(ZonedDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        /**
         * Builds and returns a new Order instance.
         *
         * @return a new Order instance
         */
        public Order build() {
            Order order = new Order();
            order.orderId = this.orderId;
            order.customerId = this.customerId;
            order.marketplaceId = this.marketplaceId;
            order.condition = this.condition;
            order.customerOrderItemList = new ArrayList<>(this.customerOrderItemList);
            order.shipOption = this.shipOption;
            order.orderDate = this.orderDate;

            return order;
        }
    }
}