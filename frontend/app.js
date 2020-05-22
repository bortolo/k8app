const port = process.env.NODE_PORT || 8081;
// https://nodesource.com/blog/running-your-node-js-app-with-systemd-part-1/

var express = require('express');
var session = require('cookie-session'); // Loads the piece of middleware for sessions
var bodyParser = require('body-parser'); // Loads the piece of middleware for managing the settings
var urlencodedParser = bodyParser.urlencoded({ extended: false });

var request = require('request');

var app = express();

app.set('views', './views');
app.set('view engine', 'ejs');


/* Using sessions */
app.use(session({secret: 'todotopsecret'}))


/* If there is no to do list in the session,
we create an empty one in the form of an array before continuing */
.use(function(req, res, next){
    if (typeof(req.session.todolist) == 'undefined') {
        req.session.todolist = [];
    }
    next();
})

/* The to do list and the form are displayed */
.get('/todo', function(req, res) {
    res.render('todo', {todolist: req.session.todolist});
})

/* Adding an item to the to do list */
.post('/todo/add/', urlencodedParser, function(req, res) {
    if (req.body.newtodo != '') {
        req.session.todolist.push(req.body.newtodo);
    }
    res.redirect('/todo');
})

/* Deletes an item from the to do list */
.get('/todo/delete/:id', function(req, res) {
    if (req.params.id != '') {
        req.session.todolist.splice(req.params.id, 1);
    }
    res.redirect('/todo');
})

/* Call backend server */
.get('/todo/callserver', function(req, res) {
  request('http://test.mydomain.com:8080/HelloWorldServlet/HelloWorld', function(err, response, body) {
      console.log('statusCode:', response && response.statusCode);
      console.log(body);
      req.session.todolist.push(body);
      res.redirect('/todo');
  });
})

/* Redirects to the to do list if the page requested is not found */
.use(function(req, res, next){
    res.redirect('/todo');
})

.listen(port);
