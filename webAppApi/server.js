require('dotenv').config()
const jwt = require("jsonwebtoken")

const express = require('express')
const app = express()
const mongoose = require('mongoose')
require('./auths/auth')
const passport = require('passport')

mongoose.connect(process.env.DATABASE_URL, {useNewUrlParser: true})
const db = mongoose.connection
db.on('error', (error) => console.log(error))
db.once('open', () => console.log('Database connected'))

app.use(express.json())

const api = require('./routes/api')

app.get('/auth/google',
  passport.authenticate('google', { scope:
      [ 'email', 'profile' ] }
));

app.get( '/auth/google/callback',
    passport.authenticate( 'google', {
        //successRedirect: '/auth/google/success',
        failureRedirect: '/auth/google/failure',
        session: false
}), (req, res) => {
    console.log('success');
    console.log('Logged In');
            const token = jwt.sign(
                {
                    email: req.user.email,

                },
                process.env.AUTH_SECRET,
                {
                    expiresIn: '48h'
                }

            );

            return res.status(200).json({
                message: "Auth successful",
                token: token
              });
});

// app.get('/auth/google/success', (req, res) => {
//   console.log('success');
//   console.log(req.body);
//   res.status(200).json({
//     message: 'Authentication successful'
// });

// });

app.get('/auth/google/failure', (req, res) => {
  console.log('Authentication failed');
  res.status(401).json({
    message: 'Authentication failed'
  });
});


app.use('/api', api)


app.listen(3000, () => console.log('server started'))