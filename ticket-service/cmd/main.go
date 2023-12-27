package main

import (
	"log"
	"net/http"
	"ticket/internal/db"

	"github.com/gorilla/mux"
)

func main() {
	
	d := db.NewCon()
	defer d.Close()

	consumeTopics()

	r := mux.NewRouter()

    r.HandleFunc("/api/v3/events/{eventId}/ticket-details", defineTicket).Methods("POST")
	r.HandleFunc("/api/v3/events/{eventId}/ticket-details", getAllTickets).Methods("GET")
	r.HandleFunc("/api/v3/ticket-details/{ticketId}", getTicket).Methods("GET")
	r.HandleFunc("/api/v3/ticket-details/{ticketId}", updateTicket).Methods("PUT")
	r.HandleFunc("/api/v3/ticket-details/{ticketId}", deleteTicket).Methods("DELETE")
	r.HandleFunc("/api/v3/tickets", purchaseTickets).Methods("POST")
	r.HandleFunc("/api/v3/tickets/{purchaseId}", getPurchase).Methods("GET")
	r.HandleFunc("/api/v3/tickets/{purchaseId}", updatePurchase).Methods("PUT")

	log.Println("Starting server on :4000")
    http.ListenAndServe(":4000", r)
}