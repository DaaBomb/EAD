const express = require('express');

const connectDB = require('./config/db');

const app = express();

const bodyParser = require('body-parser');
const { resolve } = require('path');
const cloudinary = require('cloudinary');
const firebase = require("firebase-admin");
const serviceAccount = require('./swarm-7b179-firebase-adminsdk-rhxp1-cd1ee2af5d.json');



cloudinary.config({
  cloud_name: 'saltandpeppercloud',
  api_key: '291595787748935',
  api_secret: 'P8nXfLmLbJuhdTljN9MmY0lNLLs'
});

var multer = require('multer');
const cloudinaryStorage = require('multer-storage-cloudinary');
var parser = multer({
  storage: cloudinaryStorage({
    cloudinary: cloudinary,
    folder: 'uploads',
    filename: function(req, file, cb) {
      cb(undefined, file.originalname);
    }
  })
});

//connect MongoDB
connectDB();

firebase.initializeApp({
  credential: firebase.credential.cert(serviceAccount),
  databaseURL: "https://swarm-7b179.firebaseio.com"
});

app.get('/', (req, res) => res.send('API running'));

//Middleware for access to req.body

app.use(express.static(resolve(__dirname, '/public')));

app
  .use(express.json({ extend: false }))
  .use(bodyParser.urlencoded({ extended: true }));

//routes
app.use('/api/users', require('./routes/users'));
app.use('/api/auth', require('./routes/auth'));
app.use('/api/society', require('./routes/society'));
app.use('/api/forums', require('./routes/forums'));
app.use('/api/gateregister', require('./routes/gateRegister'));
app.use('/api/programmes', require('./routes/programmes'));
app.use('/api/concierge', require('./routes/concierge'));
app.use('/api/announcements', require('./routes/announcements'));
app.use('/api/chat', require('./routes/chat'));
app.use('/api/sports', require('./routes/sports'));

app.post('/uploadImages', parser.array('image'), (req, res, next) => {
  // req.files will show you the uploaded files
  // and req.body will show you the rest of your form data
  console.log(req.files[0].url);
  res.json({ msg: 'done' });
  // var CryptoJS = require('crypto-js'); //replace thie with script tag in browser env

  // //encrypt
  // var rawStr = req.files[0].url;
  // var wordArray = CryptoJS.enc.Utf8.parse(rawStr);
  // var base64 = CryptoJS.enc.Base64.stringify(wordArray);
  // console.log('encrypted:', base64);

  // //decrypt
  // var parsedWordArray = CryptoJS.enc.Base64.parse(base64);
  // console.log(parsedWordArray);
  // var parsedStr = parsedWordArray.toString(CryptoJS.enc.Utf8);
  // console.log('parsed:', parsedStr);
});

const PORT = process.env.PORT || 5000; //heroku runs star script in package.json file. The PORT variable in env is also for heroku

app.listen(PORT, () => console.log(`server started at ${PORT}`));
