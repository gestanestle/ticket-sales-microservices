package main

import (
	"log"
	"net/http"
	"os"
	"os/signal"
	"ticket/internal/db"
	"ticket/internal/handlers"
	"ticket/internal/kafka"

	"github.com/gorilla/mux"
)

func main() {
	
	d := db.NewCon()
	defer d.Close()

	kafka.Consume()

	signals := make(chan os.Signal, 1)
	signal.Notify(signals, os.Interrupt)

	r := mux.NewRouter()

    r.HandleFunc("/api/v3/events/{eventId}/ticket-details", handlers.DefineTicket).Methods("POST")
	r.HandleFunc("/api/v3/events/{eventId}/ticket-details", handlers.GetAllTickets).Methods("GET")
	r.HandleFunc("/api/v3/ticket-details/{ticketId}", handlers.GetTicket).Methods("GET")
	r.HandleFunc("/api/v3/ticket-details/{ticketId}", handlers.UpdateTicket).Methods("PUT")
	r.HandleFunc("/api/v3/ticket-details/{ticketId}", handlers.DeleteTicket).Methods("DELETE")
	r.HandleFunc("/api/v3/tickets", handlers.PurchaseTickets).Methods("POST")
	r.HandleFunc("/api/v3/tickets/{purchaseId}", handlers.GetPurchase).Methods("GET")
	r.HandleFunc("/api/v3/tickets/{purchaseId}", handlers.UpdatePurchase).Methods("PUT")

	log.Println("Starting server on :4000")
    http.ListenAndServe(":4000", r)
}