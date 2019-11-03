import json
from flask import Flask, request, Response
import pymongo
import os
password_cred = os.environ.get('MONGO_CRED')
auth_str = "mongodb+srv://i-o-u-o-mi-access:"+password_cred+"@i-o-u-o-mi-860kg.azure.mongodb.net/test?retryWrites=true&w=majority"
client = pymongo.MongoClient(auth_str)
app = Flask(__name__)

users = {}
transactions = {}

@app.route("/hello")
def index():
    return "<h1>Hello Azure!</h1>"

@app.route("/addUser", methods = ["POST"])
def addUser():
    content = request.get_json()
    users[content["id"]] = content
    return Response(
        json.dumps(content), status = 200, mimetype = "application/json"
    )

@app.route("/removeUser/<userID>", methods = ["DELETE"])
def removeUser(userID):
    users.pop(userID, None)

@app.route("/addTransaction", methods = ["POST"])
def addTransaction():
    content = request.get_json()
    transactions[content["id"]] = content
    return Response(
        json.dumps(content), status = 200, mimetype = "application/json"
    )

@app.route("/updateTransaction", methods = ["POST"])
def updateTransaction():
    content = request.get_json()
    transactions[content["id"]] = content
    print(transactions)
    return Response(
        json.dumps(content), status = 200, mimetype = "application/json"
    )

@app.route("/login", methods = ["POST"])
def login():
	content = request.get_json()
	phone_number = content['phone_number']
	password = content['password']
	people_col = client.iouomi_db['People']
	query = {'Phone Number': phone_number}
	response_doc = people_col.find(query)
	response_dict = dict()
	for x in response_doc:
		response_dict = x
	if response_dict['Password'] == password:
		return Response(json.dumps(['successful login']), status=200, mimetype="application/json" )
	else:
		return Response(json.dumps(['ERROR: Unsuccessful login']), status=401, mimetype="application/json")

# def deleteTransaction():

# def retrieveTransaction():








if __name__ == "__main__":
        app.run(host='0.0.0.0', debug=True)
