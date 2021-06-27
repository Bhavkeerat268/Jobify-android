package com.training.jobifi.modelClass

data class RecommendedJobs(             /*Data class ot hold recommended jobs data */
    val job:String,
    val JobUserName:String,
    val location:String,
    val id:String,
    var number:String,
    var book:String
)