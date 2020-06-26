package org.mclogs.loghelper.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.net.ssl.HttpsURLConnection;

public class HTTPUtil {
  private static final String USER_AGENT = "Mozilla/5.0";
  
  public static String analyzeFile(File f) {
    try {
      StringBuilder param = new StringBuilder("content=");
      param.append(URLEncoder.encode(readFile(f.getPath(), StandardCharsets.UTF_8), "UTF-8"));
      String url = "https://api.mclo.gs/1/log";
      URL obj = new URL(url);
      HttpsURLConnection con = (HttpsURLConnection)obj.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      con.setDoInput(true);
      con.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
      wr.write(param.toString());
      wr.flush();
      wr.close();
      BufferedReader resp = new BufferedReader(new InputStreamReader(con.getInputStream()));
      StringBuffer response = new StringBuffer();
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
  
  static String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path, new String[0]));
    return new String(encoded, encoding);
  }
}
