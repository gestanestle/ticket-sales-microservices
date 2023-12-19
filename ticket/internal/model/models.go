package model

import "time"

type BrokerMessage struct {
	ID string `json:"id"`
	// Payload Event `json:"payload"`
	Payload string `json:"payload"`
}

type Event struct {
	ID       int `json:"eventId"`
	IsActive bool `json:"isActive"`
} 

type Ticket struct {
	ID       int     `json:"ticketId"`
	Type     string  `json:"ticketType"`
	Price    float64 `json:"price"`
	QtyStock int     `json:"qtyStock"`
	QtySold  int     `json:"qtySold"`
}
	

type Status struct {
	BOOKED   string
	CANCELED string
	REFUNDED string
}

type Purchase struct {
	ID          int    `json:"id"`
	TicketCode  string `json:"ticketCode"`
	TicketType  string `json:"ticketType"`
	TicketPrice float64    `json:"ticketPrice"`
	Quantity    int    `json:"quantity"`
	Status      Status `json:"status"`
	PurchasedAt time.Time `json:"purchasedAt"`
	PurchasedBy int64    `json:"purchasedBy"`
}