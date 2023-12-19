package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"ticket/internal/config"
)

func main() {
	
	config.ConsumeTopics()
	mux := http.NewServeMux();

	// http.HandleFunc("/api/v3/events", )

	mux.HandleFunc("/", home)
    log.Print("Starting server on :4000")
    err := http.ListenAndServe(":4000", mux)
    log.Fatal(err)


}

func WriteJson(w http.ResponseWriter, status int, data any) error {
	w.WriteHeader(status)
	w.Header().Set("Content-Type", "application/json")
	return json.NewEncoder(w).Encode(data) 
}

func home(w http.ResponseWriter, r *http.Request) {
    w.Write([]byte("haru warudo"))
 }

func profile(w http.ResponseWriter, r *http.Request) {
	if r.Method != "GET" {
		w.Header().Set("Allow", "GET")
		http.Error(w, "Method Not Allowed", http.StatusMethodNotAllowed)
		return
	}

	id, err := strconv.Atoi(r.URL.Query().Get("id"))
	if err != nil || id < 1 {
		http.NotFound(w, r)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.Write([]byte(`{"user": "delulu"}`))
}
