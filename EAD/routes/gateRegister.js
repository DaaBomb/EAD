
const Joi=require('joi');
const _ = require('lodash');
const {GateRegister}=require('../Models/gateRegister');
const {Society} = require('../Models/society');
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
  if(!blocks.find(k => k.name == req.body.blockname)){
    return res.send({msg:"Block not registered"})
  }
  const firebaseToken = req.body.token

  obj=blocks.find(k => k.name == req.body.blockname)
  if(!obj.flat_nums.find(k => k ==req.body.flatnum)) return res.send({msg:"Flat not registered"})
  let gateRegister=new GateRegister({
    visitor_name:req.body.visitor_name,
    block_visiting:req.body.blockname,
    flatnum_visiting:req.body.flatnum,
    society_id:req.body.user.address.society_id,
    purpose:req.body.purpose
  })
  await gateRegister.save()
  var topic = 'security';
  var message = {
    data: {
      title:"Security approval",
      message:JSON.stringify(gateRegister)
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

  let visitorDetails={
    msg:"successful",
    visitor:gateRegister,
  }
  res.send(visitorDetails)
});

router.post('/permission',async(req, res)=>{
  const {error} = validatePermission(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let gateRegister = await GateRegister.findById(req.body._id)
  if(!gateRegister.responded){
    const firebaseToken = req.body.token
    gateRegister.confirmed=req.body.confirmed
    gateRegister.responded=true;
    await gateRegister.save();
    var topic = 'security';
    var message = {
      data: {
        title:"Resident approval",
        message:JSON.stringify(gateRegister)
      },
      topic:topic
    };
    firebase.messaging().send(message)
      .then((response) => {
        console.log('Successfully sent message:', response);
      })
      .catch((error) => {
        console.log('Error sending message:', error);
      });
  }
  let visitorDetails={
    msg:"successful",
    visitor:gateRegister,
  }
  res.send(visitorDetails)
});

router.get('/getRequests',async(req,res)=>{
  const {error} = validateRequest(req.query);
  if(error) return res.send({msg:error.details[0].message});

  await GateRegister.find({ society_id: req.query.society_id})
    .sort({date_created:-1})
    .limit(req.query.flag*10)
    .skip((req.query.flag-1)*10)
    .exec(function(err, messages) {
      messages
      res.send({msg:"successful",notificationDetails:messages})
    });

})

router.get('/getUserRequests',async(req,res)=>{
  const {error} = validateUserRequest(req.query);
  if(error) return res.send({msg:error.details[0].message});

  await GateRegister.find({ society_id: req.query.society_id, block_visiting:req.query.blockname,flatnum_visiting:req.query.flatnum})
    .sort({date_created:-1})
    .limit(req.query.flag*10)
    .skip((req.query.flag-1)*10)
    .exec(function(err, messages) {
      messages
      res.send({msg:"successful",notificationDetails:messages})
    });

})

function validate(req){
  const schema={
    blockname:Joi.string().required(),
    visitor_name:Joi.string().required(),
    user:Joi.object().required(),
    flatnum:Joi.string().required(),
    purpose:Joi.string()
  };
  return Joi.validate(req ,schema)
}

function validatePermission(req){
  const schema={
    _id:Joi.string().required(),
    confirmed:Joi.boolean().required(),
  };
  return Joi.validate(req ,schema)
}

function validateRequest(req){
  const schema={
    flag:Joi.number().required(),
    society_id:Joi.string().required(),
  };
  return Joi.validate(req ,schema)
}

function validateUserRequest(req){
  const schema={
    flag:Joi.number().required(),
    society_id:Joi.string().required(),
    blockname:Joi.string().required(),
    flatnum:Joi.string().required()
  };
  return Joi.validate(req ,schema)
}


module.exports = router;
