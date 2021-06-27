package com.training.jobifi.modelClass

data class ProvideWorkItem(             /*Data class to hold jobs user posted*/
    var Book:String,
    var jobLocation:String,
    var jobName:String,
    var jobPay:String,
    var jobProvideName :String?,
    var jobGender:String,
    var jobAge:String,
    var jobTimings:String,
    var jobProvNumber:String?,
    var ageSlot:String,
    var shift:String,
    var id:String,
    var bookedBy:String,
    var bookerNumber:String,
    var bookerJobId:String,
    var confirmed:String
)