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
  if(error) return res.send(error.details[0].message);

  let user =await User.findOne({email:req.body.email});
  if (!user) return res.send('Invalid email or password')

const validPassword=  await bcrypt.compare(req.body.password,user.password);
if (!validPassword) return res.send('Invalid email or password')

if(!user.confirmed) return res.send('Please click on the link sent to to your email account')

const token=user.generateAuthToken();
const userDetails={user:user,
                  token:token
                  }
res.send("login successful")
});


function validate(req){
  const schema={
    email:Joi.string().required().email(),
    password:Joi.string().required()
  };
  return Joi.validate(req ,schema)
}
module.exports = router;
