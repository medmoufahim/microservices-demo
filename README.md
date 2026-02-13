# Listing topics on a broker
Assuming your Kafka broker is accessible as kafka:9092 on the Docker network docker-compose_default, you can list topics by running:

```bash
docker run --rm --tty \
           --network docker-compose_default \
           confluentinc/cp-kafkacat \
           kafkacat -b kafka:9092 \
                    -L
```

Exemple :
```bash
docker run --rm --tty --network docker-compose_application confluentinc/cp-kafkacat kafkacat -b kafka-broker-1:9092 -L
```

# Consuming messages from a topic
Assuming your Kafka broker is accessible as kafka:9092 on the Docker network docker-compose_default, you can print messages and their associated metadata from topic foo, as follows:

```bash
docker run --rm --tty \
           --network docker-compose_default \
           confluentinc/cp-kafkacat \
           kafkacat -b kafka:9092 -C -K: \
                    -f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\n\Partition: %p\tOffset: %o\n--\n' \
                    -t foo
```

Exemple :
```bash 
docker run --rm --tty --network docker-compose_application confluentinc/cp-kafkacat kafkacat -b kafka-broker-1:9092 -C -K: -f '\nKey (%K bytes): %k\t\nValue (%S bytes): %s\n\Partition: %p\tOffset: %o\n--\n' -t ai-generated-tweet-to-kafka-topic
```

More about Kafkacat : https://hub.docker.com/r/confluentinc/cp-kafkacat/

