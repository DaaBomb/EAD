
const Joi=require('joi');
const {Sports} = require('../Models/sports');
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();
const firebase = require("firebase-admin");
const serviceAccount = require('../swarm-7b179-firebase-adminsdk-rhxp1-cd1ee2af5d.json');

router.post('/',async(req, res)=>{
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let sport=new Sports({
    created_by:req.body.user,
    sport:req.body.sport,
    description:req.body.description,
    numberOfPlayers:req.body.numberOfPlayers,
    time:req.body.time
  })
  await sport.save()
  let dummy={
    msg:"successful",
    sport:sport,
  }
  res.send(dummy)
});

router.get('/getSports',async(req,res)=>{
  let sports = await Sports.find({"created_by.address.society_id":req.query.society_id})
  res.send({msg:"successful",sports:sports})
})

router.post('/register',async(req,res)=>{
  let sport = await Sports.findById(req.body._id)
  if(sport.participants==null){
    sport.participants=[req.body.user]
  }
  else{
    sport.participants.push(req.body.user)
  }
  await sport.save()
  let dummy={
    msg:"successful",
    sport:sport,
  }
  res.send(dummy)
})

function validate(req){
  const schema={
    from:Joi.object().required(),
    to:Joi.object().required(),
    message:Joi.string().required()
  };
  return Joi.validate(req ,schema)
}


module.exports = router;
