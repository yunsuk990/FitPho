const express = require('express'); 
const path = require('path');
const bodyParser = require('body-parser')
const routes = require('./routes/auth.js');

const PORT = process.env.PORT;
const app = express();

app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());
app.use('/public', express.static(path.join(__dirname, 'public')));

app.use("/auth", routes);

app.listen(PORT, 
()=> console.log(`Server running on ${PORT}...`))