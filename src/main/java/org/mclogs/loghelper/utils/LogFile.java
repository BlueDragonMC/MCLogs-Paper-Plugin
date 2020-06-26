package org.mclogs.loghelper.utils;

public class LogFile {
    final String name;

    final String ext;

    // final String longname;

    // final File file;

    final String path;

    public LogFile(String name, /* String longname, */ String ext, String path/*, File file */) {
        this.name = name;
        // this.longname = longname;
        this.ext = ext;
        // this.file = file;
        this.path = path;
    }

    public String getName() {
        return this.name;
    }

//
//  public String geLongname() {
//    return this.longname;
//  }
//

    public String getExt() {
        return this.ext;
    }

//
//  public File getFile() {
//    return this.file;
//  }
//

    public String getPath() {
        return this.path;
    }
}
