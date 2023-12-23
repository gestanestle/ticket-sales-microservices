package dao

import (
	"context"
	"log"
	"ticket/internal/errors"
	"ticket/internal/models"

	"github.com/jackc/pgx/v5"
)

func DefineTicket(t models.Ticket) (int64, error) {

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Panicf("conn.Acquire \n%v", err)
	}
	defer db.Release()

	var id int64
	q := `INSERT INTO ticket (event_id, type, price, qty_stock, qty_sold) VALUES ($1, $2, $3, $4, $5) RETURNING ticket_id`
	err = db.QueryRow(context.Background(), q, t.EventID, t.Type, t.Price, t.QtyStock, 0).Scan(&id)

	if err != nil {
		log.Panicf("db.QueryRow \n%v", err)
	}

	log.Printf("Defined new Ticket with ID: %d", id)
	return id, nil
}

func GetTicket(id int64) (models.Ticket, error) {

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Panicf("conn.Acquire \n%v", err)
	}
	defer db.Release()

	var t models.Ticket
	q := `SELECT * FROM ticket WHERE ticket_id = $1`
	rows, _ := db.Query(context.Background(), q, id)

	tickets, err := pgx.CollectRows(rows, pgx.RowToStructByPos[models.Ticket])
	if err != nil {
		log.Printf("CollectRows error: %v", err)
		return models.Ticket{}, err
	}

	if len(tickets) == 0 {
		log.Printf("No records found for TicketID: %d", id)
		return models.Ticket{}, errors.RaiseErr(404, "Record not found.")
	} 

	log.Printf("Retrieved Ticket with ID: %d", t.ID)
	return tickets[0], nil
}

func UpdateTicket(t models.Ticket) error {

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Panicf("conn.Acquire \n%v", err)
	}
	defer db.Release()

	q := `
		UPDATE ticket SET 
			type = COALESCE($1, type), 
			price = COALESCE($2, price), 
			qty_stock = COALESCE($3, qty_stock) 
		WHERE ticket_id = $4
	`

	_,err = db.Exec(context.Background(), q, t.Type, t.Price, t.QtyStock, t.ID)

	if err != nil {
		log.Panicf("db.Exec \n%v", err)
	}

	log.Printf("Updated Ticket with ID: %d", t.ID)
	return nil
}

func DeleteTicket(id int64) error {

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Panicf("conn.Acquire \n%v", err)
	}
	defer db.Release()

	q := `DELETE FROM ticket WHERE ticket_id = $1`
	_,err = db.Exec(context.Background(), q, id)

	if err != nil {
		log.Panicf("db.Exec \n%v", err)
	}

	log.Printf("Deleted Ticket with ID: %d", id)
	return nil
}
