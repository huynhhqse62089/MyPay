package com.example.dev00.myall.POJOs

import android.view.View

class ValidationRespond {
    var IsCancel: Boolean = false
    var FocusView: View? = null

    constructor(){
        this.IsCancel = false
        this.FocusView = null
    }

    constructor(IsCancel: Boolean, FocusView: View){
        this.IsCancel = IsCancel
        this.FocusView = FocusView
    }
}