package com.amazon.ata.deliveringonourpromise.types;

import com.amazon.ata.ordermanipulationauthority.OrderCondition;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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

    public static Builder builder() {
        return new Builder();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    public OrderCondition getCondition() {
        return condition;
    }

    public List<OrderItem> getCustomerOrderItemList() {
        return Collections.unmodifiableList(customerOrderItemList);
    }

    public String getShipOption() {
        return shipOption;
    }

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

    public static class Builder {
        private String orderId;
        private String customerId;
        private String marketplaceId;
        private OrderCondition condition;
        private List<OrderItem> customerOrderItemList;
        private String shipOption;
        private ZonedDateTime orderDate;

        public Builder withOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withMarketplaceId(String marketplaceId) {
            this.marketplaceId = marketplaceId;
            return this;
        }

        public Builder withCondition(OrderCondition condition) {
            this.condition = condition;
            return this;
        }

        public Builder withCustomerOrderItemList(List<OrderItem> customerOrderItemList) {
            this.customerOrderItemList = new ArrayList<>(customerOrderItemList);
            return this;
        }

        public Builder withShipOption(String shipOption) {
            this.shipOption = shipOption;
            return this;
        }

        public Builder withOrderDate(ZonedDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

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

