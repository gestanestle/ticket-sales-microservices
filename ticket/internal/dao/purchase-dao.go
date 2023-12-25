package dao

import (
	"context"
	"log"
	"math/rand"
	"ticket/internal/errors"
	"ticket/internal/models"
	"time"

	"github.com/jackc/pgx/v5"
)

const charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
const m503 = "Unable to process request at the moment. Try again later."

func generateSerialCode() string {
	b := make([]byte, 10)
	for i := range b {
		b[i] = charset[rand.Intn(len(charset))]
	}
	return string(b)
}

func  PurchaseTickets (p models.PurchaseReq)([]int64, error) {

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Printf("conn.Acquire \n%v", err)
		return []int64{}, errors.RaiseErr(503, m503)
    }

	defer db.Release()

	var exists bool
	q := "SELECT EXISTS (SELECT 1 FROM ticket WHERE ticket_id = $1)"
	err = db.QueryRow(context.Background(), q, p.TicketID).Scan(&exists)
	if err != nil {
		log.Printf("db.QueryRow \n%v", err)
	}
	if !exists {
		return []int64{}, errors.RaiseErr(400, "Ticket doesn't exist.")
	}

	tx, err := conn.BeginTx(context.Background(), pgx.TxOptions{})
	if err != nil {
		log.Printf("conn.BeginTx \n%v", err)
		return []int64{}, errors.RaiseErr(503, m503)
	}
	defer tx.Rollback(context.Background())


	var isAvailable bool
	q = `SELECT EXISTS (SELECT 1 FROM ticket WHERE ticket_id = $1 AND (qty_stock - qty_sold) >= $2) AS result;`
	err = db.QueryRow(context.Background(), q, p.TicketID, p.Quantity).Scan(&isAvailable)
	log.Print(isAvailable)
	if err != nil {
		log.Printf("db.QueryRow \n%v", err)
		return []int64{}, errors.RaiseErr(503, m503)	
	}
	if !isAvailable {
		return []int64{}, errors.RaiseErr(400, "Tickets sold out.")
	}
	
	var IDs []int64

	n := 0
	for n < p.Quantity {
	
		var id int64
		q = `INSERT INTO purchase (
				ticket_code, ticket_id,
				status, customer_id, created_at
				) VALUES ($1, $2, $3, $4, $5)
				RETURNING purchase_id`
		err = tx.QueryRow(context.Background(), q, generateSerialCode(), p.TicketID,
					string(models.BOOKED), p.CustomerID, time.Now()).Scan(&id)

		IDs = append(IDs, id)

		if err != nil {
			log.Printf("tx.QueryRow \n%v", err)
			return []int64{}, errors.RaiseErr(503, m503)
		}

		q = `UPDATE ticket SET qty_sold = qty_sold + 1 WHERE ticket_id = $1`
		_,err = tx.Exec(context.Background(), q, p.TicketID)
		if err != nil {
			log.Printf("tx.QueryRow \n%v", err)
			return []int64{}, errors.RaiseErr(503, m503)
		}
		
		n += 1 	
	}

	if err := tx.Commit(context.Background()); err != nil {
		return []int64{}, errors.RaiseErr(503, m503)
	}

	return IDs, nil
}

func GetPurchase(id int64) (models.Purchase, error) {

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Printf("conn.Acquire \n%v", err)
		return models.Purchase{}, errors.RaiseErr(503, m503)
	}
	defer db.Release()

	q := `SELECT * FROM purchase WHERE purchase_id = $1`
	rows, _ := db.Query(context.Background(), q, id)

	purchase, err := pgx.CollectExactlyOneRow(rows, pgx.RowToStructByPos[models.Purchase])
	if err != nil {
		log.Printf("pgx.CollectRows \n%v", err)
		return models.Purchase{}, errors.RaiseErr(503, m503)
	}

	log.Printf("Retrieved Purchase with ID: %d", id)
	return purchase, nil
}

func UpdatePurchase(id int64, s string) error {

	db, err := conn.Acquire(context.Background())
	if err != nil {
		log.Printf("conn.Acquire \n%v", err)
		return errors.RaiseErr(503, m503)
    }
	defer db.Release()

	var exists bool
	q := "SELECT EXISTS(SELECT 1 FROM purchase WHERE purchase_id = $1)"
	err = db.QueryRow(context.Background(), q, id).Scan(&exists)
	if err != nil {
		log.Printf("db.QueryRow \n%v", err)
		return errors.RaiseErr(503, m503)
	}

	if !exists {
		return errors.RaiseErr(400, "Purchase doesn't exist.")
	}

	tx, err := conn.BeginTx(context.Background(), pgx.TxOptions{})
	if err != nil {
		log.Printf("conn.BeginTx \n%v", err)
		return errors.RaiseErr(503, m503)
	}
	defer tx.Rollback(context.Background())

	q = `UPDATE purchase SET status = $1 WHERE purchase_id = $2`
	_,err = tx.Exec(context.Background(), q, s, id)

	if err != nil {
		log.Printf("tx.Exec \n%v", err)
		return errors.RaiseErr(503, m503)
	}

	if err := tx.Commit(context.Background()); err != nil {
		log.Printf("tx.Commit \n%v", err)
		return errors.RaiseErr(503, m503)
	}

	return nil
}