package dao

import (
	"context"
	"log"
	"ticket/internal/errors"
	"ticket/internal/models"

	"github.com/jackc/pgx/v5"
)

func (d *Dao) DefineTicket(t models.Ticket) (int64, error) {

	d.Mu.Lock()
	defer d.Mu.Unlock()

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Printf("conn.Acquire \n%v", err)
		return -1, errors.RaiseErr(503, m503)
	}
	defer db.Release()

	var id int64
	q := `INSERT INTO ticket (event_id, type, price, qty_stock, qty_sold) VALUES ($1, $2, $3, $4, $5) RETURNING ticket_id`
	err = db.QueryRow(context.Background(), q, t.EventID, t.Type, t.Price, t.QtyStock, 0).Scan(&id)

	if err != nil {
		log.Printf("db.QueryRow \n%v", err)
		return -1, errors.RaiseErr(503, m503)
	}

	log.Printf("Defined new Ticket with ID: %d", id)
	return id, nil
}

func (d *Dao) GetAllTickets(id int64) ([]models.Ticket, error) {

	d.Mu.Lock()
	defer d.Mu.Unlock()

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Printf("conn.Acquire \n%v", err)
		return []models.Ticket{}, errors.RaiseErr(503, m503)
	}
	defer db.Release()

	q := `SELECT * FROM ticket WHERE event_id = $1`
	rows, _ := db.Query(context.Background(), q, id)

	tickets, err := pgx.CollectRows(rows, pgx.RowToStructByPos[models.Ticket])
	if err != nil {
		log.Printf("pgx.CollectRows \n%v", err)
		return []models.Ticket{}, err
	}

	if len(tickets) == 0 {
		log.Printf("No records found for EventID: %d", id)
		return []models.Ticket{}, errors.RaiseErr(404, "Record not found.")
	} 

	log.Printf("Retrieved tickets for Event with ID: %d", id)
	return tickets, nil
}

func (d *Dao) GetTicket(id int64) (models.Ticket, error) {

	d.Mu.Lock()
	defer d.Mu.Unlock()

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Printf("conn.Acquire \n%v", err)
		return models.Ticket{}, errors.RaiseErr(503, m503)
	}
	defer db.Release()

	q := `SELECT * FROM ticket WHERE ticket_id = $1`
	rows, _ := db.Query(context.Background(), q, id)

	ticket, err := pgx.CollectExactlyOneRow(rows, pgx.RowToStructByPos[models.Ticket])
	if err != nil {
		log.Printf("pgx.CollectRows \n%v", err)
		return models.Ticket{}, err
	}

	log.Printf("Retrieved Ticket with ID: %d", id)
	return ticket, nil
}

func (d *Dao) UpdateTicket(t models.Ticket) error {

	d.Mu.Lock()
	defer d.Mu.Unlock()

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Printf("conn.Acquire \n%v", err)
		return errors.RaiseErr(503, m503)
	}
	defer db.Release()

	q := `
		UPDATE ticket SET 
			type = COALESCE($1, type), 
			price = COALESCE($2, price), 
			qty_stock = COALESCE($3, qty_stock) 
		WHERE ticket_id = $4

		AND (
			cast($1 as VARCHAR(255)) IS NOT NULL AND $1 IS DISTINCT FROM type OR
			cast($2 as FLOAT) IS NOT NULL AND $2 IS DISTINCT FROM price OR
			cast($3 as INTEGER) IS NOT NULL AND $3 IS DISTINCT FROM qty_stock
	   )
	`

	_,err = db.Exec(context.Background(), q, t.Type, t.Price, t.QtyStock, t.ID)

	if err != nil {
		log.Printf("db.Exec \n%v", err)
		return errors.RaiseErr(503, m503)
	}

	log.Printf("Updated Ticket with ID: %d", t.ID)
	return nil
}

func (d *Dao) DeleteTicket(id int64) error {

	d.Mu.Lock()
	defer d.Mu.Unlock()
	
	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Printf("conn.Acquire \n%v", err)
		return errors.RaiseErr(503, m503)
	}
	defer db.Release()

	q := `DELETE FROM ticket WHERE ticket_id = $1`
	_,err = db.Exec(context.Background(), q, id)

	if err != nil {
		log.Printf("db.Exec \n%v", err)
		return errors.RaiseErr(503, m503)
	}

	log.Printf("Deleted Ticket with ID: %d", id)
	return nil
}
