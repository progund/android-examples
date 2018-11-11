# The application

Present information about a github user’s repositories

* Look up information at github

* Present information in the Android application

# Technical description

Here's a rough overview of the application:
```
  +-------+                             +-------+
  |       |                             |       |
  |       |<----- JSON over http ------>|       |
  |       |                             |       |
  |       |                             |       |
  |       |                             |       |
  |       |                             |       |
  +-------+                             +-------+

 github.com                             Android
                                      application
```

## Application at a glance

The application should in short do:

* Fetch user data as JSON (over http)

* Parse JSON and create domain objects

* Present domain objects


# Application details

_Name_: 		Git Repo Viewer

_Package_: 	se.juneday.gitrepoviewer

_Repository information to display in application_:

* name

* private or public

* description

* license

## github.com

Github provides an API to retrieve information about the repositories users' keep there. 

# Problem - and dividing it

Perhaps too much at one time:

* Get data over network (JSON over http) using Volley

* Parse data (JSON format) into domain objects

* Present data

Android development:

* Takes time to upload and a bit cumbersome to test. Turn around time a bit too big


Let's start dividing the problem into five smaller ones

## Problem division (1) - parsing user data

## Problem division (2) - presenting user data

## Problem division (3) - parsing and presenting

## Problem division (4) - faked network with Volley

## Problem division (5) - integration