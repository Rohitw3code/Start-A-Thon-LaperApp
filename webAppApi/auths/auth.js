const passport = require('passport');
const GoogleStrategy = require( 'passport-google-oauth2' ).Strategy;
const User = require("../models/user")
const UserD = require("../models/userD")
require('dotenv').config()




passport.use(new GoogleStrategy({
    clientID:     process.env.GOOGLE_CLIENT_ID,
    clientSecret: process.env.GOOGLE_CLIENT_SECRET,
    callbackURL: "http://localhost:3000/auth/google/callback",
    passReqToCallback   : true
  },
  async function(request, accessToken, refreshToken, profile, done) {
    try {
        const result = await User.findOne({
            gpid : profile.id
        })

        if (result == null){
            console.log('User not found');
            // create a new user
            try {

      
                const user = new User();
                user.email =  profile.email,
                user.gpid = profile.id,
                user.isGAuth = true,
                user.date_created = Date.now()
                console.log('new user created, pushing to database');
        
                const userDet = new UserD();
                userDet.email =  profile.email,
                userDet.username = profile.displayName,
                userDet.name = profile.displayName,
                userDet.userImageUrl = "",
                userDet.lastActive = Date.now(),
                userDet.desc = "",
                userDet.phoneNumber = "",
                userDet.userType = "user",
                userDet.versionCode = "",
                userDet.versionName = "",
                userDet.date_created = Date.now()
                console.log('new user created, pushing to database');
        
                const saveRes = await user.save();
                const saveResData = await userDet.save();
                    if ((!saveRes) || (!saveResData)) {
                        console.log('Some error occured')
                      res.status(500).json({
                        message: "An error has occured here."
                      })
                    } else {
                        console.log('User created successfully')
                    }
                    done(null, user);
                }
             catch(e) {
                console.log(e)
            //     res.status(500).json({
            //       message: "An error has occured initially."
            //     })
            }

        }
        else{
          done(null, result);
            
            }
        }

    


    
    
    catch(err){
      console.log(err);
      res.status(500).json({
        error: err
      });
    }

    
  }
));