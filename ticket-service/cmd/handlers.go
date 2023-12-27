package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"ticket/internal/db"
	"ticket/internal/errors"
	"ticket/internal/models"
	"time"

	"github.com/gorilla/mux"
)

// GENERAL UTILS
type Response struct {
	Status 		string 		`json:"status"`
	Message 	string		`json:"message"`
	Data    	any			`json:"data"`
	Timestamp   time.Time	`json:"timestamp"`
}

func writeRes(w http.ResponseWriter, status int, msg string, data any) error {
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

// HANDLERS
func defineTicket(w http.ResponseWriter, r *http.Request) {
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

	d:= db.Dao{}

	id, err := d.DefineTicket(ticket)

	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	writeRes(w, 201, "Ticket defined successfully.", map[string]int64{"id": id})
}

func getAllTickets(w http.ResponseWriter, r *http.Request) {
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

	d:= db.Dao{}
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

func getTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["ticketId"]
	ticketId, err := strconv.Atoi(paramId)
	if err != nil {
		s := 400
		m := "Error: parameter ID is not a valid int"
		writeRes(w, s, m, nil)
		return
	}

	d:= db.Dao{}
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

func updateTicket(w http.ResponseWriter, r *http.Request) {
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
	
	d:= db.Dao{}
	err = d.UpdateTicket(ticket)

	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	writeRes(w, 200, "Ticket updated successfully.", nil)
}

func deleteTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["ticketId"]
	ticketId, err := strconv.Atoi(paramId)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}
	
	d:= db.Dao{}
	err = d.DeleteTicket(int64(ticketId))

	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	writeRes(w, 200, "Ticket deleted successfully.", nil)
}

func purchaseTickets(w http.ResponseWriter, r *http.Request) {

	p := models.PurchaseReq{}

	err := json.NewDecoder(r.Body).Decode(&p)
	if err != nil {
		log.Printf("json.NewDecoder \n%v", err)
		writeRes(w, 400, "Couldn't parse request object.", nil)
		return
	}
	log.Printf("Received payload: %v", p)
	
	d := db.Dao{}
	IDs, err := d.PurchaseTickets(p)

	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		log.Print(apiError.Error())
		return
	}

	log.Printf("Sucessful transaction. Returning IDs: %v", IDs)
	writeRes(w, 200, "Purchase created successfully.", map[string][]int64{"id": IDs})
}

func getPurchase(w http.ResponseWriter, r *http.Request) {

	params := mux.Vars(r)
	paramId := params["purchaseId"]
	purchaseId, err := strconv.Atoi(paramId)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}
	
	d := db.Dao{}
	purchase, err := d.GetPurchase(int64(purchaseId))
	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	writeRes(w, 200,  "Purchase retrieved successfully.", purchase)
}

func updatePurchase(w http.ResponseWriter, r *http.Request) {

	params := mux.Vars(r)
	paramId := params["purchaseId"]
	purchaseId, err := strconv.Atoi(paramId)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}

	p := make(map[string]string)

	err = json.NewDecoder(r.Body).Decode(&p)	
	if err != nil {
		log.Printf("Unable to parse request body. %v", err.Error())
		return
	}

	s := p["status"]
	if s == "" || !models.IsValidStatus(s) {
		writeRes(w, 400, "Invalid request body.", nil)
		return
	}

	d := db.Dao{}
	err = d.UpdatePurchase(int64(purchaseId), s)
	if err != nil {
		apiError := err.(*errors.APIError)
		writeRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	writeRes(w, 200, "Purchase status updated successfully.", nil)
}
