package main

import (
	"encoding/json"
	"log"
	"net/http"
	"ticket/internal/dao"
	"time"

	"github.com/gorilla/mux"
)

func main() {
	
	db := dao.NewCon()
	defer db.Close()

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

func WriteRes(w http.ResponseWriter, status int, msg string, data any) error {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	res := Response {
		Status: toString(status),
		Message: msg,
		Data: data,
		Timestamp: time.Now(),
	}
	return json.NewEncoder(w).Encode(res)
}

func toString(i int) string {
	switch i {
	case 200:
		return "OK"
	case 201:
		return "Created"
	case 400:
		return "Bad Request"
	case 404:
		return "Not Found"
	case 405:
		return "Method Not Allowed"
	case 408:
		return "Request Timeout"
	case 429:
		return "Too Many Requests"
	case 503: 
		return "Service Unavailable"
	}

	return ""
}