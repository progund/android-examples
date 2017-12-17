package se.juneday.objectcacheexample;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import se.juneday.ObjectCache;
import se.juneday.objectcacheexample.Product.Builder;

public class MainActivity extends AppCompatActivity {

  private static final String LOG_TAG = MainActivity.class.getSimpleName();

  private List<Product> products;
  private ObjectCache<Product> cache;
  private ArrayAdapter<Product> adapter ;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    PackageManager m = getPackageManager();
    String s = getPackageName();
    try {
      PackageInfo p = m.getPackageInfo(s, 0);
      s = p.applicationInfo.dataDir;
    } catch (PackageManager.NameNotFoundException e) {
      Log.d(LOG_TAG, "Error, could not build file name for serialization", e);
    }
    String fileName = s +
        "/" +
        MainActivity.class.getCanonicalName();

    Log.d(LOG_TAG, "Using file: " + fileName);
    cache = new ObjectCache<>(fileName);
    cache.pull();
    products = cache.get();

    if (products==null) {
      products = new ArrayList<>();
    } else {
      showToast("Read from cache " + products.size() + " products");
    }
    if (products!=null) {    Log.d(LOG_TAG, " products in store: " + products.size()); }

    ListView listView = (ListView) findViewById(R.id.product_list);

    // Create Adapter
    adapter = new ArrayAdapter<Product>(this,
        android.R.layout.simple_list_item_1,
        products);

    // Set listView's adapter to the new adapter
    listView.setAdapter(adapter);

  }

  public void refresh(View view) {
    Log.d(LOG_TAG, "Sync products");

    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this);
    String url =  "http://rameau.sandklef.com/all.json";


    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
        Request.Method.GET,
        url,
        null,
        new Response.Listener<JSONArray>() {

          @Override
          public void onResponse(JSONArray array) {
            List<Product> tmpProducts = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
              try {
                JSONObject row = array.getJSONObject(i);
                String name = row.getString("name");
                double price = row.getDouble("price");
                double alcohol = row.getDouble("alcohol");
                int volume = row.getInt("volume");
                int nr = row.getInt("nr");
                String group = row.getString("product_group");
                Product.Builder builder = new Builder();
                builder.name(name).alcohol(alcohol).nr(nr).type(group).volume(volume).price(price);
                tmpProducts.add(builder.build());
                //Log.d(LOG_TAG, " * " + name + " " + price + " " + volume + " " + nr + " " + group + " ");
                } catch (JSONException e) {
                ; // is ok since this is debug
                Log.d(LOG_TAG, "woops");
                e.printStackTrace();
              }
            }
            if (tmpProducts!=null) {
              Log.d(LOG_TAG, "List, items: " + tmpProducts.size());
              products = tmpProducts;
              cache.set(products);
              cache.push();
              showToast("Cached " + products.size() + " products");
              updateList();
            }
          }

        }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
        showToast("Download failure");
      }
    });
    queue.add(jsonArrayRequest);
  }

  public void updateList() {
    Log.d(LOG_TAG, "updateList()");
    // Create Adapter
    adapter = new ArrayAdapter<Product>(this,
        android.R.layout.simple_list_item_1,
        products);

    ListView listView = (ListView) findViewById(R.id.product_list);
    // Set listView's adapter to the new adapter
    listView.setAdapter(adapter);
  }

  private void showToast(String msg) {
    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
    toast.show();
    Log.d(LOG_TAG, "showToast: " + msg);
  }


}
