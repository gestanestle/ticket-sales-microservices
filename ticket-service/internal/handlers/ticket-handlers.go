package handlers

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"ticket/internal/db"
	"ticket/internal/errors"
	"ticket/internal/models"

	"github.com/gorilla/mux"
)

func DefineTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["eventId"]
	eventId, err := strconv.Atoi(paramId)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}

	ticket := models.Ticket{}

	err = json.NewDecoder(r.Body).Decode(&ticket)
	ticket.EventID = int64(eventId)
	if err != nil {
		log.Printf("json.NewDecoder \n%v", err)
		writeRes(w, 400, "Couldn't parse request object.", nil)
		return
	}

	d := db.Dao{}

	id, err := d.DefineTicket(ticket)

	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	writeRes(w, 201, "Ticket defined successfully.", map[string]int64{"id": id})
}

func GetAllTickets(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["eventId"]
	eventId, err := strconv.Atoi(paramId)
	if err != nil {
		s := 400
		m := "Error: parameter ID is not a valid int"
		log.Println(m)
		writeRes(w, s, m, nil)
		return
	}

	d := db.Dao{}
	tickets, err := d.GetAllTickets(int64(eventId))
	var s int
	if err != nil {

		apiError := err.(*errors.APIError)
		s = apiError.Status
		m := apiError.Err.Error()
		writeRes(w, s, m, nil)
		return
	}

	writeRes(w, 200, "All tickets for event retrieved successfully.", tickets)
}

func GetTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["ticketId"]
	ticketId, err := strconv.Atoi(paramId)
	if err != nil {
		s := 400
		m := "Error: parameter ID is not a valid int"
		writeRes(w, s, m, nil)
		return
	}

	d := db.Dao{}
	ticket, err := d.GetTicket(int64(ticketId))
	var s int
	if err != nil {

		apiError := err.(*errors.APIError)
		s = apiError.Status
		m := apiError.Err.Error()
		writeRes(w, s, m, nil)
		return
	}

	writeRes(w, 200, "Ticket retrieved successfully.", ticket)
}

func UpdateTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["ticketId"]
	ticketId, err := strconv.Atoi(paramId)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}

	ticket := models.Ticket{}

	err = json.NewDecoder(r.Body).Decode(&ticket)
	ticket.ID = int64(ticketId)
	if err != nil {
		log.Printf("json.NewDecoder \n%v", err)
		writeRes(w, 400, "Couldn't parse request object.", nil)
		return
	}

	d := db.Dao{}
	err = d.UpdateTicket(ticket)

	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	writeRes(w, 200, "Ticket updated successfully.", nil)
}

func DeleteTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["ticketId"]
	ticketId, err := strconv.Atoi(paramId)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}

	d := db.Dao{}
	err = d.DeleteTicket(int64(ticketId))

	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	writeRes(w, 200, "Ticket deleted successfully.", nil)
}