
const Joi=require('joi');
const _ = require('lodash');
const {Concierge}=require('../Models/concierge');
const {Society} = require('../Models/society');
const {User}= require('../Models/user')
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();
const firebase = require("firebase-admin");
const serviceAccount = require('../swarm-7b179-firebase-adminsdk-rhxp1-cd1ee2af5d.json');

router.post('/',async(req, res)=>{
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let society = await Society.findById(req.body.user.address.society_id)
  let blocks = society.block
  if(!blocks.find(k => k.name == req.body.user.address.blockname)){
    return res.send({msg:"Block not registered"})
  }
  obj=blocks.find(k => k.name == req.body.user.address.blockname)
  if(!obj.flat_nums.find(k => k ==req.body.user.address.flatnum)) return res.send({msg:"Flat not registered"})

  let concierge=new Concierge({
    requirement:req.body.requirement,
    details:req.body.details,
    blockname:req.body.user.address.blockname,
    flatnum:req.body.user.address.flatnum,
    society_id:req.body.user.address.society_id,
    date_needed:req.body.date_needed,
    time_needed:req.body.time_needed
  })
  await concierge.save()
  var topic = 'security';
  var message = {
    data: {
      title:"concierge",
      message:JSON.stringify({concierge:req.body})
    },
    topic:topic
  };
  firebase.messaging().send(message)
    .then((response) => {
      // Response is a message ID string.
      console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
    });
  let dummy={
    msg:"successful",
    concierge:concierge,
  }
  res.send(dummy)
});

router.get('/getBySociety',async(req,res)=>{

  await Concierge.find({ society_id: req.query.society_id})
    .sort({date_created:-1})
    .limit(req.query.flag*10)
    .skip((req.query.flag-1)*10)
    .exec(function(err, messages) {
      messages
      res.send({msg:"successful",concierge:messages})
    });
})

router.get('/getByFlat',async(req,res)=>{

  await Concierge.find({ society_id: req.query.society_id,blockname:req.query.blockname,flatnum:req.query.flatnum})
    .sort({date_created:-1})
    .limit(req.query.flag*10)
    .skip((req.query.flag-1)*10)
    .exec(function(err, messages) {
      messages
      res.send({msg:"successful",concierge:messages})
    });

})

router.post('/responded',async(req,res)=>{
  let concierge = await Concierge.findById(req.body._id)
  if(concierge)
  {
    concierge.responded=true;
    concierge.approved = req.body.approved;
    concierge.save();
  }
  res.send({msg:"successful",concierge:concierge})
})

router.post('/done',async(req,res)=>{
  let concierge = await Concierge.findById(req.body._id)
  if(concierge)
  {
    concierge.resident_responded = true;
    concierge.done = req.body.done;
    concierge.save();
  }
  res.send({msg:"successful",concierge:concierge})
})

router.post('/addcomment',async(req,res)=>{
  const {error} = validateComment(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let concierge= await Concierge.findById(req.body.topic_id)
  if(!concierge) return res.send({msg:"Topic not available"})
  let arr=concierge.comments
  arr.push({
    date_created:Date.now(),
    person_name:req.body.user.name,
    comment:req.body.comment,
  })
  concierge.comments=arr
  await concierge.save()
  return res.send({msg:"successful",concierge:concierge})
})

function validate(req){
  const schema={
    requirement:Joi.string(),
    details:Joi.string(),
    user:Joi.object().required(),
    date_needed:Joi.date().required(),
    time_needed:Joi.string().required(),
    approved:Joi.boolean(),
    responded:Joi.boolean()
  };
  return Joi.validate(req ,schema)
}

function validateComment(req){
  const schema={
    topic_id:Joi.string().required(),
    comment:Joi.string().required(),
    user:Joi.object().required(),
  };
  return Joi.validate(req ,schema)
}


module.exports = router;
