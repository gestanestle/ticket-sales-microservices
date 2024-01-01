package db

import (
	"context"
	"log"
	"os"
	"sync"

	"github.com/jackc/pgx/v5/pgxpool"
)

var conn *pgxpool.Pool

func NewCon() *pgxpool.Pool {
	var err error
	conn, err = pgxpool.New(context.Background(), os.Getenv("PG_URL"))
	if err != nil {
		log.Printf("Unable to create connection pool: %v\n", err)
		os.Exit(1)
	}
	log.Println("Created connection to the database...")

	c, ioErr := os.ReadFile("_schema.sql")
	if ioErr != nil {
		log.Panicf("os.ReadFile %v", ioErr)
	}

	sql := string(c)
	_, err = conn.Exec(context.Background(), sql)
	if err != nil {
		log.Panicf("conn.Exec %v", err)
	}

	return conn
}

type Dao struct {
	Mu	sync.Mutex
}

const m503 = "Unable to process request at the moment. Try again later."