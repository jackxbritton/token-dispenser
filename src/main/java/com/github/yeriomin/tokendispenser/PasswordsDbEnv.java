package com.github.yeriomin.tokendispenser;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

class Password {
  public String username, password;
  Password(String u, String p) {
    username = u;
    password = p;
  }
}

public class PasswordsDbEnv implements PasswordsDbInterface {

  static private final String FIELD_SEPARATOR = ":";
  static private final String LINE_SEPARATOR = ",";

  private Map<String, String> passwords = new HashMap<>();
  
  PasswordsDbEnv(Properties config) {

    String envString = System.getenv(Server.ENV_TOKEN_CREDENTIALS);
    String[] lines = envString.split(LINE_SEPARATOR);
    for (String line : lines) {
      String[] pair = line.split(FIELD_SEPARATOR);
      if (pair.length != 2) {
          Server.LOG.warn("Invalid user:pass pair in " + Server.ENV_TOKEN_CREDENTIALS);
          continue;
      }
      String email = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
      String password = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
      passwords.put(email, password);
      System.out.println(passwords);
    }

  }
  
  @Override
  public String getRandomEmail() {
    List<String> emails = new ArrayList<>(passwords.keySet());
    return emails.get(new Random().nextInt(emails.size()));
  }
  
  @Override
  public String get(String email) {
    Server.LOG.info(email + (passwords.containsKey(email) ? "" : " NOT") + " found");
    return passwords.get(email);
  }
  
  @Override
  public void put(String email, String password) {
    throw new UnsupportedOperationException("put is not supported for env storage");
  }
}