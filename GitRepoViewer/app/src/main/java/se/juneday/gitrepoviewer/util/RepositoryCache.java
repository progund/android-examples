package se.juneday.gitrepoviewer.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import se.juneday.ObjectCache;
import se.juneday.gitrepoviewer.domain.Repository;

public class RepositoryCache {

  Context context;
  private ObjectCache<Repository> cache;
  private static RepositoryCache instance;
  private String className;
  private String LOG_TAG = RepositoryCache.class.getName();

  private RepositoryCache() {};

  public static RepositoryCache getInstance(Context context,String className) {
    if (instance==null) {
      instance = new RepositoryCache(context, className);
    }
    return instance;
  }

  private RepositoryCache (Context context, String className) {
    this.context = context;
    this.className = className;
  }

  private void initObjectCache() {
    // Create an ObjectCache instance - pass the Repository class as a
    // parameter (used to set the name of the file to write/cache to).
    PackageManager m = context.getPackageManager();
    String s = context.getPackageName();
    try {
      PackageInfo p = m.getPackageInfo(s, 0);
      s = p.applicationInfo.dataDir;
    } catch (PackageManager.NameNotFoundException e) {
      Log.d(LOG_TAG, "Error, could not build file name for serialization", e);
      return;
    }
    String fileName = s +
        "/" + className;

    cache = new ObjectCache<>(fileName);
  }

  public List<Repository> readObjectCache() {
    if (cache==null) {
      initObjectCache();
    }
    cache.pull();

    List<Repository> cachedRepos = cache.get();
    Log.d(LOG_TAG, "cached: " + ((cachedRepos==null)?0:cachedRepos.size()));
    if (cachedRepos!=null) {
      //      Log.d(LOG_TAG, " - using cache");
      return cachedRepos;
    }
    //Log.d(LOG_TAG, " - no cache, creating empty list");
    return new ArrayList<>();
  }

  public  void writeObjectCache(List<Repository> repos) {
    if (cache==null) {
      initObjectCache();
    }
    cache.set(repos);
    cache.push();
  }


}
