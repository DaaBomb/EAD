
const Joi=require('joi');
const bcrypt=require('bcryptjs');
const _ = require('lodash');
const {Society}=require('../Models/society');
const {User}= require('../Models/user')
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();



router.post('/addbuilder',async(req, res)=>{
  const {error} = validate(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let society =await Society.findOne({name:req.body.name,city:req.body.city});
  if (society) return res.send({msg:'Society already exists'})
  let user=await User.findById(req.body.user._id)
  user.isResident=false
  user.profession="builder"
  user.approved=true
  society=new Society({
    name:req.body.name,
    address:req.body.address,
    city:req.body.city,
  })
  await society.save()
  user.address.society_id=society._id
  await user.save()
  let societyDetails={
    msg:"successful",
    society:society,
    user:user,
  }
  res.send(societyDetails)
});




router.post('/flat',async(req,res)=>{
  const {error} = validateFlat(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let society = await Society.findOne({name:req.body.name,city:req.body.city})
  if(!society) return res.send({msg:"Society does not exist"})
  let user=await User.findById(req.body.user._id)
  user.address.society_id=society._id
  user.address.blockname=req.body.blockname
  user.address.flatnum=req.body.flatnum
  user.isResident=true
  user.approved=true
  await user.save()
  let arr = society.block
  if(!arr.find(k => k.name==req.body.blockname)){
    arr.push({name:req.body.blockname, flat_nums:[req.body.flatnum]})
    society.block=arr
    await society.save()
    return res.send({msg:"successful",
              society:society,
              user:user,
            })
  }
  var index=arr.findIndex(k => k.name ==req.body.blockname)
  if(!arr[index].flat_nums.find(k => k == req.body.flatnum)){
    arr[index].flat_nums.push(req.body.flatnum)
  }
  society.block=arr
  await society.save()
  return res.send({msg:"successful",
            society:society,
            user:user
          })
});




router.post('/staff',async(req,res)=>{
  const {error} = validateStaff(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let society = await Society.findOne({name:req.body.name,city:req.body.city})
  if(!society) return res.send({msg:"Society does not exist"})
  let user=await User.findById(req.body.user._id)
  user.address.society_id=society._id
  user.isResident=false
  user.profession=req.body.profession
  user.approved=true
  await user.save()
  return res.send({msg:"successful",
            user:user
          })
});




router.get('/blocks',async(req,res)=>{
  const {error} = validateBlock(req.body);
  if(error) return res.send({msg:error.details[0].message});

  let society = await Society.findById(req.body.user.address.society_id)
  if(!society) return res.send({msg:"Society not availabale"})

  let blockDetails = society.block
  return res.send({msg:"successful",blockDetails:blockDetails})
})




router.get('/cities',async(req,res)=>{
  Society.distinct('city',function(err,results){
    res.send({msg:"successful",cities:results})
  })
})



router.get('/societies',async(req,res)=>{
  Society.distinct('name',function(err,results){
    res.send({msg:"successful",societies:results})
  })
})



router.get('/socitiesbycity',async(req,res)=>{
  let result= await Society.find({city:req.query.city}).select('name -_id')
  res.send({msg:"successful",societies:result})
})


function validate(req){
  const schema={
    name:Joi.string().required(),
    address:Joi.string().required(),
    city:Joi.string().required(),
    user:Joi.object().required(),
  };
  return Joi.validate(req ,schema)
}

function validateFlat(req){
  const schema={
    name:Joi.string().required(),
    city:Joi.string().required(),
    blockname:Joi.string().required(),
    flatnum:Joi.string().required(),
    user:Joi.object().required(),
  };
  return Joi.validate(req ,schema)
}

function validateStaff(req){
  const schema={
    name:Joi.string().required(),
    city:Joi.string().required(),
    user:Joi.object().required(),
    profession:Joi.string().required(),
  };
  return Joi.validate(req ,schema)
}

function validateBlock(req){
  const schema={
      user:Joi.object().required(),
  };
  return Joi.validate(req, schema)
}


module.exports = router;
