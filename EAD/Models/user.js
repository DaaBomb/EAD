const jwt=require('jsonwebtoken');
const config=require('config');
const Joi= require('joi');
const mongoose= require('mongoose');

const userSchema=new mongoose.Schema({
name:{
  type:String,
  required:true,
},
email:{
  type:String,
  required:true,
},
password:{
  type:String,
  required:true,
},
isResident:{
  type:Boolean,
  default:false
},
profession:{
  type:String,
  default:null
},
address:{
  society_id:{
    type:String,
    default:null
  },
  blockname:{
    type:String,
    default:null,
  },
  flatnum:{
    type:String,
    default:null,
  },
},
confirmed:{
  type:Boolean,
  default:false,
},
approved:{
  type:Boolean,
  default:false,
},
// profilepic: {
//     type: String,
//     required: true,
//     default: "no picture"
//   }
});
userSchema.methods.generateAuthToken=function(){
  const token=jwt.sign({_id:this._id,isAdmin:this.isAdmin},config.get('jwtPrivateKey'));
  return token
}
const User= mongoose.model('User',userSchema)
function validateUser(user){
  const schema={
    name:Joi.string().required(),
    email:Joi.string().required().email(),
    password:Joi.string().regex(/^(?=.*[!@#$%^&*])/).min(8).required(),
    // contact:Joi.string().trim().regex(/^[0-9]{7,10}$/).length(10).required()
    // profilepic: Joi.string().required(),
    // profilepicparse: Joi.any()
  };
  return Joi.validate(user,schema)
}

exports.User = User;
exports.validate =validateUser;

/* const token = req.header('x-auth-token')*/
