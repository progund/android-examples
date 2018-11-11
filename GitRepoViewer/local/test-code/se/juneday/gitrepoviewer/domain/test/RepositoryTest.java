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
    assert (repo.access().toString().equals("PRIVATE")) : "Access differs " ;
    System.out.println("OK");

    
    System.out.println("RepositoryTest succeeded :)");
  }

  
}
