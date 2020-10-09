package me.oczi.common.request;

import me.oczi.common.utils.CommonsUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * A basic builder for {@link HttpURLConnection}.
 */
class HttpUrlConnectionBuilderImpl
    implements HttpUrlConnectionBuilder {
  private String urlName;
  private URL url;

  private int connectionTimeout;
  private int readTimeout;

  private boolean redirects;
  private String method;
  private Properties properties;

  public HttpUrlConnectionBuilderImpl() {}

  public HttpUrlConnectionBuilderImpl(String urlName) {
    this.urlName = urlName;
  }

  @Override
  public HttpUrlConnectionBuilder setURL(String urlName) {
    this.urlName = urlName;
    return this;
  }

  @Override
  public HttpUrlConnectionBuilder setURL(URL url) {
    this.url = url;
    return this;
  }

  @Override
  public HttpUrlConnectionBuilder setMethod(String method) {
    this.method = method;
    return this;
  }

  @Override
  public HttpUrlConnectionBuilder setInstanceFollowRedirects(boolean redirects) {
    this.redirects = redirects;
    return this;
  }

  @Override
  public HttpUrlConnectionBuilder setProperty(String key, String value) {
    if (properties == null) {
      this.properties = new Properties();
    }
    this.properties.setProperty(key, value);
    return this;
  }

  @Override
  public HttpUrlConnectionBuilder setConnectionTimeout(int timeout) {
    this.connectionTimeout = timeout;
    return this;
  }

  @Override
  public HttpUrlConnectionBuilder setReadTimeout(int timeout) {
    this.readTimeout = timeout;
    return this;
  }

  @Override
  public HttpURLConnection build() {
    try {
      if (this.url == null) {
        if (!CommonsUtils.isNullOrEmpty(urlName)) {
          this.url = new URL(urlName);
        } else {
          throw new MalformedURLException("URL is null or empty.");
        }
      }
      HttpURLConnection connection =
          (HttpURLConnection) url.openConnection();
      if (!CommonsUtils.isNullOrEmpty(method)) {
        connection.setRequestMethod(method);
      }
      if (readTimeout > 0) {
        connection.setReadTimeout(readTimeout);
      }
      if (connectionTimeout > 0) {
        connection.setConnectTimeout(connectionTimeout);
      }
      connection.setInstanceFollowRedirects(redirects);
      putPropertiesIfPresent(connection);
      return connection;
    } catch (IOException e) {
      throw new RuntimeException(
          "Error while trying to build a HttpURLConnection", e);
    }
  }

  private void putPropertiesIfPresent(HttpURLConnection connection) {
    if (!CommonsUtils.isNullOrEmpty(properties)) {
      for (String key : properties.stringPropertyNames()) {
        String value = properties.getProperty(key);
        connection.setRequestProperty(key, value);
      }
    }
  }
}
