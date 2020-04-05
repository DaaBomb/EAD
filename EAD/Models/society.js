const jwt=require('jsonwebtoken');
const config=require('config');
const Joi= require('joi');
const mongoose= require('mongoose');

const societySchema=new mongoose.Schema({
name:{
  type:String,
  required:true,
},
block:[{
    name:{
      type:String,
      default:null,
    },
    flat_nums:{
      type:Array,
      default:null,
    }
}],

address:{
  type:String,
  default:null
},
city:{
  type:String,
  default:null
}
});

const Society= mongoose.model('Society',societySchema)
function validateSociety(society){
  const schema={
    name:Joi.string().required(),
    // contact:Joi.string().trim().regex(/^[0-9]{7,10}$/).length(10).required()
    // profilepic: Joi.string().required(),
    // profilepicparse: Joi.any()
  };
  return Joi.validate(society,schema)
}

exports.Society = Society;
exports.validate =validateSociety;

/* const token = req.header('x-auth-token')*/
