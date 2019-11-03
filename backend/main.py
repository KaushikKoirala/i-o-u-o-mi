import json
from flask import Flask, request, Response
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

@app.route("/removeUser", methods = ["DELETE"])
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

    

# def deleteTransaction():

# def retrieveTransaction():








if __name__ == "__main__":
        app.run(debug=True)