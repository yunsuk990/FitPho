const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const auth = require('./routes/auth');
const library = require('./routes/library');
const favorites = require('./routes/favorites');
const calendar = require('./routes/calendar');
const {
	verifyToken
} = require('./middleware/auth_middleware');

const PORT = process.env.PORT;
const app = express();

app.use(cookieParser());
app.use(bodyParser.urlencoded({
	extended: false
}));
app.use(bodyParser.json());
app.use('/public', express.static(path.join(__dirname, 'public')));

app.use("/auth", auth);
app.use("/library", verifyToken, library);
app.use("/favorites", verifyToken, favorites);
app.use("/calendar", verifyToken, calendar);

app.use(function(err, req, res, next) {
	console.error(err.stack);
	res.status(500).send('Something broke!');
});

app.use(function(req, res, next) {
	res.status(404).send('Sorry cant find that!');
});

app.listen(PORT,
	() => console.log(`Server running on ${PORT}...`))

module.exports = app;