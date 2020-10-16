# CS4518 Final Project MobileCalendar Readme

Team member: Xiaowei Chen, Fan Gong, Mingxi Liu, Jialin Song

## Project Description:
In this project, the team developed an Android based mobile application for students to add and display school courses on a calendar. This app involves a persistent database, which allows students to log into their accounts, add courses or delete courses based on term, and view them easily through the calendar page.

## To run the code:
- An Android mobile or an Android virtual device is required.  
- The external library is based on a lower version of SDK. When running the code, if the compiler hits the error:  * Module 'sample': platform 'android-29' not found. *
** Then please install the version of SDK indicated in the error message! **

## Evaluation tests:
- The JUnit test can be accessed from the /java folder with the same name as the project’s name. 
  - To run the test, you can just click the green arrow (run button) in each test class.

## Framwork used:
- CalendarView external library for the calendar page
- Sqlite database for persistent storage of data

## Features:
- User registration and login
- Course event add
- Course event delete
- Refresh
- Auto populate
