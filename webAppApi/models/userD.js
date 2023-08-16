const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const bcrypt = require('bcrypt');


const userSchema = new Schema({
    email: { 
        type: String,
        required: true 
    },

    username: {
        type: String,
        required: true

    },

    name: {
        type: String,
        required: true

    },
    userImageUrl: {
        type: String,
        
    },

    req: {
        type: String
    },
    
    lastActive: {
        type: Date,

    },
    desc: {
        type: String,

    },
    phoneNumber: {
        type: String,

    },

    userType: {
        type: String,
        required: true
    },

    versionCode: {
        type: Number
    },

    versionName: {
        type: String
    },

    date_created : {
        type: Date,
        required: true,
        default: Date.now

    }

});

userSchema.methods.encryptPassword = function(password) {
    return bcrypt.hashSync(password, bcrypt.genSaltSync(10)); };
  
userSchema.methods.validPassword = function(password) {
    return bcrypt.compareSync(password, this.password);
  };

module.exports = mongoose.model("UserDet", userSchema);