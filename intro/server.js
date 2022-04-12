var express = require('express'); // package for hosting the webserver
var app = express();
var fs = require("fs"); // package for accessing the filesystem

app.use(express.json())

// packages/dependencies can be installed using the command npm i <<package-name>>
// e.g. npm i express

// specify suburl of endpoint
app.get('/listUsers', function (req, res) {
    // read the users.json file from current directory
    fs.readFile(__dirname + "/" + "users.json", 'utf8', function (err, data) {
        console.log(data);
        // end read and send data
        res.end(data);
    });
})

app.post('/addUser', function (req, res) {
    // read the users.json file from current directory
    fs.readFile(__dirname + "/" + "users.json", 'utf8', function (err, data) {
        data = JSON.parse(data);
        console.log(data);
        // create/update user
        containsUser = false;
        for (i = 0; i < data.length; i++) {
            if(data[i].id === req.body.id) {
                containsUser = true;
                data[i] = req.body;
            }
        }

        if(!containsUser) {
            data.push(req.body);
        }

        // overwrite file with new data
        fs.writeFile("users.json", JSON.stringify(data), function (err) { });
        res.end(JSON.stringify(data));
    });
})

var server = app.listen(8081, function () {
    var host = server.address().address
    var port = server.address().port

    console.log("Example app listening at http://%s:%s", host, port)
})


