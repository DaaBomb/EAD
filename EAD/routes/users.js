const auth=require('../middleware/auth')
const config=require('config');
const jwt= require('jsonwebtoken');
const bcrypt=require('bcryptjs')
const _ = require('lodash')
const {User,validate}=require('../Models/user')
const mongoose = require('mongoose');
const express = require('express');
const router = express.Router();
const multer = require("multer");
const uuidv1=require('uuid/v1');
const sendEmail = require('./email.send')
const msgs = require('./email.msgs')
const templates = require('./email.templates')

const storage = multer.diskStorage({
  destination: function(req, file, cb) {
    cb(null, "./uploads/");
  },
  filename: function(req, file, cb) {
    cb(null,uuidv1()+ file.originalname);
  }
});

const fileFilter = (req, file, cb) => {
  //accept on certain types of files

  if (
    file.mimetype === "image/jpg" ||
    file.mimetype === "image/jpeg" ||
    file.mimetype === "image/png" ||
    file.mimetype === "image/gif"
  ) {
    cb(null, true);
  } else {
    cb(null, false);
  }
};
const upload = multer({
  storage: storage,
  limits: { fileSize: 1024 * 1024 * 100 },
  fileFilter: fileFilter
}).single("profilepicparse");


// router.get('/adetails',async(req,res)=>{
//   console.log('inconsole')
//   const users= await User.find()
//   const professionals = await Professional.find()
//   const orders = await Order.find({is_confirmed:true})
//   var Admindetails = {
//     no_users : users.length,
//     no_professionals : professionals.length,
//     no_orders : orders.length
//   }
//
//   res.send(Admindetails);
// });


router.get('/loggedin',auth,async(req,res)=>{
  const user= await User.findById(req.user._id).select('-password')
  res.send(user);
});

router.get('/all',async(req,res)=>{
  const user= await User.find()
  res.send(user);
})


// router.get('/:id',async(req,res)=>{
//   const user = await User.findById(req.params.id)
//   res.send(user);
// })

// router.get('/mybookings/:id',async(req, res)=>{
//   const order = await Order.find({user_id:req.params.id,is_confirmed:true})
//   var item = null;
//   orders = [];
//   for(item in order){
//     temp =   order[item];
//     var professional = await Professional.findById(temp.professional)
//     var slot = await Slot.findById(temp.slot._id)
//     var ordered_date = temp.order_date.date.toString() + '/' + temp.order_date.month.toString() + '/' + temp.order_date.year.toString()
//     var ser_chosen= [];var item2=null;
//     for(item2 in temp.services_chosen)
//     {
//       ser_chosen.push(item2)
//     }
//     var Orderdetails= {
//       order_id:temp._id,
//       professional_id: professional._id,
//       user_id: req.body.id,
//       services_chosen:ser_chosen,
//       total_cost:temp.total_cost,
//       date:ordered_date,
//       prof_name:professional.user.name,
//       prof_phone:professional.phonenumber,
//       slot:slot.start_time,
//       address:temp.address[2],
//       city:temp.address[3],
//   }
//   orders.push(Orderdetails)
//   }
//   res.send(orders.reverse());
// })

router.post('/updateprofile',async(req, res)=>{
  const about = req.body.about
  const contact = req.body.contact
  const email = req.body.email
  const address = req.body.address
  console.log('...................................................................')
  console.log(req.body.contact)
  User.findOne({ _id: req.body.id }, function (err, doc){
    console.log(doc)
    // if(doc != null){
    if(about){
      doc.about = about;
    }
    if(contact){
      doc.contact = contact;
    }
    if(email){
      doc.email = email;
    }
    if(address){
      doc.address = address;
    }



    doc.save();
  // }
  });

res.send('Updated')
})





// router.get('/mypendingpayments/:id',async(req, res)=>{
//   const order = await Order.find({user_id:req.params.id,is_paid:false,is_confirmed:true})
//   var item = null;
//   orders = [];
//   for(item in order){
//     temp =   order[item];
//     var professional = await Professional.findById(temp.professional)
//     var slot = await Slot.findById(temp.slot._id)
//     var ordered_date = temp.order_date.date.toString() + '/' + temp.order_date.month.toString() + '/' + temp.order_date.year.toString()
//     var ser_chosen= [];var item2=null;
//     for(item2 in temp.services_chosen)
//     {
//       ser_chosen.push(item2)
//     }
//     var Orderdetails= {
//       order_id:temp._id,
//       professional_id: professional._id,
//       user_id: req.body.id,
//       services_chosen:ser_chosen,
//       total_cost:temp.total_cost,
//       date:ordered_date,
//       prof_name:professional.user.name,
//       prof_phone:professional.phonenumber,
//       slot:slot.start_time,
//       address:temp.address[2],
//       city:temp.address[3],
//   }
//   orders.push(Orderdetails)
//   }
//   res.send(orders.reverse());
// })




// router.post('/showprofile',async(req, res)=>{
//   const user= await User.findById(req.body.id).select('-password')
//   const professional_details = await Professional.findOne({"user._id":user._id});
//   const arr = []
//   arr.push(user.name)
//   arr.push(user.email)
//   arr.push(professional_details.locality)
//   arr.push(professional_details.profession)
//   arr.push(professional_details.phonenumber)
//   arr.push(user.profilepic)
//   res.send(arr);
// })

router.post('/',async(req, res)=>{
  // console.log(req.file);
  // console.log(req.body);
  console.log("entered");
  const {error} = validate(req.body);
  if(error) {
    const test="\"password\" with value "+"\""+req.body.password+"\""+" fails to match the required pattern: /^(?=.*[!@#$%^&*])/"
    if(error.details[0].message==test)
      return res.send({msg:"need a special character"})
    return res.send(error.details[0].message)
  }

  let user =await User.findOne({email:req.body.email});
  if (user && user.confirmed) return res.send({msg:'User already registered'})
  else if(user && !user.confirmed){
     await sendEmail(req.body.email, templates.confirm(user._id))
     return res.send({msg:msgs.resend})
    }
  user= new User({
    name: req.body.name,
    email: req.body.email,
    password: req.body.password,
  });
  const salt=await bcrypt.genSalt(10);
  user.password=await bcrypt.hash(user.password,salt)
  await user.save()
  let tem = await User.findOne({email:req.body.email})
  if(tem) await sendEmail(req.body.email, templates.confirm(tem._id))
  else console.log("failed")

  // User.findOne({ email })
  //   .then(user => {
  //
  //     // We have a new user! Send them a confirmation email.
  //     if (!user) {
  //       User.create({ name,email,password })
  //         .then(newUser => sendEmail(newUser.email, templates.confirm(newUser._id)))
  //         .then(() => res.json({ msg: msgs.confirm }))
  //         .catch(err => console.log(err))
  //     }
  //
  //     // We have already seen this email address. But the user has not
  //     // clicked on the confirmation link. Send another confirmation email.
  //     else if (user && !user.confirmed) {
  //       sendEmail(user.email, templates.confirm(user._id))
  //         .then(() => res.json({ msg: msgs.resend }))
  //     }
  //
  //     // The user has already confirmed this email address
  //     else {
  //       return res.status(400).send('User already registered')
  //     }
  //
  //   })
  //   .catch(err => console.log(err))
  /* can also be written as
  user= new User({
    name: req.body.name,
    email: req.body.email,
    password: req.body.password
  });
  */

  const token=user.generateAuthToken();

  userDetails={
    msg:"successful",
    user:user,
    token:token
  }

  res.send(userDetails)

});

router.post('/username',async(req,res)=>{
  let user = await User.findOne({email:req.body.email})
  if(user) return res.send("Valid username")
  return res.send("Invalid username")
});

router.get('/:id',async(req,res)=>{
  const user = await User.findById(req.params.id)
  if(!user) return res.send("no user")
  user.confirmed=true
  await user.save()
  return res.send("verified. You can now login")
});

module.exports = router;
