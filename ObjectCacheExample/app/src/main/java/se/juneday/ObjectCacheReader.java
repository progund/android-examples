package se.juneday;

import java.util.List;
import java.io.File;

public class ObjectCacheReader<T> {

  ObjectCache<T> cache;

  public ObjectCacheReader(String fileName) {
    cache = new ObjectCache<>(fileName);
  }

  public void printObjects() {
    List<T> objects = cache.pull();

    if (objects!=null) {
      for (T o : objects) {
        System.out.println(" * " + o );
      }
    }
  }

  public static void main(String args[]) {
    if (args.length==0) {
      System.out.println("Missing argument (filename)");
      System.exit(1);
    }
    
    ObjectCacheReader<Object> ocr = new ObjectCacheReader<>(args[0]);
    ocr.printObjects();
  }
  
}
