package se.juneday.memberimages.domain;


public class Member {

    private String name;
    private String email;
    private String avatarUrl;

    public Member(String name, String email, String avatarUrl) {
        if ( name == null ){
            throw new NullPointerException("name cannot be null");
        }
        if ( email == null ){
            throw new NullPointerException("email cannot be null");
        }
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public String name() {
        return name;
    }

    public String avatarUrl() {
        return avatarUrl;
    }

    public String email(){
        return email;
    }

    @Override
    public String toString() {
        return name + (email!=null?"<" + email +">":"");
    }

}
