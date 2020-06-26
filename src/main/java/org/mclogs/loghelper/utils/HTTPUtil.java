package org.mclogs.loghelper.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.net.ssl.HttpsURLConnection;

public class HTTPUtil {
  private static final String USER_AGENT = "Mozilla/5.0";
  
  public static String analyzeFile(File f) {
    try {
      String url = "https://api.mclo.gs/1/log";
      URL obj = new URL(url);
      HttpsURLConnection con = (HttpsURLConnection)obj.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      con.setDoInput(true);
      con.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
      wr.write("content=" + URLEncoder.encode(readFile(f.getPath()), "UTF-8"));
      wr.flush();
      wr.close();
      BufferedReader resp = new BufferedReader(new InputStreamReader(con.getInputStream()));
      StringBuilder response = new StringBuilder();
      String inputLine;
      while ((inputLine = resp.readLine()) != null)
        response.append(inputLine); 
      resp.close();
      return response.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  static String readFile(String path) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, StandardCharsets.UTF_8);
  }
}
