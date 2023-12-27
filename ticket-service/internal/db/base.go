package db

import (
	"sync"
)

type Dao struct {
	Mu	sync.Mutex
}