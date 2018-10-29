package com.example.dev00.myall.models

import com.google.android.gms.common.api.GoogleApiClient

class GoogleApiClient_Singleton private constructor() {

    private var mGoogleApiClient: GoogleApiClient? = null

    companion object {
        private var m_instance: GoogleApiClient_Singleton = GoogleApiClient_Singleton()
        @Synchronized
        fun getInstance(): GoogleApiClient_Singleton {
            return m_instance
        }
    }

    fun setGoogleApiClient(mGoogleApiClient: GoogleApiClient){
        this.mGoogleApiClient = mGoogleApiClient
    }

    fun getGoogleApiClient(): GoogleApiClient?{
        return this.mGoogleApiClient
    }
}