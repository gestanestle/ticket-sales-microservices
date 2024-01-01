package middlewares

import (
	"account/internal/models"
	"context"
	"encoding/json"
	"log"
	"net/http"
	"strconv"
	"time"

	"github.com/gorilla/mux"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo/options"
)

func CreateUser(w http.ResponseWriter, r *http.Request) {

	user := models.User{}

	err := json.NewDecoder(r.Body).Decode(&user)
	if err != nil {
		log.Printf("json.NewDecoder %v", err)
		writeRes(w, 400, "Couldn't parse request object.", nil)
		return
	}

	user.CreatedAt = time.Now()

	crs, err := coll.Find(context.TODO(), bson.D{}, options.Find().SetSort(bson.D{{Key: "userId", Value: -1}}).SetLimit(int64(1)))
	if err != nil {
		log.Printf("coll.InsertOne %v", err)
		writeRes(w, 503, m503, nil)
		return
	}
	defer crs.Close(context.TODO())

	PREV := models.User{}
	for crs.Next(context.TODO()) {
		
		err := crs.Decode(&PREV)
		if err != nil {
			log.Printf("crs.Decode %V", err)
		}

		log.Printf("Previous record: %v", PREV)
	}

	if err := crs.Err(); err != nil {
		log.Fatal(err)
	}

	user.UserID = PREV.UserID + 1
	log.Printf("Inserting new record: %v", user)
	result, err := coll.InsertOne(context.TODO(), user)
	if err != nil {
		log.Printf("coll.InsertOne %v", err)
		writeRes(w, 503, m503, nil)
		return
	}

	newUser := models.User{}
	coll.FindOne(context.TODO(), bson.D{{Key: "_id", Value: result.InsertedID}}).Decode(&newUser)
	writeRes(w, 201, "User account created successfully.", map[string]any{"id": newUser.UserID})
}

func GetUser(w http.ResponseWriter, r *http.Request) {

	params := mux.Vars(r)
	param := params["id"]
	id, err := strconv.Atoi(param)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}

	user := models.User{}
	c, _ := coll.CountDocuments(context.Background(), bson.D{{Key: "userId", Value: id}})
	if c == 0 {
		writeRes(w, 404, "User not found.", nil)
		log.Printf("No user with ID %v found.", id)
		return
	}

	coll.FindOne(context.TODO(), bson.D{{Key: "userId", Value: id}}).Decode(&user)

	log.Printf("Retrieved user with ID: %v", id)
	writeRes(w, 200, "User account retrieved successfully.", user)
}

func UpdateUser(w http.ResponseWriter, r *http.Request) {

	params := mux.Vars(r)
	param := params["id"]
	id, err := strconv.Atoi(param)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}

	user := models.User{}

	err = json.NewDecoder(r.Body).Decode(&user)
	if err != nil {
		log.Printf("json.NewDecoder %v", err)
		writeRes(w, 400, "Couldn't parse request object.", nil)
		return
	}

	c, _ := coll.CountDocuments(context.Background(), bson.D{{Key: "userId", Value: id}})
	if c == 0 {
		writeRes(w, 404, "User not found.", nil)
		log.Printf("No user with ID %v found.", id)
		return
	}

	_, err = coll.UpdateOne(context.TODO(), 
							bson.D{{Key: "userId", Value: id}}, 
							bson.D{{Key: "$set", Value: bson.D{
									{Key: "lastName", Value: user.LastName},
									{Key: "firstName", Value: user.FirstName},
									{Key: "phone", Value: user.Phone}, 
									{Key: "birthdate", Value: user.Birthdate},
									}}}, 
							options.Update().SetUpsert(false))
	if err != nil {
		log.Printf("coll.UpdateOne %v", err)
		writeRes(w, 503, m503, nil)
		return
	}

	log.Printf("Updated user with ID: %v", id)
	writeRes(w, 200, "User account updated successfully.", nil)
}

func DeleteUser(w http.ResponseWriter, r *http.Request) {

	params := mux.Vars(r)
	param := params["id"]
	id, err := strconv.Atoi(param)
	if err != nil {
		writeRes(w, 400, "Error: parameter ID is not a valid int", nil)
		return
	}

	c, _ := coll.CountDocuments(context.Background(), bson.D{{Key: "userId", Value: id}})
	if c == 0 {
		writeRes(w, 204, "User not found.", nil)
		log.Printf("No user with ID %v found.", id)
		return
	}

	_, err = coll.DeleteOne(context.TODO(), bson.D{{Key: "userId", Value: id}})
	if err != nil {
		log.Printf("coll.DeleteOne %v", err)
		writeRes(w, 503, m503, nil)
		return
	}

	log.Printf("Deleted user with ID: %v", id)
	writeRes(w, 200, "User account deleted successfully.", nil)
} 