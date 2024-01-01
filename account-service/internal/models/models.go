package models

import (
	"time"
)

type User struct {
	UserID	  int64				 `bson:"userId"`
	LastName  string             `bson:"lastName"`
	FirstName string             `bson:"firstName"`
	Phone     string             `bson:"phone"`
	Birthdate time.Time          `bson:"birthdate"`
	CreatedAt time.Time		     `bson:"createdAt"`
}