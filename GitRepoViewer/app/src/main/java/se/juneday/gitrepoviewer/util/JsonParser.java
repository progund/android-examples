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
