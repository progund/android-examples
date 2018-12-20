package se.juneday.lecturemouth.domain;

public class AudioButton {

    private String text;
    private String path;

    public AudioButton(String text, String path) {
        this.text = text;
        this.path = path;
    }

    public String text() {
        return text;
    }

    public String path() {
        return path;
    }

    public String toString() {
        return text + "<" + path + ">";
    }

}
