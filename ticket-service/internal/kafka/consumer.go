package kafka

import (
	"encoding/json"
	"log"

	"ticket/internal/db"
	"ticket/internal/models"

	"github.com/IBM/sarama"
)

func Consume() {

	const topic  = "outbox.event.event_status"

    consumer, err := sarama.NewConsumer([]string{broker}, nil)
	if err != nil {
		log.Panicf("Could not create consumer: %v", err)
	}

	partitionList, err := consumer.Partitions(topic)
	if err != nil {
		log.Panicf("Error retrieving partitionList: %v", err)
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
        log.Panicf("json.Unmarshal %v", err.Error()) 
    }

    var event models.Event
    err = json.Unmarshal([]byte(msg.Payload), &event)
    if err != nil {
        log.Panicf("json.Unmarshal %v", err.Error()) 
    }

    log.Printf("Event ID: %v", event.ID)
    log.Printf("Status: %v", event.IsActive)

	d:= db.Dao{}
	d.PersistEvent(event)
}