
const Joi=require('joi');
const _ = require('lodash');
const {Concierge}=require('../Models/concierge');
const {Society} = require('../Models/society');
const {User}= require('../Models/user')
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();

router.post('/',async(req, res)=>{
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let society = await Society.findById(req.body.user.address.society_id)
  let blocks = society.block
  if(!blocks.find(k => k.name == req.body.blockname)){
    return res.send({msg:"Block not registered"})
  }
  obj=blocks.find(k => k.name == req.body.blockname)
  if(!obj.flat_nums.find(k => k ==req.body.flatnum)) return res.send({msg:"Flat not registered"})

  let manager = await User.find({address.society_id:req.body.user.address.society_id,profeesion:"manager"})
  if(!manager) res.send({msg:"No manager registered"})


  let concierge=new Concierge({
    requirement:req.body.requirement,
    blockname:req.body.user.address.blockname,
    flatnum:req.body.user.address.flatnum,
    society_id:req.body.user.address.society_id,
    manager_id:manager
  })
  await concierge.save()
  let concierge={
    msg:"successful",
    concierge:concierge,
  }
  res.send(concierge)
});


function validate(req){
  const schema={
    blockname:Joi.string().required(),
    visitor_name:Joi.string().required(),
    user:Joi.object().required(),
    flatnum:Joi.string().required(),
  };
  return Joi.validate(req ,schema)
}


module.exports = router;
