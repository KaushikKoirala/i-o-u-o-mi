from flask import Flask
app = Flask(__name__)

@app.route("/hello")
def index():
    return "<h1>Hello Azure!</h1>"

if __name__ == '__main__':
        app.run(host='0.0.0.0', debug=True)