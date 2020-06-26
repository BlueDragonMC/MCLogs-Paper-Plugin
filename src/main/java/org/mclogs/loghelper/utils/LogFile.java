package org.mclogs.loghelper.utils;

import java.io.File;

public class LogFile {
  String name;
  
  String ext;
  
  String longname;
  
  File file;
  
  String path;
  
  public LogFile(String name, String longname, String ext, String path, File file) {
    this.name = name;
    this.longname = longname;
    this.ext = ext;
    this.file = file;
    this.path = path;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String geLongname() {
    return this.longname;
  }
  
  public String getExt() {
    return this.ext;
  }
  
  public File getFile() {
    return this.file;
  }
  
  public String getPath() {
    return this.path;
  }
}
