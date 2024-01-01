package middlewares

import (
	"encoding/json"
	"net/http"
	"time"
)

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
	case 204:
		return "No Content"
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
