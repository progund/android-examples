package se.juneday.androidchat;

import android.content.ClipData.Item;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import se.juneday.androidchat.ChatHandler.ChatHandlerStatus;
import se.juneday.androidchat.ChatHandler.MessageListener;

public class MainActivity extends AppCompatActivity {

  private static final String LOG_TAG = MainActivity.class.getSimpleName();

  // UI stuff
  private EditText userInput;
  private TextView messages;
  private Button sendButton;

  private String server;
  private int port;

  private ChatHandler chat;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    if (!running) {
      startChatClient();
      running = true;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.settings_menu, menu);
    return true;
  }


  private boolean running;
  private void updateState(MenuItem item){

    if (running) {
      Log.d(LOG_TAG, "running");
      running=false;
//      ChatHandler.getInstance(this).cancel(true);
      chat.cancel(true);
      chat=null;
      item.setTitle(getString(R.string.start_chat));
    } else {
      Log.d(LOG_TAG, "NOT running");
      running=true;
      item.setTitle(getString(R.string.close_chat));
      startChatClient();
    }
    // Give some time for chat to start/close
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        return true;
      case R.id.action_state:
        updateState(item);
        return true;
      /*case R.id.action_start:
        startChatClient();
        return true;
        */
    }
    return true;
  }

  private void feedbackStatus(String msg) {
    int duration = Toast.LENGTH_SHORT;

    Toast toast = Toast.makeText(this, msg, duration);
    toast.show();
  }

  public void startChatClient() {
    //Log.d(LOG_TAG, "startChatClient(): status: " + ChatHandler.getInstance(this).isRunning());
    if (chat==null) {
      chat = new ChatHandler();
    } else {
      feedbackStatus("Chat already running");
      return;
    }
/*    if (chat.isRunning()) {
      feedbackStatus("Chat already running");
      return ;
    }
    */
    //ChatHandler.getInstance(this).addMessageListener(new MessageListener() {
    chat.addMessageListener(new MessageListener() {
      @Override
      public void onMessage(String message) {
//        messaQeueue.add(message);
        messages.append(message+"\n");
      }

      @Override
      public void onStatus(ChatHandlerStatus status) {
        switch (status) {
          case CHAT_HANDLER_OK:
            feedbackStatus("Chat closed");
            break;
          case CHAT_HANDLER_SOCKET_FAILURE:
            feedbackStatus("Could not connect to server: ");
            break;
        }
      }
    });
    Log.d(LOG_TAG, "Starting chat client...");
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    server = preferences.getString("pref_server_name", null);
    port = Integer.parseInt(preferences.getString("pref_server_port", null));
    Log.d(LOG_TAG, "server name: " + server);
    Log.d(LOG_TAG, "server port: " + port);

//    ChatHandler.getInstance(this).setServer(server, port);
  //  ChatHandler.getInstance(this).execute(null, null, null);
    chat.setServer(server, port);
    chat.execute(null, null, null);
    Log.d(LOG_TAG, "Started chat client...");
  }

  @Override
  public void onStart(){
    super.onStart();

    Log.d(LOG_TAG, " --> onStart()");

    userInput = (EditText) findViewById(R.id.user_input);
    messages = (TextView) findViewById(R.id.message_view);
    sendButton = (Button) findViewById(R.id.send_button);

    messages.setMovementMethod(new ScrollingMovementMethod());
    // chatRunning=true;
    Log.d(LOG_TAG, " <-- onStart()");
  }

  public void sendMessage(View v) {
    final String msg = userInput.getText().toString();
    if (msg.equals("")) {
      Log.d(LOG_TAG, "sendMessage, NOT SENDING EMPTY STRING");
    } else {
      if(chat!=null) {
//        ChatHandler.getInstance(this).sendMessage(msg);
        chat.sendMessage(msg);
      }
      userInput.setText("");
    }
  }


}
