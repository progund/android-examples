package se.juneday.gitrepoviewer.util.test;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import se.juneday.ObjectCache;
import se.juneday.gitrepoviewer.domain.Repository;
import static se.juneday.gitrepoviewer.domain.Repository.RepoAccess;

import se.juneday.gitrepoviewer.util.RepositoryCache;


public class RepositoryCacheTest {

  private static String fileName = "repositories";


  
  public static void main(String[] args) {
    
    RepositoryCache cache;
    Context context = new Context();

    System.out.println("Deleting file: " + fileName);
    new File(fileName  + "_serialized.data" ).delete();

    System.out.print("Creating cache: ");
    cache = RepositoryCache.getInstance(context, fileName);
    assert (cache != null) : "Failed creating cache";
    System.out.println("OK");

    System.out.print("Reading cache: ");
    List<Repository> repos = cache.readObjectCache();
    assert (repos.size() == 0) : "Reading empty cache failed";
    System.out.println("OK");

    System.out.print("Creating cache: ");
    cache = RepositoryCache.getInstance(context, fileName);
    assert (cache != null) : "Failed creating cache";
    System.out.println("OK");

    System.out.print("Writing to cache: ");
    int reposToAdd = 1000;
    repos = new ArrayList<>();
    for (int i=0; i<reposToAdd ; i++) {
      repos.add(
                new Repository("faked-name-"+i, "bla bla bla"+i,
                               "GPLv3",
                               RepoAccess.PRIVATE));
    }
    cache.writeObjectCache(repos);
    System.out.println("OK");
    
    System.out.print("Creating cache: ");
    cache = RepositoryCache.getInstance(context, fileName);
    assert (cache != null) : "Failed creating cache";
    System.out.println("OK");

    System.out.print("Reading cache: ");
    repos = cache.readObjectCache();
    assert (repos.size() == reposToAdd) : "Reading cache with 1 element failed";
    System.out.println("OK repos size: " + repos.size());
  
  }

}
