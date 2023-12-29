package kafka

import (
	"os"
	"os/signal"

	"github.com/IBM/sarama"
)

type KafkaProducer struct {
	pr sarama.AsyncProducer
	ch chan os.Signal
}

func init() {
	k := KafkaProducer{}
	k.Init()
}

func (kp *KafkaProducer) Init() (sarama.AsyncProducer, error) {

	config := sarama.NewConfig()
	config.Producer.Return.Successes = true

	prd, err := sarama.NewAsyncProducer([]string{broker}, config)

	kp.ch = make(chan os.Signal, 1)
	signal.Notify(kp.ch, os.Interrupt)

	return KafkaProducer{pr: prd}.pr, err
}

func (kp *KafkaProducer) Publish(message string) {

	const topic  = "ticket_purchase"

	pr, _ := kp.Init()	
	pr.Input() <- &sarama.ProducerMessage{
		Topic: topic,
		Value: sarama.StringEncoder(message),
	}
}