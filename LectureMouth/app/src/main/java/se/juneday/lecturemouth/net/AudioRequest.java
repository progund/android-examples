package se.juneday.lecturemouth.net;

import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.Map;

public class AudioRequest extends Request<byte[]> {

  private final Response.Listener<byte[]> listener;
  public Map<String, String> responseHeaders ;
  private final static String LOG_TAG = AudioRequest.class.getSimpleName();


  public AudioRequest(String url,
      Listener<byte[]> listener,
      ErrorListener elistener) {
    super(url, elistener);
    Log.d(LOG_TAG, "AudioRequest()   url: " + url);
    this.listener = listener;
    setShouldCache(false);
  }

  @Override
  protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
    responseHeaders = response.headers;
    Log.d(LOG_TAG, "parseNetworkResponse()");
    return Response.success( response.data, HttpHeaderParser.parseCacheHeaders(response));
  }

  @Override
  protected void deliverResponse(byte[] response) {
    Log.d(LOG_TAG, "deliverResponse()");
    listener.onResponse(response);
  }
}
