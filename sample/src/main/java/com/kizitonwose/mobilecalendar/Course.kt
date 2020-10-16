package com.kizitonwose.mobilecalendar

class Course {
    private var cName: String=""
    private var loc: String=""
    private var sTime: String=""
    private var eTime: String=""
    private var recurrence: MutableList<String> = mutableListOf()
    private var term: String=""

    fun getCourseName():String{
        return cName
    }
    fun setCourseName(s:String){
        cName = s
    }
    fun getLocation():String{
        return loc
    }
    fun setLocation(s:String){
        loc = s
    }
    fun getStartTime():String{
        return sTime
    }
    fun setStartTime(s:String){
        sTime = s
    }
    fun getEndTime():String{
        return eTime
    }
    fun setEndTime(s:String){
        eTime = s
    }
    fun getRecurrance():MutableList<String>{
        return recurrence
    }
    fun setRecurrance(los:MutableList<String>){
        recurrence = los
    }
    fun getCourseTerm():String{
        return term
    }
    fun setCourseTerm(s:String){
        term = s
    }
}