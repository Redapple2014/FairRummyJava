package com.connection.message.queue;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class CustomMQConsumer extends DefaultConsumer {
    private final BlockingQueue<Delivery> queue = new LinkedBlockingQueue<>();

    public CustomMQConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
        queue.add(new Delivery(envelope, properties, body, consumerTag));
    }

    public Delivery nextMessage() throws Exception {
        return queue.take();
    }

    public static class Delivery {
        private String consumerTag;
        private final Envelope _envelope;
        private final AMQP.BasicProperties _properties;
        private final byte[] _body;

        public Delivery(Envelope envelope, AMQP.BasicProperties properties, byte[] body, String consumerTag) {
            _envelope = envelope;
            _properties = properties;
            _body = body;
            this.consumerTag = consumerTag;
        }

        /**
         * Retrieve the message envelope.
         *
         * @return the message envelope
         */
        public Envelope getEnvelope() {
            return _envelope;
        }

        /**
         * Retrieve the message properties.
         *
         * @return the message properties
         */
        public BasicProperties getProperties() {
            return _properties;
        }

        /**
         * Retrieve the message body.
         *
         * @return the message body
         */
        public byte[] getBody() {
            return _body;
        }

        /**
         * @return the consumerTag
         */
        public String getConsumerTag() {
            return consumerTag;
        }

    }
}
