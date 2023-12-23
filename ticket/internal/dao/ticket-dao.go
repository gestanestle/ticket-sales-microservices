package dao

import (
	"context"
	"log"
	"ticket/internal/errors"
	"ticket/internal/models"
)

func DefineTicket(t models.Ticket) (int64, error) {
	
	db, err := conn.Acquire(context.Background())
	if err != nil {
		panic("Error acquiring connection to database.")
    }
	defer db.Release()

	var id int64

	q := `INSERT INTO ticket (event_id, type, price, qty_stock, qty_sold) VALUES ($1, $2, $3, $4, $5) RETURNING ticket_id`
	err = db.QueryRow(context.Background(), q, t.EventID, t.Type, t.Price, t.QtyStock, 0).Scan(&id)

	
	if err != nil {
		log.Fatalf(errMsg, err)
		return 0, errors.RaiseErr(404, "Invalid request data.")
	}

	log.Printf("Defined new Ticket with ID: %d", id)
	return id, nil
}