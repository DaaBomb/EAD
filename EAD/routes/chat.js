
const Joi=require('joi');
const {Chat} = require('../Models/chat');
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();
const firebase = require("firebase-admin");
const serviceAccount = require('../swarm-7b179-firebase-adminsdk-rhxp1-cd1ee2af5d.json');

router.post('/',async(req, res)=>{
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let chat=new Chat({
    from:req.body.from,
    to:req.body.to,
    message:req.body.message
  })
  await chat.save()
  var topic = 'security';
  var message = {
    data: {
      title:"chat",
      message:JSON.stringify({chat:req.body})
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
    chat:chat,
  }
  res.send(dummy)
});

router.get('/getMessages',async(req,res)=>{
  let chat = await Chat.find({$or:[{'from._id':req.query.from_id,'to._id':req.query.to_id},{'to._id':req.query.from_id,'from._id':req.query.to_id}]})
  res.send({msg:"successful",chats:chat})
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
