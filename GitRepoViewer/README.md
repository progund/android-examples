# Lecture: (http://wiki.juneday.se/mediawiki/index.php/Android_-_client_strategy)


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

__Name__: 		Git Repo Viewer

__Package__: 	se.juneday.gitrepoviewer

__Repository information to display__:

* name

* private or public

* description

* license

