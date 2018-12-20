package se.juneday.memberimages.domain;


import java.io.Serializable;

public class Member implements Serializable {

    private String name;
    private String email;
    private String avatarUrl;

    private static final long serialVersionUID = 1L;

  /**
     * Constructs a new Member
     * @param name name of the member
     * @param email email for the member
     * @param avatarUrl url to an avatar, null if no avatar
     *
     * Throws a NullPointerException if any name of email is null
     */
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

    /**
     * Returns an objects name
     * @return the name of the object
     */
    public String name() {
        return name;
    }

    /**
     * Returns an objects avatar url
     * @return the avatar url of the object
     */
    public String avatarUrl() {
        return avatarUrl;
    }

    /**
     * Returns an objects email address
     * @return the email address of the object
     */
    public String email(){
        return email;
    }

    /**
     * Returns a String representation of the Member
     * @return a string representing the Member
     */
    @Override
    public String toString() {
        return name + (email!=null?"<" + email +">":"");
    }

}
