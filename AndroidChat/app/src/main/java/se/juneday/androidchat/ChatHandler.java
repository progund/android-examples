package se.juneday.androidchat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ContentHandler;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import se.juneday.androidchat.ChatHandler.ChatHandlerStatus;


public class ChatHandler extends AsyncTask<Void, String, ChatHandlerStatus> {


/*  public static class ServerInfo {
    public ServerInfo(String server, int port){
      this.serverName = server;
      this.port = port;
    }
    public String serverName;
    public int port;
  }
  */

public enum ChatHandlerStatus{
    CHAT_HANDLER_OK,
    CHAT_HANDLER_SOCKET_FAILURE,
  };

  public interface MessageListener {
    void onMessage(String message);
    void onStatus(ChatHandlerStatus status);
  }

  private static final String LOG_TAG = ChatHandler.class.getSimpleName();
  private List<MessageListener> listeners;

  // Networking
  private Socket socket;
  private String server = "10.0.2.2";
  private int port = 1066;
  private Handler h;

  // Streams
  private BufferedReader in;
  private PrintWriter out;

  private Context context;
  private static ChatHandler instance;

  boolean running;

  private ChatHandler(Context context) {
    listeners = new ArrayList<>();
    this.context = context;
  }

  public void setServer(String server, int port) {
    this.server = server;
    this.port = port;
    setupSocket();
  }

  public static synchronized ChatHandler getInstance(Context context) {
    if (instance==null) {
      instance = new ChatHandler(context);
    }
    return instance;
  }

  private void setupSocket(){
    try {
      Log.d(LOG_TAG, "setupSocket()");
      socket = new Socket(server, port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out=new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Log.d(LOG_TAG, "setupSocket() socket: " + socket);
  }

  public boolean isRunning(){
    return running;
  }

  @Override
  protected void onPostExecute(ChatHandlerStatus status) {
    informListeners(status);
  }



  public void addMessageListener(MessageListener listener) {
    listeners.add(listener);
  }

  private void informListeners(String msg){
    if (msg != null) {
      for (MessageListener listener : listeners) {
        listener.onMessage(msg);
      }
    }
  }

  private void informListeners(ChatHandlerStatus status){
      for (MessageListener listener : listeners) {
        listener.onStatus(status);
      }
  }



  public void sendMessage(String msg) {
    if (out != null) {
      out.println("Android: " + msg);
    }

  }

  @Override
  protected void onProgressUpdate(String... msg) {
    informListeners(msg[0]);
  }


  @Override
  protected ChatHandlerStatus doInBackground(Void... voids) {
    setupSocket();
    running=true;
    while (!isCancelled()) {
      Log.d(LOG_TAG, "Listening ...");
      if (socket == null) {
        Log.d(LOG_TAG, "setting up socket");
        setupSocket();
      }
      if (socket == null) {
        running=false;
        return ChatHandlerStatus.CHAT_HANDLER_SOCKET_FAILURE;
      }

      Log.d(LOG_TAG, " info: " + socket + " " + in + " " + out);
      Log.d(LOG_TAG, "Trying to read");

      try {
        Log.d(LOG_TAG, "reading...");
        String msg = in.readLine();
        //String msg = "hallo";
        Log.d(LOG_TAG, "read..." + msg);
        publishProgress(msg);
        Log.d(LOG_TAG, "Got: " + msg);
      } catch (IOException e) {
        Log.d(LOG_TAG, "Uh oh... exception while reading");
        e.printStackTrace();
      }
    }
    Log.d(LOG_TAG, "Finished background task");
    instance=null;
    running=false;
    return ChatHandlerStatus.CHAT_HANDLER_OK;
  }


}

