package se.juneday.lecturemouth.net;

import java.util.ArrayList;
import java.util.List;
import se.juneday.lecturemouth.domain.AudioButton;

public class Theme {

  String name;
  String url;
  List<AudioButton> audios;

  public Theme(String name, String url) {
    this.name = name;
    this.url = url;
    audios = new ArrayList<>();
  }

  public String name() {
    return name;
  }

  public String url() {
    return url;
  }

  public void addAudionButton(AudioButton ab) {
    audios.add(ab);
  }

  public void removeAudionButton(AudioButton ab) {
    audios.remove(ab);
  }

  public String toString() {
    return name + "<" + url + ">";
  }

}
