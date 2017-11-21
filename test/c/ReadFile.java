import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ReadFile {

  public static void main(String[] args) {
    int lineCount=1;
    Scanner sc=null;
    try {
      sc = new Scanner(new File(args[0]));
    } catch (FileNotFoundException e) { ; }
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      System.out.println(lineCount + ":" + line);
      lineCount++;
    }
  }
  
}
