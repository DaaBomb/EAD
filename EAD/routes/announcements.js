
const Joi=require('joi');
const {Announcement} = require('../Models/announcements');
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();
const firebase = require("firebase-admin");
const serviceAccount = require('../swarm-7b179-firebase-adminsdk-rhxp1-cd1ee2af5d.json');

router.post('/',async(req, res)=>{
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let announcement=new Announcement({
    announcement:req.body.announcement,
    society_id:req.body.society_id
  })
  await announcement.save()
  var topic = 'security';
  var message = {
    data: {
      title:"announcement",
      message:JSON.stringify({announcement:req.body})
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
    announcement:announcement,
  }
  res.send(dummy)
});

router.get('/getBySociety',async(req,res)=>{

  await Announcement.find({ society_id: req.query.society_id})
    .sort({date_created:-1})
    .limit(req.query.flag*10)
    .skip((req.query.flag-1)*10)
    .exec(function(err, messages) {
      messages
      res.send({msg:"successful",announcements:messages})
    });
})


function validate(req){
  const schema={
    announcement:Joi.string().required(),
    society_id:Joi.string().required()
  };
  return Joi.validate(req ,schema)
}


module.exports = router;
