const Joi= require('joi');
const mongoose= require('mongoose');

const conciergeSchema=new mongoose.Schema({
requirement :{
  type:String,
  default:"No requirement mentioned",
},
details:{
  type:String,
  default:"No details mentioned",
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
date_needed:{
  type:Date,
  default:Date.now,
  required:true,
},
time_needed:{
  type:String,
  default:null
},
approved:{
  type:Boolean,
  default:false
},
responded:{
  type:Boolean,
  default:false
},
done:{
  type:Boolean,
  default:false
},
resident_responded:{
  type:Boolean,
  default:false
},
comments:[{
  date_created:{
    type:Date,
    default:Date.now,
  },
  person_name:{
    type:String,
    default:null,
  },
  comment:{
    type:String,
    required:true
  }

}]
});

const Concierge= mongoose.model('Concierge',conciergeSchema)


exports.Concierge = Concierge;

/* const token = req.header('x-auth-token')*/
