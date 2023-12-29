package handlers

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"ticket/internal/db"
	"ticket/internal/errors"
	"ticket/internal/kafka"
	"ticket/internal/models"

	"github.com/gorilla/mux"
)

func PurchaseTickets(w http.ResponseWriter, r *http.Request) {

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

	e := models.PurchaseEvent{}
	eId, _ := d.GetEventID(p.TicketID)
	pr, _ := d.GetPurchase(IDs[0])
	
	e.EventID = eId
	e.CustomerID = pr.CustomerID
	e.DateTime = pr.CreatedAt

	prd := kafka.KafkaProducer{}
	str, err := json.Marshal(e)
	if err != nil {
		log.Printf("json.Marshal \n%v", err)
		return
	}
	prd.Publish(string(str))

	log.Printf("Sucessful transaction. Returning IDs: %v", IDs)
	writeRes(w, 200, "Purchase created successfully.", map[string][]int64{"id": IDs})
}

func GetPurchase(w http.ResponseWriter, r *http.Request) {

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

	writeRes(w, 200, "Purchase retrieved successfully.", purchase)
}

func UpdatePurchase(w http.ResponseWriter, r *http.Request) {

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
