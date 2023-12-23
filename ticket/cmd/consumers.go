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
    msgErr := json.Unmarshal(v, &msg)
    if msgErr != nil {
        log.Println(msgErr.Error()) 
    }

    var event models.Event
    eventErr := json.Unmarshal([]byte(msg.Payload), &event)
    if eventErr != nil {
        log.Println(eventErr.Error()) 
    }

    log.Printf("Event ID: %v", event.ID)
    log.Printf("Status: %v", event.IsActive)

	dao.CreateEvent(event)
}