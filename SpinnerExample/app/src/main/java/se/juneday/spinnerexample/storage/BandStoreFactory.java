package se.juneday.spinnerexample.storage;

public class BandStoreFactory {

  public static BandStore getBandStore() {
    return new FakeBandStore();
  }

}
