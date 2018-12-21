package se.juneday.lecturemouth.domain;

public class AudioButton {

    private String text;
    private String path;
    private String url;

  public AudioButton(String text, String path, String url) {
    this.text = text;
    this.path = path;
    this.url = url;
  }

  public String text() {
        return text;
    }

    public String url() {
        return url;
    }

    public String path() {
        return path;
    }

    public String toString() {
        return text + "<" + path + ">";
    }

}
