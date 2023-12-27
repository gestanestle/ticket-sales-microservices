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
	paramId := params["eventId"]
	eventId, err := strconv.Atoi(paramId)
	if err != nil {
		WriteRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}
	
	ticket := models.Ticket{}

	err = json.NewDecoder(r.Body).Decode(&ticket)
	ticket.EventID = int64(eventId)
	if err != nil {
		log.Printf("json.NewDecoder \n%v", err)
		WriteRes(w, 400, "Couldn't parse request object.", nil)
		return
	}

	id, err := dao.DefineTicket(ticket)

	if err != nil {
		apiError := err.(*errors.APIError)
		WriteRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	WriteRes(w, 201, "Ticket defined successfully.", map[string]int64{"id": id})
}

func getAllTickets(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["eventId"]
	eventId, err := strconv.Atoi(paramId)
	if err != nil {
		s := 400
		m := "Error: parameter ID is not a valid int"
		log.Println(m)
		WriteRes(w, s, m, nil)
		return
	}

	
	tickets, err := dao.GetAllTickets(int64(eventId))
	var s int
	if err != nil {

		apiError := err.(*errors.APIError)
		s = apiError.Status
		m := apiError.Err.Error()
		WriteRes(w, s, m, nil)
		return
	}

	WriteRes(w, 200, "All tickets for event retrieved successfully.", tickets)
}

func getTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["ticketId"]
	ticketId, err := strconv.Atoi(paramId)
	if err != nil {
		s := 400
		m := "Error: parameter ID is not a valid int"
		WriteRes(w, s, m, nil)
		return
	}

	
	ticket, err := dao.GetTicket(int64(ticketId))
	var s int
	if err != nil {

		apiError := err.(*errors.APIError)
		s = apiError.Status
		m := apiError.Err.Error()
		WriteRes(w, s, m, nil)
		return
	}

	WriteRes(w, 200, "Ticket retrieved successfully.", ticket)
}

func updateTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["ticketId"]
	ticketId, err := strconv.Atoi(paramId)
	if err != nil {
		WriteRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}
	
	ticket := models.Ticket{}

	err = json.NewDecoder(r.Body).Decode(&ticket)
	ticket.ID = int64(ticketId)
	if err != nil {
		log.Printf("json.NewDecoder \n%v", err)
		WriteRes(w, 400, "Couldn't parse request object.", nil)
		return
	}
	
	err = dao.UpdateTicket(ticket)

	if err != nil {
		apiError := err.(*errors.APIError)
		WriteRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	WriteRes(w, 200, "Ticket updated successfully.", nil)
}

func deleteTicket(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	paramId := params["ticketId"]
	ticketId, err := strconv.Atoi(paramId)
	if err != nil {
		WriteRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}
	
	err = dao.DeleteTicket(int64(ticketId))

	if err != nil {
		apiError := err.(*errors.APIError)
		WriteRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	WriteRes(w, 200, "Ticket deleted successfully.", nil)
}

func purchaseTickets(w http.ResponseWriter, r *http.Request) {

	p := models.PurchaseReq{}

	err := json.NewDecoder(r.Body).Decode(&p)
	if err != nil {
		log.Printf("json.NewDecoder \n%v", err)
		WriteRes(w, 400, "Couldn't parse request object.", nil)
		return
	}
	log.Printf("Received payload: %v", p)
	
	d := dao.Dao{}
	IDs, err := d.PurchaseTickets(p)

	if err != nil {
		apiError := err.(*errors.APIError)
		WriteRes(w, apiError.Status, apiError.Err.Error(), nil)
		log.Print(apiError.Error())
		return
	}

	log.Printf("Sucessful transaction. Returning IDs: %v", IDs)
	WriteRes(w, 200, "Purchase created successfully.", map[string][]int64{"id": IDs})
}

func getPurchase(w http.ResponseWriter, r *http.Request) {

	params := mux.Vars(r)
	paramId := params["purchaseId"]
	purchaseId, err := strconv.Atoi(paramId)
	if err != nil {
		WriteRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}
	
	d := dao.Dao{}
	purchase, err := d.GetPurchase(int64(purchaseId))
	if err != nil {
		apiError := err.(*errors.APIError)
		WriteRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	WriteRes(w, 200,  "Purchase retrieved successfully.", purchase)
}

func updatePurchase(w http.ResponseWriter, r *http.Request) {

	params := mux.Vars(r)
	paramId := params["purchaseId"]
	purchaseId, err := strconv.Atoi(paramId)
	if err != nil {
		WriteRes(w, 400, "Error: parameter ID is not a valid int", nil)
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
		WriteRes(w, 400, "Invalid request body.", nil)
		return
	}

	d := dao.Dao{}
	err = d.UpdatePurchase(int64(purchaseId), s)
	if err != nil {
		apiError := err.(*errors.APIError)
		WriteRes(w, apiError.Status, apiError.Err.Error(), nil)
		return
	}

	WriteRes(w, 200, "Purchase status updated successfully.", nil)
}
