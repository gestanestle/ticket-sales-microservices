package db

import (
	"context"
	"log"
	"ticket/internal/models"

	"github.com/jackc/pgx/v5"
)

func (d *Dao) PersistEvent(event models.Event) error {

	d.Mu.Lock()
	defer d.Mu.Unlock()

	db, err := conn.Acquire(context.Background())
	if err != nil {
		panic("Error acquiring connection to database.")
    }

	defer db.Release()

	var exists bool
	q := "SELECT EXISTS(SELECT 1 FROM event WHERE event_id = $1)"
	err = db.QueryRow(context.Background(), q, event.ID).Scan(&exists)
	if err != nil {
		log.Panicf("db.QueryRow \n%v", err)
	}

	if exists {
		log.Println("Event already persisted.")
		return nil
	}

	tx, err := conn.BeginTx(context.Background(), pgx.TxOptions{})
	if err != nil {
		log.Panicf("conn.BeginTx \n%v", err)
	}
	defer tx.Rollback(context.Background())

	q = `INSERT INTO event (event_id, is_active) VALUES ($1, $2)`
	_,err = tx.Exec(context.Background(), q, event.ID, event.IsActive)

	if err != nil {
		log.Panicf("tx.Exec \n%v", err)
	}

	if err := tx.Commit(context.Background()); err != nil {
		log.Panicf("tx.Commit \n%v", err)
	}

	return nil
}