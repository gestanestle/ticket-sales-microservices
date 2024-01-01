package middlewares

import (
	"context"
	"log"
	"os"
	"sync"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

const m503 = "Unable to process request at the moment. Try again later."

type Dao struct {
	MU sync.Mutex
}

var coll *mongo.Collection

func InitClient() *mongo.Client {

	client, err := mongo.Connect(context.TODO(), options.Client().ApplyURI(os.Getenv("MONGODB_URL")))
	if err != nil {
		log.Panicf("No MongoDB URL provided. %v", err)
	}

	indexModel := mongo.IndexModel{
		Keys: bson.D{
			{Key: "userId", Value: 1},
		},
	}

	coll = client.Database("account_db").Collection("users")
	name, err := coll.Indexes().CreateOne(context.TODO(), indexModel)
	if err != nil {
		log.Panicf("coll.Indexes %v", err)
	}
	log.Printf("Created index: %v", name)
	return client
}
