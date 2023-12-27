package main

import (
	"encoding/json"
	"log"

	"ticket/internal/dao"
	"ticket/internal/models"

	"github.com/IBM/sarama"
)

const broker = "localhost:9092"
const topic = "outbox.event.event_status"

func consumeTopics() {

    consumer, err := sarama.NewConsumer([]string{broker}, nil)
	if err != nil {
		log.Println("Could not create consumer: ", err)
	}

	partitionList, err := consumer.Partitions(topic)
	if err != nil {
		log.Println("Error retrieving partitionList ", err)
	}

	initialOffset := sarama.OffsetOldest
	for _, partition := range partitionList {
		pc, _ := consumer.ConsumePartition(topic, partition, initialOffset)

		go func(pc sarama.PartitionConsumer) {
			for message := range pc.Messages() {
				structify(message)
			}
		}(pc)
	}
}

func structify(message *sarama.ConsumerMessage) {
    v := message.Value

    var msg models.BrokerMessage
    err := json.Unmarshal(v, &msg)
    if err != nil {
        log.Printf("json.Unmarshal %v", err.Error()) 
    }

    var event models.Event
    err = json.Unmarshal([]byte(msg.Payload), &event)
    if err != nil {
        log.Printf("json.Unmarshal %v", err.Error()) 
    }

    log.Printf("Event ID: %v", event.ID)
    log.Printf("Status: %v", event.IsActive)

	d:= dao.Dao{}
	d.PersistEvent(event)
}