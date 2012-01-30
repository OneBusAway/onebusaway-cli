package org.onebusaway.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandLineInterfaceLibrary {

  public static boolean wantsHelp(String[] args) {
    if (args.length == 0) {
      return false;
    }
    String first = args[0].toLowerCase();
    while (first.startsWith("-")) {
      first = first.substring(1);
    }
    return first.equals("h") || first.equals("help") || first.equals("?");
  }

  public static void printUsage(Class<?> mainClass) {
    InputStream is = mainClass.getResourceAsStream("usage.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        System.err.println(line);
      }
    } catch (IOException ex) {

    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException ex) {

        }
      }
    }
  }
}
