package com.dicoding.capstone.data.response

data class ClassesResponse(
	val status: String,
	val data: List<UserClassResponseData>
)

data class UserClassResponseData(
	val className: String,
	val subject: String,
	val classCode: String
)



