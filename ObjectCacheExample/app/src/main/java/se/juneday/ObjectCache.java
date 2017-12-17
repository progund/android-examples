package se.juneday;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Represents a cache for objects.
 * Uses Serializable and a file.
 *
 */
public class ObjectCache<T> {

  private List<T> objects;
  private long localCacheDate ;

  private String cacheFileName;
  private final static long maxDiff = (60 * 60 * 1000);

  /**
   * Creates a new ObjectCache instance.
   *
   * @param clazz A Class object. Used to name the cache file.
   */ 
  public ObjectCache(Class clazz) {
    if (! (clazz instanceof Serializable)) {
      throw new IllegalArgumentException("Class \"" + clazz.getName() + "\" does not implement Serializable");
    }
    localCacheDate = 0;
    cacheFileName = clazz.getSimpleName() + "_serialized.data";
  }

  /**
   * Creates a new ObjectCache instance.
   *
   * @param fileName A String used to name the cache file. The string
   * "_serialized.data" is added to the parameter.
   */ 
  public ObjectCache(String fileName) {
    localCacheDate = 0;
    cacheFileName = fileName + "_serialized.data";
  }

  /**
   * Sets a list of objects to cache (in RAM). This does not store the
   * objects to file.
   *
   * @param objects The objects to cache
   */ 
  public void set(List<T> objects) {
    this.objects = objects;
    localCacheDate = System.currentTimeMillis();
  }

  /**
   * Sets one object to cache (in RAM). This does not store the
   * object to file.
   *
   * @param object The object to cache
   */ 
  public void setSingle(T object) {
    ArrayList<T> objectList = new ArrayList<>();
    objectList.add(object);
    this.objects = objectList;
  }


  /**
   * Stores the list of cached (in RAM) objects to a file.
   *
   */ 
  public void push() {
    FileOutputStream fos = null;
    ObjectOutputStream out = null;
    try {
      fos = new FileOutputStream(cacheFileName);
      out = new ObjectOutputStream(fos);
      out.writeObject(objects);      
      out.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }



  /**
   * Retrieves (and returns) the list of stored objects from
   * file. These objects are now stored in RAM.
   *
   * @return The list of objects read from file.
   */ 
  @SuppressWarnings("unchecked")
  public List<T> pull() {
    File f = new File(cacheFileName);
    if (!f.exists()) {
      System.err.println("missing cache file: " + f);
      return null;
    }
    long diff =
      System.currentTimeMillis() - f.lastModified();

    if (diff>maxDiff) {
      System.err.println("cache timed out for objects");
      return null;
    }
    FileInputStream fis = null;
    ObjectInputStream in = null;
    List<T> tmpObjects;
    try {
      fis = new FileInputStream(cacheFileName);
      in = new ObjectInputStream(fis);
      tmpObjects = (List<T>) in.readObject();
      in.close();
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    objects = tmpObjects;
    return objects;
  }

  /**
   * Gets a list of cached objects (from RAM). This does not read the
   * objects from file.
   *
   * @return The list of cached objects 
   */ 
  public List<T> get() {
    return objects;
  }

  /**
   * Gets the cached object (from RAM). This does not read the object
   * from file.
   *
   * @return The cached object
   */ 
  public T getSingle() {
    if (objects == null) {
      return null;
    }
    return objects.get(0);
  }
}
