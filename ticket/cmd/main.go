package main

import (
	"encoding/json"
	"log"
	"net/http"
	"ticket/internal/dao"

	"github.com/gorilla/mux"
)

func main() {
	
	db := dao.NewCon()
	defer db.Close()

	consumeTopics()

	r := mux.NewRouter()

    r.HandleFunc("/api/v3/events/{id}/ticket-details", defineTicket).Methods("POST")
	r.HandleFunc("/api/v3/ticket-details/{ticketID}", getTicket).Methods("GET")
	r.HandleFunc("/api/v3/ticket-details/{ticketID}", updateTicket).Methods("PUT")
	r.HandleFunc("/api/v3/ticket-details/{ticketID}", deleteTicket).Methods("DELETE")
	
	log.Println("Starting server on :4000")
    http.ListenAndServe(":4000", r)

}

func WriteJson(w http.ResponseWriter, data any) error {
	w.Header().Set("Content-Type", "application/json")
	return json.NewEncoder(w).Encode(data)
}


// func profile(w http.ResponseWriter, r *http.Request) {
// 	if r.Method != "GET" {
// 		w.Header().Set("Allow", "GET")
// 		http.Error(w, "Method Not Allowed", http.StatusMethodNotAllowed)
// 		return
// 	}

// 	id, err := strconv.Atoi(r.URL.Query().Get("id"))
// 	if err != nil || id < 1 {
// 		http.NotFound(w, r)
// 		return
// 	}

// 	w.Header().Set("Content-Type", "application/json")
// 	w.Write([]byte(`{"user": "delulu"}`))
// }
