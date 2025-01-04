package com.rzou.payment.infrastructure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Order exchange and queue for sending messages
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_ROUTING_KEY = "order.routing.key";

    // Payment exchange and queues for receiving messages
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String UPDATE_ORDER_STATUS_QUEUE = "update.order.status.queue";
    public static final String UPDATE_PAYMENT_LINK_QUEUE = "update.payment.link.queue";
    public static final String UPDATE_ORDER_STATUS_ROUTING_KEY = "update.order.status.routing.key";
    public static final String UPDATE_PAYMENT_LINK_ROUTING_KEY = "update.payment.link.routing.key";

    // Order exchange for sending messages
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true); // durable = true
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    // Payment exchange and queues for receiving messages
    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue updateOrderStatusQueue() {
        return new Queue(UPDATE_ORDER_STATUS_QUEUE, true); // durable = true
    }

    @Bean
    public Queue updatePaymentLinkQueue() {
        return new Queue(UPDATE_PAYMENT_LINK_QUEUE, true); // durable = true
    }

    @Bean
    public Binding updateOrderStatusBinding(Queue updateOrderStatusQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(updateOrderStatusQueue).to(paymentExchange).with(UPDATE_ORDER_STATUS_ROUTING_KEY);
    }

    @Bean
    public Binding updatePaymentLinkBinding(Queue updatePaymentLinkQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(updatePaymentLinkQueue).to(paymentExchange).with(UPDATE_PAYMENT_LINK_ROUTING_KEY);
    }
}