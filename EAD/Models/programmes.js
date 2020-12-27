const Joi= require('joi');
const mongoose= require('mongoose');

const programmeSchema=new mongoose.Schema({
name :{
  type:String,
  default:"Not Mentioned",
},
society_id:{
  type:String,
  default:null,
  required:true,
},
start_date:{
  type:Date,
  default:null,
  required :true,
},
time:{
  type:String,
  default:"00:00 AM"
},
description:{
  type:String,
  default:"No description mentioned"
},
creator_name:{
  type:String,
  default:null,
},
attending:{
type:Array,
default:null,
},
food_choice:{
  type:Boolean,
  default:false
},
veg:{
  type:Array,
  default:null
},
non_veg:{
  type:Array,
  default:null
},
programmes:[{
  name:{
    type:String,
    default:null,
  },
  description:{
    type:String,
    default:"No description mentioned"
  },
  participants:{
    type:Array,
    default:null,
  }
}]

});

const Programme= mongoose.model('Programme',programmeSchema)


exports.Programme = Programme;

/* const token = req.header('x-auth-token')*/
