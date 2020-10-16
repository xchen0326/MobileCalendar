package com.kizitonwose.mobilecalendar


class Converter{
        //list of Course to Stirng
        fun fromListCourse(loc: MutableList<Course>?): String?{
            var str = String()
            if (loc != null) {
                for(c in loc){
                    str += fromCourse(c)
                    str += '/'
                }
            }
            return str
        }

        //string to list of course
        fun toListCourse(course: String?): MutableList<Course>? {
            if(course != null){
                val list = course.split('/').map { it.trim() }
                var loc = mutableListOf<Course>()
                if(list.isEmpty()){
                    return null
                }
                for(i in list){
                    toCourse(i)?.let { loc.add(it) }
                }
                return loc
            }else{
                return null
            }
        }


        //Course to string
        fun fromCourse(course: Course?): String?{
            var rec = String()
            if(course?.getRecurrance() == null){
                rec = ";"
            }else {
                for (i in course.getRecurrance()) {
                    rec += i
                    if(i != course.getRecurrance().last()){
                        rec += ';'
                    }
                }
            }
            return course?.getCourseName()+','+course?.getLocation()+','+
                    course?.getStartTime()+','+course?.getEndTime()+','+ rec +
                    ','+ course?.getCourseTerm() + ','
        }

        //string to course
        fun toCourse(course: String?): Course?{
            if(course != null){
                val list = course.split(',').map { it.trim() }
                if (list.isEmpty() || list.size < 6){
                    return null
                }
                val sublist = list[4].split(';').map { it.trim() }.toMutableList()
                if (sublist.isEmpty()){
                    return null
                }
                val c = Course()
                c.setCourseName(list[0])
                c.setLocation(list[1])
                c.setStartTime(list[2])
                c.setEndTime(list[3])
                c.setRecurrance(sublist)
                c.setCourseTerm(list[5])
                return c
            }else{
                return null
            }
        }
}
