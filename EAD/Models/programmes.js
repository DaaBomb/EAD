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
end_date:{
  type:Date,
  default:null,
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
programmes:[{
  name:{
    type:String,
    default:null,
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
