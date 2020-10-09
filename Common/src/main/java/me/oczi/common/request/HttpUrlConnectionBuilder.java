package me.oczi.common.request;

import me.oczi.common.api.state.BuildableState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public interface HttpUrlConnectionBuilder
    extends BuildableState<HttpURLConnection> {

  static HttpUrlConnectionBuilder newBuilder() {
    return new HttpUrlConnectionBuilderImpl();
  }

  static HttpUrlConnectionBuilder newBuilder(String urlName) {
    return new HttpUrlConnectionBuilderImpl(urlName);
  }

  HttpUrlConnectionBuilder setURL(String urlName);

  HttpUrlConnectionBuilder setURL(URL urlName);

  HttpUrlConnectionBuilder setMethod(String method);

  HttpUrlConnectionBuilder setInstanceFollowRedirects(boolean redirects);

  HttpUrlConnectionBuilder setProperty(String key, String value);

  HttpUrlConnectionBuilder setConnectionTimeout(int timeout);

  HttpUrlConnectionBuilder setReadTimeout(int timeout);

  default BufferedReader request()
      throws IOException {
    return new BufferedReader(
        new InputStreamReader(
            build().getInputStream(),
            StandardCharsets.UTF_8));
  }
}
