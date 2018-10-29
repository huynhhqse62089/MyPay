package com.example.dev00.myall.models

import java.io.Serializable

data class User(var Id: Int = 0,
                var Email: String = "",
                var PhoneNumber: String = "",
                var Password: String = "",
                var Type: Int = 0)
    : Serializable {

}

