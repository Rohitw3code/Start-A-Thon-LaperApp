const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const bcrypt = require('bcrypt');


const expertSchema = new Schema({
    gpid: {
        type: String,
    },
    
    email: { 
        type: String,
        required: true 
    },
    password: { 
        type: String
        
    },

    isGAuth: {
        type: Boolean,
        required: true
    },

    date_created : {
        type: Date,
        required: true,
        default: Date.now

    }

});

expertSchema.methods.encryptPassword = function(password) {
    return bcrypt.hashSync(password, bcrypt.genSaltSync(10)); };
  
expertSchema.methods.validPassword = function(password) {
    return bcrypt.compareSync(password, this.password);
  };

module.exports = mongoose.model("ExpertAuth", expertSchema);