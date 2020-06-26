package org.mclogs.loghelper.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class UnpackUtil {
  public static void gunzipIt(String path, String outpath) {
    byte[] buffer = new byte[1024];
    try {
      GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(path));
      FileOutputStream out = new FileOutputStream(outpath);
      int len;
      while ((len = gzis.read(buffer)) > 0)
        out.write(buffer, 0, len); 
      gzis.close();
      out.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    } 
  }
}
