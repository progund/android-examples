package se.juneday.gitrepoviewer.domain;

import java.io.Serializable;

public class Repository implements Serializable {

  private static final long serialVersionUID = 1L;


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
