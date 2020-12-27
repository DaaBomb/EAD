const Joi= require('joi');
const mongoose= require('mongoose');

const gateRegisterSchema=new mongoose.Schema({
visitor_name :{
  type:String,
  default:true,
},
block_visiting:{
  type:String,
  default:null,
  required:true,
},
flatnum_visiting:{
  type:String,
  defualt:null,
  required:true,
},
society_id:{
  type:String,
  required:true,
},
confirmed:{
  type:Boolean,
  default:false,
},
responded:{
  type:Boolean,
  default:false,
},
purpose:{
  type:String,
  default:"No purpose mentioned"
},
date_created:{
  type:Date,
  default:Date.now,
},

});

const GateRegister= mongoose.model('GateRegister',gateRegisterSchema)


exports.GateRegister = GateRegister;

/* const token = req.header('x-auth-token')*/
