# Shared Architecture

This repository demonstrates a basic Kafka event-driven system composed of two Spring Boot services:

- a producer that writes order events
- a consumer that reads and processes those events

## End-to-end flow

```text
Order Client
    |
    v
HTTP API (kfproduce)
    |
    v
Kafka Producer
    |
    v
Kafka Topic: order-service
    |
    v
Kafka Consumer (kfconsumer)
    |
    v
Business processing / logs / storage / downstream services
```

## Why this architecture works

Kafka acts as a decoupling layer between the producer and consumer:

- the producer does not need to know who consumes the event
- the consumer can process messages independently and asynchronously
- services can scale independently
- the system can evolve to include extra consumers, analytics, or integrations

## Producer side

The producer service handles incoming order requests and publishes them to Kafka.

### Responsibilities

- accepts `POST /v1/orders` requests
- builds an `Order` payload
- serializes it to JSON
- sends it to the `order-service` topic through `KafkaTemplate`

### Typical execution path

```text
Controller -> Service -> KafkaTemplate.send(...) -> Kafka Topic
```

## Consumer side

The consumer service subscribes to the same Kafka topic and reads the incoming order messages.

### Responsibilities

- listens on `order-service`
- deserializes JSON into the `Order` model
- validates and processes the event
- logs or forwards the event to downstream logic

### Typical execution path

```text
Kafka Topic -> @KafkaListener -> Service -> Business processing
```

## Shared event contract

Both services rely on the same message payload shape:

```json
{
  "id": 101,
  "orderType": "ONLINE",
  "customerName": "Alice Johnson"
}
```

This contract must remain consistent across producer and consumer, especially for field names and package trust settings.

## Topic model

```text
Topic: order-service
```

This topic is the communication channel between the two applications. It allows one producer to emit events that multiple consumer services may subscribe to in the future.

## Operational flow

1. Start Kafka.
2. Run `kfproduce`.
3. Send an order request to the producer API.
4. Confirm that the event appears in the Kafka topic.
5. Run `kfconsumer`.
6. Observe the order being consumed and processed.

## Design principles

- asynchronous communication
- event-driven decoupling
- simple contract-first payload design
- modular separation between producer and consumer services

## Recommended next step

Extend the architecture with:

- a database sink
- a notification consumer
- retries and DLQ handling
- schema validation and versioning

This repo is intended as a clean starting point for learning event-driven systems with Kafka and Spring Boot.
