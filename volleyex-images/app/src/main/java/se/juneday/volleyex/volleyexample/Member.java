package se.juneday.volleyex.volleyexample;


public class Member {

  private String name;
  private String email;

  public Member(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String name() {
    return name;
  }

  public String email(){
    return email;
  }

  @Override
  public String toString() {
    return name + (email!=null?"<" + email +">":"");
  }

}
