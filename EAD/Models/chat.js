const Joi= require('joi');
const mongoose= require('mongoose');

const chatSchema=new mongoose.Schema({
from:{
  type:Object,
  required:true,
},
to:{
  type:Object,
  required:true,
},
date_created:{
  type:Date,
  default:Date.now
},
message:{
  type:String,
  required:true
}
});

const Chat= mongoose.model('Chat',chatSchema)


exports.Chat = Chat;

/* const token = req.header('x-auth-token')*/
