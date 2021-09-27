package com.example.kotlintestapplication

import android.app.Application

class UserInfoData{
    companion object{
        private var NAME : String = "null"
        private var ID : String = "null"
        private var PW : String = "null"
        private var COMP : String = "null"

        fun setName(NAME : String){
            this.NAME = NAME
        }

        fun setID(ID : String){
            this.ID = ID
        }

        fun setPW(PW : String){
            this.PW = PW
        }

        fun setCOMP(COMP : String){
            this.COMP = COMP
        }

        fun getName() : String{
            return NAME
        }

        fun getID() : String{
            return ID
        }

        fun getPW() : String{
            return PW
        }

        fun getCOMP() : String{
            return COMP
        }





    }
}