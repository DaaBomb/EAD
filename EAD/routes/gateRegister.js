
const Joi=require('joi');
const _ = require('lodash');
const {GateRegister}=require('../Models/gateRegister');
const {Society} = require('../Models/society');
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
  let gateRegister=new GateRegister({
    visitor_name:req.body.visitor_name,
    block_visiting:req.body.blockname,
    flatnum_visiting:req.body.flatnum,
    society_id:req.body.user.address.society_id,
  })
  await gateRegister.save()
  let visitorDetails={
    msg:"successful",
    visitor:gateRegister,
  }
  res.send(visitorDetails)
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
