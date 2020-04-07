const Joi= require('joi');
const mongoose= require('mongoose');

const conciergeSchema=new mongoose.Schema({
requirement :{
  type:String,
  default:"No requirement mentioned",
},
blockname:{
  type:String,
  default:null,
  required:true,
},
date_created:{
  type:Date,
  default:Date.now,
},
flatnum:{
  type:String,
  default:null,
  required:true,
},
society_id:{
  type:String,
  required:true,
},
manager_id:{
  type:String,
  default:null,
  required:true,
}

});

const Concierge= mongoose.model('Concierge',conciergeSchema)


exports.Concierge = Concierge;

/* const token = req.header('x-auth-token')*/
