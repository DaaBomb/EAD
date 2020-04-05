const Joi= require('joi');
const mongoose= require('mongoose');

const forumSchema=new mongoose.Schema({
is_discussion :{
  type:Boolean,
  default:true,
},
topic:{
  type:String,
  default:null,
  required:true,
},
date_created:{
  type:Date,
  default:Date.now,
},
description:{
  type:String,
  default:"No description mentioned"
},
creator_name:{
  type:String,
  default:null,
},
society_id:{
  type:String,
  required:true,
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

const Forum= mongoose.model('Forum',forumSchema)


exports.Forum = Forum;

/* const token = req.header('x-auth-token')*/
