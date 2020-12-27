
const Joi=require('joi');
const _ = require('lodash');
const {Programme}=require('../Models/programmes');
const {Society} = require('../Models/society');
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();

router.post('/',async(req, res)=>{
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let society = await Society.findById(req.body.user.address.society_id)
  if(!society) return res.send("Society not found")

  let obj= new Programme({
    name:req.body.name,
    society_id:req.body.user.address.society_id,
    start_date:req.body.start_date,
    time:req.body.time,
    food_choice:req.body.food_choice,
    description:req.body.description,
    programmes:req.body.programmes,
    creator_name:req.body.user.name
  })
  await obj.save()
  res.send({msg:"successful",eventDetails:obj})
});




router.post('/register',async(req, res)=>{
  const {error} = validateRegister(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let society = await Society.findById(req.body.user.address.society_id)
  if(!society) return res.send("Society not found")

  a = await Programme.findById(req.body.eventDetails._id)
  if(!a) return res.send("Event does not exist anymore")

  if (req.body.attending){
    if (a.attending==null)
      a.attending=[req.body.user._id]
    else{
      if(!a.attending.find(k=>k==req.body.user._id)){
        a.attending.push(req.body.user._id)
      }
    }
    if(req.body.food_choice != null){
      if(req.body.food_choice)
        food_arr=a.veg
      else
        food_arr=a.non_veg
      if (food_arr==null)
        food_arr=[req.body.user._id]
      else{
        if(!food_arr.find(k=>k==req.body.user._id)){
          food_arr.push(req.body.user._id)
        }
      }
      if(req.body.food_choice)
        a.veg=food_arr
      else
        {
            a.non_veg=food_arr
        }
    }
    arr=a.programmes
    for(var i=0;i<arr.length;i++){
      arr[i].participants = arr[i].participants.filter(k=>k != req.body.user._id)
    }
    for(var i =0;i<req.body.programmeslist.length;i++){
        if(!arr.find(k=>k.name==req.body.programmeslist[i])) return res.send("Selected programme doesnt exist")
        let temp=arr.findIndex(k=>k.name==req.body.programmeslist[i])
        if(arr[temp].participants !=null){
          if(!arr[temp].participants.find(k=>k==req.body.user._id))
            {arr[temp].participants.push(req.body.user._id)}
        }
        else{
          arr[temp].participants=[req.body.user._id]
        }
    }
    a.programmes=arr
  }
  await a.save()
  res.send({msg:"successful",eventDetails:a})
});

router.get('/getEvents',async(req,res)=>{
  const {error} = validategetEvent(req.query);
  if(error) return res.send({msg:error.details[0].message});

  await Programme.find({ society_id: req.query.society_id})
    .sort({start_date:-1})
    .limit(req.query.flag*10)
    .skip((req.query.flag-1)*10)
    .exec(function(err, messages) {
      messages
      res.send({msg:"successful",eventDetails:messages})
    });

})



function validate(req){
  const schema={
    name:Joi.string().required(),
    start_date:Joi.date().required(),
    time:Joi.string().required(),
    food_choice:Joi.boolean().required(),
    description:Joi.string(),
    programmes:Joi.array().required(),
    user:Joi.object().required(),
  };
  return Joi.validate(req ,schema)
}

function validateRegister(req){
  const schema={
    attending:Joi.boolean().required(),
    programmeslist:Joi.array(),
    food_choice:Joi.boolean(),
    eventDetails:Joi.object().required(),
    user:Joi.object().required(),
  };
  return Joi.validate(req ,schema)
}

function validategetEvent(req){
  const schema={
    flag:Joi.number().required(),
    society_id:Joi.string().required(),
  };
  return Joi.validate(req ,schema)
}


module.exports = router;
