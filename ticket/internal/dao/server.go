package dao

import (
	"context"
	"log"
	"os"

	"github.com/jackc/pgx/v5/pgxpool"
)

var conn *pgxpool.Pool

func NewCon() *pgxpool.Pool {
	var err error
	conn, err = pgxpool.New(context.Background(), "postgresql://postgres:postgres@localhost:5432/ticket_db")
	if err != nil {
		log.Printf("Unable to create connection pool: %v\n", err)
		os.Exit(1)
	}
	log.Println("Created connection to the database...")
	return conn
}
