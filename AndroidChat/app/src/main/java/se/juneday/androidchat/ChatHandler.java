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
import java.net.SocketTimeoutException;
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

//  private Context context;
  private static ChatHandler instance;

  boolean running;

  public ChatHandler(/*Context context*/) {
    listeners = new ArrayList<>();
  //  this.context = context;
  }

  public void setServer(String server, int port) {
    this.server = server;
    this.port = port;
    setupSocket();
  }

  /*
  public static synchronized ChatHandler getInstance(Context context) {
    if (instance==null) {
      instance = new ChatHandler(context);
    }
    return instance;
  }
*/
  private void setupSocket(){
    try {
      Log.d(LOG_TAG, "setupSocket() creating socket");
      socket = new Socket(server, port);
      Log.d(LOG_TAG, "setupSocket() setting timeout");
      socket.setSoTimeout(1000);
      Log.d(LOG_TAG, "setupSocket() getting input");
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      Log.d(LOG_TAG, "setupSocket() getting output");
      out=new PrintWriter(socket.getOutputStream(), true);
      Log.d(LOG_TAG, "setupSocket() all done");
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
    running=false;
    informListeners(status);
    instance=null;
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
    if(isRunning()) {
      return ChatHandlerStatus.CHAT_HANDLER_SOCKET_FAILURE;
    }
    running=true;
    setupSocket();
    while (!isCancelled()) {
      Log.d(LOG_TAG, "Listening ...");
      if (socket == null) {
        Log.d(LOG_TAG, "setting up socket");
        setupSocket();
      }
      if (socket == null) {
        running=false;
        instance=null;
        return ChatHandlerStatus.CHAT_HANDLER_SOCKET_FAILURE;
      }

      try {
        String msg = in.readLine();
        if (msg==null) {
          running=false;
          instance=null;
          return ChatHandlerStatus.CHAT_HANDLER_SOCKET_FAILURE;
        }

        publishProgress(msg);
      } catch (SocketTimeoutException e) {
        // expected if no activity, loop again
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

