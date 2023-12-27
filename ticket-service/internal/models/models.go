package models

import "time"

type BrokerMessage struct {
	ID string 		`json:"id"`
	Payload string  `json:"payload"`
}

type Event struct {
	ID       int64 	`json:"eventId"`
	IsActive bool 	`json:"isActive"`
} 

type Ticket struct {
	ID       int64     `json:"ticketId"`
	EventID  int64 	 `json:"eventId"`
	Type     *string  `json:"ticketType"`
	Price    *float64 `json:"price"`
	QtyStock *int     `json:"qtyStock"`
	QtySold  int     `json:"qtySold"`
}

type Status string
	
const (
	BOOKED Status = "BOOKED"
	CANCELED Status = "CANCELED"
	REFUNDED Status = "REFUNDED"
)

func IsValidStatus(in string) bool {
	switch in {
	case string(BOOKED), 
		 string(CANCELED), 
		 string(REFUNDED):
		return true
	}
	return false
}

type Purchase struct {
	ID          int64    `json:"purchaseId"`
	TicketCode  string `json:"ticketCode"`
	TicketID 	int64	`json:"ticketId"`
	Status      Status `json:"status"`
	CustomerID int64    `json:"customerId"`
	CreatedAt time.Time `json:"createdAt"`
}

type PurchaseReq struct {
	TicketID 	int64	`json:"ticketId"`
	Quantity	int	`json:"quantity"`
	Status      Status  `json:"status"`
	CustomerID  int64   `json:"customerId"`
}