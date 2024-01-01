package main

import (
	"account/internal/middlewares"
	"context"
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

func main() {

	c := middlewares.InitClient()
	defer c.Disconnect(context.TODO())

	r := mux.NewRouter()

	r.HandleFunc("/api/v3/accounts", middlewares.CreateUser).Methods("POST")
	r.HandleFunc("/api/v3/accounts/{id}", middlewares.GetUser).Methods("GET")
	r.HandleFunc("/api/v3/accounts/{id}", middlewares.UpdateUser).Methods("PUT")
	r.HandleFunc("/api/v3/accounts/{id}", middlewares.DeleteUser).Methods("DELETE")

	log.Println("Starting server on :3000")
	http.ListenAndServe(":3000", r)
}