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

## github.com

Github provides an API to retrieve information about the repositories users' keep there. 

# Problem - and dividing it

## To many things

Perhaps we will be doing too much at one time. It's hard to know exactly what fails if we're writting all the code at once. Let's look at what parts we have:

* Get data over network (JSON over http) using Volley

* Parse data (JSON format) into domain objects

* Present data

## Slow turn around time

When developing for Android things take longer time than what you've been used to so far in our courses. When developing Java programs it is usually only:

* compilation

* launch program

and you can test again but in Android you need to:

* compile

* convert the java bytecode into DEX

* uninstall the previous version on the device (emulated or physical)

* transfer the new package to the device

* install the new package

* finally lauuch the application

This takes time so it is a bit cumbersome to develop and test. We need to do as much as possible to speed things up.

Let's start dividing the problem into five smaller ones

## Problem division (1) - parsing user data

Using the same library (__org.json__) as in Android we could develop
and test the code that parses the JSON data locally. During this phase
we will develop the so domain objects (e g the __Repository__ class
representing a repository and the information we would like to
present).

Instead of retrieving data from from github using Java code we can put some JSON data in a String and parse that instead. Doing this we can focus entirely on parsing and not worry about network transfer. 

### Preparing a string with JSON data

#### Analysing the use data
Let's look at the data from github. We can do this by either using a browser (i e Firefox or Chrome) or by using a command line tool such as wget and curl __TODO:LINK THEM TOOLS__.

First of all, download the JSON file with information about the repositories:
```
$ curl -o progund-repos.json "https://api.github.com/orgs/progund/repos?per_page=400"
```

How many lines do we have in this file? We can use wc to do the counting: 
```
$ wc -l progund-repos.json
8236 progund-repos.json
```
Uh oh, 8236 lines of JSON data. Seems as if there are tons of repositories in the JSON file. Can we keep just two repos? Let's look at parts of the file first:
```
[
  {
    "id": 24987853,
    "node_id": "MDEwOlJlcG9zaXRvcnkyNDk4Nzg1Mw==",
    "name": "timelapse",
    "full_name": "progund/timelapse",
    "private": false,
    "owner": {
      "login": "progund",
      "id": 19474334,
      "node_id": "MDEyOk9yZ2FuaXphdGlvbjE5NDc0MzM0",
      "avatar_url": "https://avatars0.githubusercontent.com/u/19474334?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/progund",
      "html_url": "https://github.com/progund",
      "followers_url": "https://api.github.com/users/progund/followers",
      "following_url": "https://api.github.com/users/progund/following{/other_user}",
      "gists_url": "https://api.github.com/users/progund/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/progund/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/progund/subscriptions",
      "organizations_url": "https://api.github.com/users/progund/orgs",
      "repos_url": "https://api.github.com/users/progund/repos",
      "events_url": "https://api.github.com/users/progund/events{/privacy}",
      "received_events_url": "https://api.github.com/users/progund/received_events",
      "type": "Organization",
      "site_admin": false
    },
    "html_url": "https://github.com/progund/timelapse",
    "description": "Timelapse scripts (bash) for Raspberry PI",
    "fork": false,
    "url": "https://api.github.com/repos/progund/timelapse",
    "forks_url": "https://api.github.com/repos/progund/timelapse/forks",
    "keys_url": "https://api.github.com/repos/progund/timelapse/keys{/key_id}",
    "collaborators_url": "https://api.github.com/repos/progund/timelapse/collaborators{/collaborator}",
    "teams_url": "https://api.github.com/repos/progund/timelapse/teams",
    "hooks_url": "https://api.github.com/repos/progund/timelapse/hooks",
    "issue_events_url": "https://api.github.com/repos/progund/timelapse/issues/events{/number}",
    "events_url": "https://api.github.com/repos/progund/timelapse/events",
    "assignees_url": "https://api.github.com/repos/progund/timelapse/assignees{/user}",
    "branches_url": "https://api.github.com/repos/progund/timelapse/branches{/branch}",
    "tags_url": "https://api.github.com/repos/progund/timelapse/tags",
    .....
        "archived": false,
    "open_issues_count": 0,
    "license": {
      "key": "gpl-3.0",
      "name": "GNU General Public License v3.0",
      "spdx_id": "GPL-3.0",
      "url": "https://api.github.com/licenses/gpl-3.0",
      "node_id": "MDc6TGljZW5zZTk="
    },
    "forks": 0,
    "open_issues": 0,
    "watchers": 0,
    "default_branch": "master",
    "permissions": {
      "admin": false,
      "push": false,
      "pull": true
    }
  },
  {
  .....
```
From the above we can deduce that:

* the repositories come in an array

* each array element (a repo) contain

** name

** private

** description

** license (element with name of license)

#### Creating a string with JSON data

We can either delete the elements after the two first or we can use tools to do this. Let's go for the second (a) it's easier and b) we get to learn a new tool). We're going to use ```jq```. Extracting the first element from the JSON file is done like this:

```
jq '.[0]' progund-repos.json
```

If we put the following in a file:

* ```[```

* the first element (```jq '.[0]' progund-repos.json```)

* ```,```

* the second element (```jq '.[1]' progund-repos.json```)

* ```]```

We can do the above like this:
```
 echo "[" > tmp.json
 cat progund-repos.json | jq '.[0]' >> tmp.json
 echo "," >> tmp.json
 cat progund-repos.json | jq '.[1]' >> tmp.json
 echo "]" >> tmp.json
```

Now we have a file called ```tmp.json``` containing the first two
elements as retrieved from github. This will do fine to test with.

But hold your horses... if we copy the content in this file to use in
a string it will not work due to fact that both Java and JSON use
```"```. So we need to escape the ```"``` to ```\"``` and also remove
the newlines before we can copy/paste the value:

We can do the above like this:
```
 cat tmp.json| sed -e 's,\",\\\",g' | tr '\n' ' '> string.txt
```

The file ```string.txt``` now contains data that you can put in a String and pass that String to your parser method. The content of the file looks like this:

```
[ {   \"id\": 24987853,   \"node_id\": \"MDEwOlJlcG9zaXRvcnkyNDk4Nzg1Mw==\",   \"name\": \"timelapse\",   \"full_name\": \"progund/timelapse\",   \"private\": false,   \"owner\": {     \"login\": \"progund\",     \"id\": 19474334,     \"node_id\": \"MDEyOk9yZ2FuaXphdGlvbjE5NDc0MzM0\",     \"avatar_url\": \"https://avatars0.githubusercontent.com/u/19474334?v=4\",     \"gravatar_id\": \"\",     \"url\": \"https://api.github.com/users/progund\",     \"html_url\": \"https://github.com/progund\",     \"followers_url\": \"https://api.github.com/users/progund/followers\",     \"following_url\": \"https://api.github.com/users/progund/following{/other_user}\",     \"gists_url\": \"https://api.github.com/users/progund/gists{/gist_id}\",    
```

Look at (and use) the script ```fetch_repositories.sh``` if you want to automate the above.

### Source code

Let's use the package as we should use later on in the application. This means that we need a directory structure like this:
```
.
`-- se
    `-- juneday
        `-- gitrepoviewer
            |-- domain
            |   |-- Repository.java
            `-- util
                |-- JsonParser.java
```

We will use this code in Android later on so we're going to keep our test code away from the above code. Let's put the test code in a separate folder:

```
test-code/
`-- se
    `-- juneday
        `-- gitrepoviewer
            |-- domain
            |   `-- test
            |       |-- RepositoryTest.java
            `-- util
                `-- test
                    `-- JsonParserTest.java
```

#### Repository.java

```
package se.juneday.gitrepoviewer.domain;

public class Repository {

  /*
   *
   * enum for repository states (private/public)
   *
   */
  public enum RepoAccess {
    PRIVATE("private"),
    PUBLIC("public");
    private String accessName;
    RepoAccess(String name) {
      this.accessName = name;
    }
    public String accessName() {
      return this.accessName;
    }
  }
  
  private String name;
  private String description;
  private String license;
  private RepoAccess access;

  public Repository(String name,    String description,
                    String license, RepoAccess access) {
    this.name = name;
    this.access = access;
    this.description = description;
    this.license = license;
  }

  public String name() {
    return name;
  }

  public String description() {
    return description;
  }

  public String license() {
    return license;
  }
  
  public RepoAccess access() {
    return access;
  }
  
  @Override
  public String toString() {
    return new StringBuilder(name)
      .append(" - ")
      .append(description)
      .append(" (")
      .append(license)
      .append(", ")
      .append(access.accessName())
      .append(")")
      .toString();
  }
  
}
```

#### JsonParser.java
```
package se.juneday.gitrepoviewer.util;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import se.juneday.gitrepoviewer.domain.Repository;
import static se.juneday.gitrepoviewer.domain.Repository.RepoAccess;

public class JsonParser {

  public static List<Repository> parse(String json) {
    
    List<Repository> repos = new ArrayList<>();
    JSONArray jsonArray = null;

    try {
      jsonArray = new JSONArray(json);
    }  catch ( JSONException e ) {
      return repos;
    }

    for(int i = 0; i < jsonArray.length(); i++) {

      JSONObject jsonObject = null;

      try {
        jsonObject = jsonArray.getJSONObject(i);
      } catch ( JSONException e ) {
        continue;
      }

      //default values
      String name = null;
      String description = ""; 
      RepoAccess repoAccess = RepoAccess.PUBLIC;
      String license = ""; 

      try {
        name = jsonObject.getString("name");
      } catch ( JSONException e ) {
        continue;
      }      

      // extract description (if any)
      try {
        description = jsonObject.getString("description");
      } catch ( JSONException e ) {
        //System.err.println("warning: " + e.getMessage());
      }

      // extract repo access (private/public)
      try {
        boolean privateRepo = jsonObject.getBoolean("private");
        if (privateRepo) {
          repoAccess = RepoAccess.PRIVATE;
        }
      } catch ( JSONException e ) {
        continue;
      }      


      // extract license (if any)
      try {
        JSONObject licenseObject = jsonObject.getJSONObject("license");
        license = licenseObject.getString("name");
      } catch ( JSONException e ) {
        //System.err.println("warning: " + e.getMessage());
      }

      repos.add(new Repository(name,
                               description,
                               license,
                               repoAccess));
    }
    return repos;
  }

 
}
```

#### RepositoryTest.java

```
package se.juneday.gitrepoviewer.domain.test;

import java.util.List;

import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.util.JsonParser;
import static se.juneday.gitrepoviewer.domain.Repository.RepoAccess;

public class RepositoryTest {

  public static void main(String[] args) {

    System.out.print("Creating repository:");
    Repository repo =
      new Repository("faked-name", "bla bla bla", "GPLv3", RepoAccess.PRIVATE);
    System.out.println("OK");

    System.out.println("Checking repository:");
    System.out.print(" * name: ");
    assert (repo.name().equals("faked-name")) : "Fail, name differs";
    System.out.println("OK");

    System.out.print(" * description: ");
    assert (repo.description().equals("bla bla bla")) : "Fail, description differs";
    System.out.println("OK");

    System.out.print(" * license: ");
    assert (repo.license().equals("GPLv3")) : "Fail, license differs";
    System.out.println("OK");

    System.out.print(" * access: ");
    assert (repo.access().equals("private")) : "Access differs";
    System.out.println("OK");

    
    System.out.println("RepositoryTest succeeded :)");
  }

  
}
```
#### JsonParserTest

```
package se.juneday.gitrepoviewer.util.test;

import java.util.List;

import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.util.JsonParser;
import static se.juneday.gitrepoviewer.domain.Repository.RepoAccess;

public class JsonParserTest {

  public static void main(String[] args) {

    System.out.print("Parsing JSON String: ");
    List<Repository> repos = JsonParser.parse(jsonData);
    System.out.println("OK");

    System.out.print("Check nr of elements: ");
    assert (repos.size()==2) : "Fail, number of elements (" + repos.size() + ") not 2";
    System.out.println("OK");

    System.out.println("Check repositories: ");
    for (Repository r : repos) {
      System.out.print(" * check name: ");
      assert ( r.name() != null && r.name().length() > 0 ) : "Fail, name null or empty";
      System.out.println("OK");
      
      System.out.print(" * check access : ");
      assert ( r.access() != null
               &&
               ( r.access() == RepoAccess.PRIVATE ||
                 r.access() == RepoAccess.PUBLIC)) : "Fail, access null or faulty";
      System.out.println("OK");
      
      System.out.print(" * check description not null: ");
      assert ( r.description() != null ) : "Fail, description null";
      System.out.println("OK");
      
      System.out.print(" * check license not null: ");
      assert ( r.license() != null ) : "Fail, license null";
      System.out.println("OK");
      
    }
    
    System.out.println("JsonParserTest succeeded :)");
  }


  private static String jsonData = ""; // NOTE: this string must be replaced with the content in the file string.txt
  
}
```

### Compile and test

You can use the script ```test.sh``` which will:

* download the JSON jar if needed

* compile android code

* compile test code

* execute tests

Or you can do the following manually:

Download JSON jar file:
```
$ wget 'https://search.maven.org/remotecontent?filepath=org/json/json/20171018/json-20171018.jar' -O org.json.jar
```

Compile all android files:
```
$ javac -cp org.json.jar se/juneday/gitrepoviewer/domain/Repository.java  se/juneday/gitrepoviewer/util/JsonParser.java 
```

Compile and execute RespositoryTest:
```
$ javac -cp org.json.jar:. test-code/se/juneday/gitrepoviewer/domain/test/RepositoryTest.java
$ java -cp org.json.jar:test-code:. se.juneday.gitrepoviewer.domain.test.RepositoryTest
Creating repository:OK
Checking repository:
 * name: OK
 * description: OK
 * license: OK
 * access: OK
RepositoryTest succeeded :)
```

Compile and execute JsonParserTest:
```
$ javac -cp org.json.jar:. test-code/se/juneday/gitrepoviewer/util/test/JsonParserTest.java 
$ java -cp org.json.jar:test-code:. se.juneday.gitrepoviewer.util.test.JsonParserTest
Parsing JSON String: OK
Check nr of elements: OK
Check repositories: 
 * check name: OK
 * check access : OK
 * check description not null: OK
 * check license not null: OK
 * check name: OK
 * check access : OK
 * check description not null: OK
 * check license not null: OK
JsonParserTest succeeded :)
```

### Conclusion

Even if it may feel cumbersome and not worth the while dividing the
problem into smaller pieces if you had to use tools such as jq, sed,
bash .... But you have to take our waords for it. It is worth it. It
may be tough to grasp the whole thing but having a parser that you can
test standalone (no android device) is a great thing. Imagine if
github changes the structure of the JSON data? it will be a pieace of
cake to rewrite with having to upload and install the app to a device
each time you want to test a small change.

We have a JSONParser and a domain object. Nice, let's proceed with the presentation in the Android app.

## Problem division (2) - presenting user data

Presenting the repository information really has nothing to do with retrieving and parsing the data so let's keep it that way. Let's create some fake data (typically put some Repository objects in a List) and present these data.

### Source code

#### Repository.java

Move the file ```Repository.java``` to the Android directory
structure. No, don't keep a copy for your self - move the file! And
move (do not copy) the JSON parser file as well.

#### MainActivity.java
```
package se.juneday.gitrepoviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.domain.Repository.RepoAccess;

public class MainActivity extends AppCompatActivity {

  private ArrayAdapter<Repository> adapter;
  private List<Repository> repos = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  private void populateWithFakedData() {
    repos.add(new Repository("dummy", "faked repo for test", "GPLv3", RepoAccess.PRIVATE));
    repos.add(new Repository("dummy again", "faked repo for test2", "GPLv3", RepoAccess.PUBLIC));
  }

  @Override
  public void onStart() {
    super.onStart();

    // fake data
    populateWithFakedData();

    // Lookup ListView
    ListView listView = (ListView) findViewById(R.id.repos_list);

    // Create Adapter
    adapter = new ArrayAdapter<Repository>(this,
        android.R.layout.simple_list_item_1,
        repos);

    // Set listView's adapter to the new adapter
    listView.setAdapter(adapter);
  }

}
```

The method ```populateWithFakedData``` adds faked repositories to the list of repositories. 

#### main_activity.xml

We're using a ```LinearLayout``` since we're only having one element in the entire Activity. In this list we're adding a ```ListView``` which we will use th present the repositories one by one in a scrollable list. If you want to read more about ListView, check out our page: __Android:ListView__ TODO-LINK.

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  xmlns:tools="http://schemas.android.com/tools"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  tools:context="se.juneday.gitrepoviewer.MainActivity">

  <ListView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/repos_list">
  </ListView>

</LinearLayout>
```

## Problem division (3) - parsing and presenting

We have a user interface that can present repository data - at least faked data from a list. Remeber the JSON string we used earlier when testing the JSON parser. Let's use that string and the JSON parser and present the resulting list.

### Source code

#### MainActivity.java
```
package se.juneday.gitrepoviewer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.domain.Repository.RepoAccess;
import se.juneday.gitrepoviewer.network.VolleyRepositoryFetcher;
import se.juneday.gitrepoviewer.network.VolleyRepositoryFetcher.RepositioryChangeListener;
import se.juneday.gitrepoviewer.util.JsonParser;

public class MainActivity extends AppCompatActivity {

  private static final String LOG_TAG = MainActivity.class.getName();
  private ArrayAdapter<Repository> adapter;
  private List<Repository> repos = new ArrayList<>();

  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }


  private void populateWithFakedData() {
    repos = JsonParser.parse(jsonData);
    Log.d(LOG_TAG, " repos: " + repos.size());
  //    repos.add(new Repository("dummy", "faked repo for test", "GPLv3", RepoAccess.PRIVATE));
  //  repos.add(new Repository("dummy again", "faked repo for test2", "GPLv3", RepoAccess.PUBLIC));
  }


  private void showToast(String msg) {
    Log.d(LOG_TAG, " showToast: " + msg);
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(this, msg, duration);
    toast.show();
  }

  private void resetListView(List<Repository> repos) {
    listView = (ListView) findViewById(R.id.repos_list);
    adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, repos);
    listView.setAdapter(adapter);
  }

  @Override
  public void onStart() {
    super.onStart();

    // fake data
    populateWithFakedData();

    // Lookup ListView
    listView = (ListView) findViewById(R.id.repos_list);

    // Create Adapter
    adapter = new ArrayAdapter<Repository>(this,
        android.R.layout.simple_list_item_1,
        repos);

    // Set listView's adapter to the new adapter
    listView.setAdapter(adapter);

  }

  private static String jsonData = ""; // copy the content of the file string.txt to this string

}
```

We've added some ```Log``` calls to make it possible to check the progress in the ```Logcat``` window in Android Studio.

#### JsonParser.java
When compiling the JSON parser you will see a lot of compilation errors. This comes from the fact that Android have changed the Exception approach _a bit_. Let's look at the inheritance structure of ```JSONException``` at both ```org.json``` and  ```android``` :

__https://stleary.github.io/JSON-java/__
```
java.lang.Object
  java.lang.Throwable
    java.lang.Exception
      java.lang.RuntimeException
        org.json.JSONException
```

__https://developer.android.com/reference/org/json/JSONException__
```
java.lang.Object
  java.lang.Throwable
    java.lang.Exception
      org.json.JSONException
```
So in Android ```JSONException``` is a checked exception. We now need to rewrite the JSON parser a bit.


```
package se.juneday.gitrepoviewer.util;

import android.util.Log;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import se.juneday.gitrepoviewer.domain.Repository;
import static se.juneday.gitrepoviewer.domain.Repository.RepoAccess;

public class JsonParser {

  private static final String LOG_TAG = JsonParser.class.getName();

  public static List<Repository> parse(JSONArray jsonArray) {
    List<Repository> repos = new ArrayList<>();

    for(int i = 0; i < jsonArray.length(); i++) {
      Log.d(LOG_TAG, "parse() for: i: " + i);

      JSONObject jsonObject = null;

      try {
        jsonObject = jsonArray.getJSONObject(i);
      } catch ( JSONException e ) {
        continue;
      }

      //default values
      String name = null;
      String description = "";
      RepoAccess repoAccess = RepoAccess.PUBLIC;
      String license = "";

      try {
        name = jsonObject.getString("name");
      } catch ( JSONException e ) {
        continue;
      }

      // extract description (if any)
      try {
        description = jsonObject.getString("description");
      } catch ( JSONException e ) {
        //System.err.println("warning: " + e.getMessage());
      }

      // extract repo access (private/public)
      try {
        boolean privateRepo = jsonObject.getBoolean("private");
        if (privateRepo) {
          repoAccess = RepoAccess.PRIVATE;
        }
      } catch ( JSONException e ) {
        continue;
      }


      // extract license (if any)
      try {
        JSONObject licenseObject = jsonObject.getJSONObject("license");
        license = licenseObject.getString("name");
      } catch ( JSONException e ) {
        //System.err.println("warning: " + e.getMessage());
      }

      repos.add(new Repository(name,
          description,
          license,
          repoAccess));
    }
    return repos;
  }

  public static List<Repository> parse(String json) {
    Log.d(LOG_TAG, "parse()");

    JSONArray jsonArray = null;

    try {
      jsonArray = new JSONArray(json);
    }  catch ( JSONException e ) {
      Log.d(LOG_TAG, "can not create array. " + e);
      return new ArrayList<>();
    }

    return parse(jsonArray);
  }

 
}
```

We've added some ```Log``` calls to make it possible to check the progress in the ```Logcat``` window in Android Studio.

### Making sure things work locally (not on a device)

Let's make sure everything works locally to make sure we can rewrite
and test our JSON parser locally. Since we have moved the files a bit
we will end up with different command lines:

Compile the android files:
```
$ javac -cp org.json.jar ../app/src/main/java/se/juneday/gitrepoviewer/domain/Repository.java ../app/src/main/java/se/juneday/gitrepoviewer/util/JsonParser.java 
../app/src/main/java/se/juneday/gitrepoviewer/util/JsonParser.java:3: error: package android.util does not exist
import android.util.Log;
                   ^
../app/src/main/java/se/juneday/gitrepoviewer/util/JsonParser.java:22: error: cannot find symbol
      Log.d(LOG_TAG, "parse() for: i: " + i);
      ^
  symbol:   variable Log
  location: class JsonParser
../app/src/main/java/se/juneday/gitrepoviewer/util/JsonParser.java:79: error: cannot find symbol
    Log.d(LOG_TAG, "parse()");
    ^
  symbol:   variable Log
  location: class JsonParser
../app/src/main/java/se/juneday/gitrepoviewer/util/JsonParser.java:86: error: cannot find symbol
      Log.d(LOG_TAG, "can not create array. " + e);
      ^
  symbol:   variable Log
  location: class JsonParser
4 errors
```

Uh oh, there's no ```Log``` class here. Should we scrap the idea of making it possible to test locally. No. Let's create a simplified version of the Log class. How many different calls the Log do we do? One! Couldn't be that hard?

### Create a stub class

Let's create a class ```test-code/android/util/Log.java``` like this:

__test-code/android/util/Log.java__
```
package android.util;

public class Log {

  public static void d(String tag, String s) {
    System.err.println("["+tag+"]: " + s);
  }

}
```

Creating a class like this, with the same name as another class and a
new implementation of the API, is called writing a stub. This is
useful if you want to shortcut a big API and replace that with your
own version. 

### Use the stub class and compile again


Let us try to compile again (and adding test-code to the classpath):
```
$ javac -cp org.json.jar:test-code ../app/src/main/java/se/juneday/gitrepoviewer/domain/Repository.java ../app/src/main/java/se/juneday/gitrepoviewer/util/JsonParser.java
```
and let's compile and execute the tests:
```
$ javac -cp org.json.jar:test-code:../app/src/main/java/ test-code/se/juneday/gitrepoviewer/util/test/JsonParserTest.java 
$ java -cp org.json.jar:test-code:../app/src/main/java/ se.juneday.gitrepoviewer.util.test.JsonParserTest
Parsing JSON String: [se.juneday.gitrepoviewer.util.JsonParser]: parse()
[se.juneday.gitrepoviewer.util.JsonParser]: parse() for: i: 0
[se.juneday.gitrepoviewer.util.JsonParser]: parse() for: i: 1
OK
Check nr of elements: OK
Check repositories: 
 * check name: OK
 * check access : OK
 * check description not null: OK
 * check license not null: OK
 * check name: OK
 * check access : OK
 * check description not null: OK
 * check license not null: OK
JsonParserTest succeeded :)
```

Ok, great we have a parser that can be used in Android and locally (without a device).

## Problem division (4) - faked network with Volley

We're going to use the Volley approach we call __Volley code outside the Activity using Observer__. Check out our page on how this works: http://wiki.juneday.se/mediawiki/index.php/Android:Network#Volley_code_outside_the_Activity_using_Observer __TODO LINK LINK__.

__VolleyRepositoryFetcher.java__
```
package se.juneday.gitrepoviewer.network;


import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.util.JsonParser;

public class VolleyRepositoryFetcher {

  private static final String LOG_TAG = VolleyRepositoryFetcher.class.getName();
  private static VolleyRepositoryFetcher fetcher;
  private Context context;
  private List<RepositioryChangeListener> listeners;


  public static synchronized VolleyRepositoryFetcher getInstance(Context context) {
    if (fetcher == null) {
      fetcher = new VolleyRepositoryFetcher(context);
    }
    return fetcher;
  }

  private VolleyRepositoryFetcher(Context context) {
    listeners = new ArrayList<>();
    this.context = context;
  }


  public void getRepositories() {
    RequestQueue queue = Volley.newRequestQueue(context);

    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        "https://api.github.com/orgs/progund/repos?per_page=400",
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            Log.d(LOG_TAG, " got data from Volley");
            List<Repository> repos = JsonParser.parse(array);
            for (RepositioryChangeListener r : listeners) {
              r.onRepositoryChange(repos);
            }
          }
        }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
      }

    });

    // Add the request to the RequestQueue.
    queue.add(jsonArrayRequest);
  }


  /******************************************
   RepositioryChangeListener
   ******************************************/


  public interface RepositioryChangeListener {

    void onRepositoryChange(List<Repository> repos);
  }

  public void addRepositoryChangeListener(RepositioryChangeListener l) {
    listeners.add(l);

  }

}
```

__MainActivity.java__
```
package se.juneday.gitrepoviewer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.domain.Repository.RepoAccess;
import se.juneday.gitrepoviewer.network.VolleyRepositoryFetcher;
import se.juneday.gitrepoviewer.network.VolleyRepositoryFetcher.RepositioryChangeListener;
import se.juneday.gitrepoviewer.util.JsonParser;

public class MainActivity extends AppCompatActivity {

  private static final String LOG_TAG = MainActivity.class.getName();
  private ArrayAdapter<Repository> adapter;
  private List<Repository> repos ;

  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    repos = new ArrayList<>();
  }


  /*
  private void populateWithFakedData() {
    repos = JsonParser.parse(jsonData);
    Log.d(LOG_TAG, " repos: " + repos.size());
  //    repos.add(new Repository("dummy", "faked repo for test", "GPLv3", RepoAccess.PRIVATE));
  //  repos.add(new Repository("dummy again", "faked repo for test2", "GPLv3", RepoAccess.PUBLIC));
  }
  */

  private void showToast(String msg) {
    Log.d(LOG_TAG, " showToast: " + msg);
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(this, msg, duration);
    toast.show();
  }

  private void resetListView(List<Repository> repos) {
    this.repos = repos;
    listView = (ListView) findViewById(R.id.repos_list);
    adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, repos);
    listView.setAdapter(adapter);
    showToast("Displaying " + repos.size() + " repositories" );
  }

  @Override
  public void onStart() {
    super.onStart();

    // fake data
    /* populateWithFakedData();
     */

   // register to listen to member updates in VolleyMember
   VolleyRepositoryFetcher.getInstance(this).addRepositoryChangeListener(new RepositioryChangeListener() {
      @Override
      public void onRepositoryChange(List<Repository> repos) {
        resetListView(repos);
      }
    });


    // Lookup ListView
    listView = (ListView) findViewById(R.id.repos_list);

    // Create Adapter
    adapter = new ArrayAdapter<Repository>(this,
        android.R.layout.simple_list_item_1,
        repos);

    // Set listView's adapter to the new adapter
    listView.setAdapter(adapter);


    // Let's get all the repos and await an async "callback" to onRepositoryChange (above)
    VolleyRepositoryFetcher.getInstance(this).getRepositories();

  }

  /*
  private static String jsonData = ""; // not needed 
  */

}
```

## Problem division (5) - integration

Fact is, we already have integrated all the code. Nothing more to do. But before we close this page we will go through what we have done:

* We started out by writing classes for our domain objects
(repositories) and a JSON parser. We developed and tested this locally
(no android device).

* After that we created a GUI that displayed a list of Repository objects.

* We then proceeded with parsing prepared JSON data and displayed the
resulting list of Repositories.

* Having all these pieces in place we continued with actually retrieving
the JSON data from github and presented that.