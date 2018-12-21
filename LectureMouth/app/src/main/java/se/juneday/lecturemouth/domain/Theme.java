package se.juneday.lecturemouth.domain;

import java.util.ArrayList;
import java.util.List;
import se.juneday.lecturemouth.domain.AudioClip;

public class Theme {

  String name;
  String url;
  List<AudioClip> audios;

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

  public void addAudionClip(AudioClip ab) {
    audios.add(ab);
  }

  public void removeAudionButton(AudioClip ab) {
    audios.remove(ab);
  }

  public List<AudioClip> audioClips() {
    return audios;
  }

  public String toString() {
    return name + "<" + url + ">";
  }

}
