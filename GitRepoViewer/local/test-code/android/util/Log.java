package android.util;

public class Log {

  public static void d(String tag, String s) {
    System.err.println("["+tag+"]: " + s);
  }

  public static void d(String tag, String s, Exception e) {
    System.err.println("["+tag+"]: " + s + " e: " + e.getMessage());
  }

  
}
