package com.training.jobifi.modelClass

data class UserHistoryModel(
    var orderId:String,
    var hisJobName:String?,
    var hisJobUser:String?,
    var hisTime:String?,
    var hisPay:String?,         /*User History Class*/
    var jobId:String,
    var status:String
)
