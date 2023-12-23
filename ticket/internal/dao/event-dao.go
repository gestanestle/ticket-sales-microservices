package dao

import (
	"context"
	"log"
	"ticket/internal/models"
)

const errMsg = "Couldnt execute sql statement. %v"

func CreateEvent(event models.Event) {
	log.Print("AT CREATE EVENT...")

	db, err := conn.Acquire(context.Background())
	if err != nil {
		panic("Error acquiring connection to database.")
    }

	defer db.Release()

	q := `INSERT INTO event (event_id, is_active) VALUES ($1, $2)`
	_,err = db.Query(context.Background(), q, event.ID, event.IsActive)

	if err != nil {
		log.Printf(errMsg, err)
		return
	}

	log.Printf("Created new Event with ID: %d", event.ID)
}