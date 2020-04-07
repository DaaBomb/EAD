const config=require('config');
const jwt= require('jsonwebtoken');
const Joi=require('joi');
const bcrypt=require('bcryptjs');
const _ = require('lodash');
const {User}=require('../Models/user');
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();

router.post('/',async(req, res)=>{
  console.log('coming');
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let user =await User.findOne({email:req.body.email});
  if (!user) return res.send({msg:'Invalid email or password'})

const validPassword=  await bcrypt.compare(req.body.password,user.password);
if (!validPassword) return res.send({msg:'Invalid email or password'})

user= await User.findOne({email:req.body.email}).select('-password');
if(!user.confirmed) return res.send({msg:'Please click on the link sent to to your email account'})

res.send({msg:"login successful",
          user:user
        })
});


function validate(req){
  const schema={
    email:Joi.string().required().email(),
    password:Joi.string().required()
  };
  return Joi.validate(req ,schema)
}
module.exports = router;
