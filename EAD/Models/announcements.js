const Joi= require('joi');
const mongoose= require('mongoose');

const announcementSchema=new mongoose.Schema({
announcement:{
  type:String,
  default:"No announcement",
  required:true,
},
society_id:{
  type:String,
  required:true
},
date_created:{
  type:Date,
    default:Date.now
}
});

const Announcement= mongoose.model('Announcement',announcementSchema)


exports.Announcement = Announcement;

/* const token = req.header('x-auth-token')*/
