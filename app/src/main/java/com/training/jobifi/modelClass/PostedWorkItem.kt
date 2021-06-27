package com.training.jobifi.modelClass

data class PostedWorkItem(
    var status: String,
    var pay: String,
    var time: String,
    var location: String,
    var bookedBy: String,                /*Data class to store job data of job posted by users*/
    var jobId: String,
    var bookerJobId: String,
    var bookerNumber: String,
    var jobName: String,
    var confirmed: String
)