package kafka

import "os"

var (
	broker = os.Getenv("BROKER")
)