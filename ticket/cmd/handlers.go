package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"ticket/internal/dao"
	"ticket/internal/errors"
	"ticket/internal/models"
	"time"

	"github.com/gorilla/mux"
)

type Response struct {
	Status 		string 		`json:"status"`
	Message 	string		`json:"message"`
	Data    	any			`json:"data"`
	Timestamp   time.Time	`json:"timestamp"`
}

func defineTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["id"]
	eventId, err := strconv.Atoi(paramId)
	if err != nil {
		s := 400
		m := "Error: parameter ID is not a valid int"
		log.Println(m)
		
		w.WriteHeader(s)
		WriteJson(w, Response {
			Status: toString(s),
			Message: m,
			Data: nil,
			Timestamp: time.Now(),
		})
		return
	}
	
	ticket := models.Ticket{}

	err = json.NewDecoder(r.Body).Decode(&ticket)
	ticket.EventID = int(eventId)
	if err != nil {
		log.Fatalf("Unable to define ticket. %v", err.Error())
	}

	id, err := dao.DefineTicket(ticket)

	var s int
	if err != nil {

		apiError := err.(*errors.APIError)
		s = apiError.Status
			m := apiError.Err.Error()
			

			w.WriteHeader(s)
			WriteJson(w, Response {
				Status: toString(s),
				Message: m,
				Data: nil,
				Timestamp: time.Now(),
			})
	}

	s = 201

	w.WriteHeader(s)
	WriteJson(w, Response {
		Status: toString(s),
		Message: "Ticket defined successfully.",
		Data: map[string]int64{"id": id},
		Timestamp: time.Now(),
	})
}

func toString(s int) string {
	switch s {
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