package se.juneday.gitrepoviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.domain.Repository.RepoAccess;

public class MainActivity extends AppCompatActivity {

  private ArrayAdapter<Repository> adapter;
  private List<Repository> repos = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  private void populateWithFakedData() {
    repos.add(new Repository("dummy", "faked repo for test", "GPLv3", RepoAccess.PRIVATE));
    repos.add(new Repository("dummy again", "faked repo for test2", "GPLv3", RepoAccess.PUBLIC));
  }

  @Override
  public void onStart() {
    super.onStart();

    // fake data
    populateWithFakedData();

    // Lookup ListView
    ListView listView = (ListView) findViewById(R.id.repos_list);

    // Create Adapter
    adapter = new ArrayAdapter<Repository>(this,
        android.R.layout.simple_list_item_1,
        repos);

    // Set listView's adapter to the new adapter
    listView.setAdapter(adapter);
  }

}
