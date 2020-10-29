package me.oczi.common.api.mojang;

import me.oczi.common.request.mojang.MojangResolverImpl;

/**
 * Mojang's api URLs for {@link MojangResolverImpl}.
 */
public enum MojangApi {
  UUID("https://api.mojang.com/users/profiles/minecraft/%s"),
  NAME("https://api.mojang.com/user/profiles/%s/names");

  public final String url;

  MojangApi(String url) {
    this.url = url;
  }

  public String of(String arg) {
    return String.format(url, arg);
  }

  public String getUrl() {
    return url;
  }
}
