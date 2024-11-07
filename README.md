
# Kafka Dead Letter Topic (DLT) Demostration

This Spring Boot application demonstrates the implementation and usage of Dead Letter Topics (DLT) in Apache Kafka using Spring Kafka. The application simulates an order processing system where some orders may fail processing and need to be redirected to a Dead Letter Topic after several retry attempts.

## Features

- REST API endpoint for submitting orders
- Kafka message producer and consumer implementation
- Dead Letter Topic handling with retry mechanism
- Demo mode with automatic random order generation
- Configurable retry attempts and intervals
- Comprehensive error handling

## Prerequisites

- Java 11 or later
- Apache Kafka 2.8.0 or later
- Maven 3.6 or later
- Git (optional)

## Project Structure

```
kafka-dlt-example/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── kafka/
│       │               ├── config/
│       │               ├── controller/
│       │               ├── model/
│       │               ├── producer/
│       │               ├── consumer/
│       │               └── service/
│       └── resources/
│           └── application.yml
├── .gitignore
├── pom.xml
└── README.md
```

## Prerequisites

Follow the steps in [Apache Kafka Quickstart](https://kafka.apache.org/quickstart) guide to download and setup Apache Kafka locally.

## Setup and Installation

1. Clone the repository (optional):
```bash
git clone https://your-repository-url/kafka-dlt-example.git
cd kafka-dlt-example
```

2. Create required Kafka topics:
```bash
# Create main topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --topic orders-topic --partitions 1 --replication-factor 1

# Create DLT topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --topic orders-topic.DLT --partitions 1 --replication-factor 1
```

3. Build the application:
```bash
mvn clean package
```

## Running the Application

### Normal Mode
In normal mode, the application accepts orders through a REST API endpoint:

```bash
java -jar target/kafka-dlt-example-1.0.0.jar
```

### Demo Mode
In demo mode, the application automatically generates random orders:

```bash
java -jar target/kafka-dlt-example-1.0.0.jar --demo=true
```

Demo mode features:
- Generates random orders every 10-30 seconds
- 20% probability of generating high-value orders (amount > $1000)
- Continues to accept orders via REST API

### Configuration Options

You can configure the following properties:
```bash
# Base delay for demo mode order generation (in milliseconds)
--demo.delay=15000

# Kafka broker address
--spring.kafka.bootstrap-servers=localhost:9092
```

## Testing the Application

### Using REST API

1. Submit a normal order (will be processed successfully):
```bash
curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{"orderId":"123","amount":500,"status":"NEW"}'
```

2. Submit a high-value order (will fail and go to DLT):
```bash
curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{"orderId":"456","amount":1500,"status":"NEW"}'
```

### Monitoring Kafka Topics

Monitor the main topic:
```bash
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic orders-topic --from-beginning
```

Monitor the DLT topic:
```bash
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic orders-topic.DLT --from-beginning
```

## Message Flow

1. Order is received (via REST API or demo generation)
2. Order is published to the `orders-topic`
3. Consumer attempts to process the order
4. For orders with amount > $1000:
   - Processing fails
   - Retry mechanism attempts 3 retries
   - After all retries fail, message is moved to `orders-topic.DLT`
5. DLT consumer logs the failed order

## Error Handling

- Messages that fail processing are retried 3 times
- 1-second delay between retry attempts
- After all retries are exhausted, messages are moved to DLT
- DLT messages are logged and can be processed separately

## Logging

The application provides detailed logging:
- Order generation (in demo mode)
- Processing attempts
- Retry attempts
- DLT messages
- General application events

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

Unless otherwise specified, all content, including all source code files and documentation files in this repository are:

Copyright (c) 2024 Leonid Shamis

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

SPDX-License-Identifier: Apache-2.0