package se.juneday.spinnerexample.storage;

import java.util.ArrayList;
import java.util.List;
import se.juneday.spinnerexample.domain.Band;

public class FakeBandStore implements BandStore {

  private List<Band> bands;

  @Override
  public List<Band> bands() {
    if (bands==null) {
      bands = new ArrayList<>();
      bands.add(new Band("Black Flag"));
      bands.add(new Band("Dead Kenedys"));
      bands.add(new Band("Sonic Youth"));
      bands.add(new Band("Minor Threat"));
      bands.add(new Band("Butthole Surfers"));
      bands.add(new Band("Mudhoney"));
    }
    return bands;
  }
}
