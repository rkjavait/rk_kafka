# rk_kafka

This repository contains a simple Kafka-based event flow built with two Spring Boot services:

- `kfproduce`: publishes order events to Kafka
- `kfconsumer`: consumes order events from Kafka

Together they demonstrate the producer/consumer pattern for an asynchronous event-driven architecture.

## Project structure

```text
rk_kafka/
├── README.md
├── docs/
│   └── ARCHITECTURE.md
├── kfproduce/
│   ├── README.md
│   ├── docs/
│   │   └── ARCHITECTURE.md
│   └── src/
├── kfconsumer/
│   ├── README.md
│   ├── docs/
│   │   └── ARCHITECTURE.md
│   └── src/
└── ...
```

## Architecture at a glance

```text
Client / HTTP Request
        |
        v
kfproduce (Producer)
        |
        v
Kafka Topic: order-service
        |
        v
kfconsumer (Consumer)
        |
        v
Order processing / logging / downstream services
```

## Business flow

1. A client sends an order payload to the producer service.
2. `kfproduce` serializes the message as JSON and pushes it to Kafka.
3. `kfconsumer` subscribes to the same topic and deserializes the message.
4. The consumer processes the order event for downstream tasks such as logging, storage, or notifications.

## Shared configuration

Both services use the same Kafka topic:

```text
order-service
```

The producer publishes messages to this topic and the consumer listens to it.

## Prerequisites

- Java 17+
- Maven 3.8+
- Kafka broker running on `localhost:9092`

## Start Kafka locally

If Kafka is not running, start ZooKeeper and the broker with your local Kafka installation:

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

Then create the topic:

```bash
bin/kafka-topics.sh --create --topic order-service --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

## Run the services

From each project directory:

```bash
cd kfproduce
./mvnw spring-boot:run
```

In a second terminal:

```bash
cd kfconsumer
./mvnw spring-boot:run
```

## Example message

```json
{
  "id": 101,
  "orderType": "ONLINE",
  "customerName": "Alice Johnson"
}
```

## Component responsibilities

### Producer (`kfproduce`)

- accepts HTTP requests
- serializes orders as JSON
- sends events to Kafka

### Consumer (`kfconsumer`)

- listens on the `order-service` topic
- deserializes incoming messages
- processes the payload for business logic

## Related docs

- `kfproduce/README.md`
- `kfconsumer/README.md`
- `docs/ARCHITECTURE.md`

## Notes

This is a starter Kafka example intended for learning, demo, and portfolio work. It highlights the common event-driven pipeline of publish-and-subscribe messaging with Spring Boot and Kafka.
