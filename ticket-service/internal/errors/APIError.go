package errors

import (
	"errors"
	"fmt"
)

type APIError struct {
	Status int
	Err    error
}

func (e *APIError) Error() string {
	return fmt.Sprintf("status %d: err %v", e.Status, e.Err)
}

func RaiseErr(s int, m string) error {
	return &APIError{
		Status: s,
		Err:    errors.New(m),
	}
}
