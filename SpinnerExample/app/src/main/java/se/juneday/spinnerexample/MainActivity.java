package se.juneday.spinnerexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import se.juneday.spinnerexample.domain.Band;
import se.juneday.spinnerexample.storage.BandStoreFactory;
import se.juneday.spinnerexample.storage.FakeBandStore;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {


  private static final String LOG_TAG = MainActivity.class.getSimpleName();

  private void setupBandSpinner() {
    // Find the Spinner
    Spinner spinner = (Spinner) findViewById(R.id.band_spinner);

    // Create an ArrayAdapter using the string array and a default spinner layout
    // there are other ways to create such an adapater and layout, but let's stick to basics
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.great_bands_array, android.R.layout.simple_spinner_item);

    // Use an Android layout to present the bands
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // Use the adapter above with the Spinner
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(this);
  }

  private void setupPunkSpinner() {
    Spinner spinner = (Spinner) findViewById(R.id.punk_spinner);
    ArrayAdapter<Band> adapter =
        new ArrayAdapter<Band>(this,
            android.R.layout.simple_spinner_item,
            BandStoreFactory.getBandStore().bands());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    //Using anon inner class
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        Log.d(LOG_TAG, "onItemSelected");
        Band band = (Band) adapterView.getItemAtPosition(pos);
        TextView tv = findViewById(R.id.punk_band_view);
        tv.setText(band.toString());
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setupBandSpinner();
    setupPunkSpinner();

  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
    Log.d(LOG_TAG, "onItemSelected");
    String s = (String) adapterView.getItemAtPosition(pos);
    TextView tv = findViewById(R.id.southern_rock_band_view);
    tv.setText(s);
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {
  }

}
