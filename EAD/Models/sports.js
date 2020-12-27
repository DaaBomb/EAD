const Joi= require('joi');
const mongoose= require('mongoose');

const sportsSchema=new mongoose.Schema({
created_by:{
  type:Object,
  required:true
},
date_created:{
  type:Date,
  default:Date.now
},
sport:{
  type:String,
  required:true
},
description:{
  type:String,
  default:"No description mentioned"
},
numberOfPlayers:{
  type:Number,
  default:0,
  required:true
},
time:{
  type:String,
  required:true
},
participants:{
  type:Array,
  default:null
}
});

const Sports= mongoose.model('Sports',sportsSchema)

exports.Sports = Sports;

/* const token = req.header('x-auth-token')*/
