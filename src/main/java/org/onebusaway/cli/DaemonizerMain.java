/**
 * Copyright (C) 2012 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.cli;

import java.lang.reflect.Method;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;

/**
 * Convenience class for daemonizing an arbitrary Java main method. Simply call
 * the {@link DaemonizerMain} class as your main class and specify a {@code
 * -mainClass some.package.YourMainClass} command line argument and an optional
 * {@code -args "arg1 arg2"} to specify command line arguments to your main
 * class and your target main class will automatically be daemonized.
 * 
 * @author bdferris
 */
public class DaemonizerMain {

  private static final String ARG_MAIN_CLASS = "mainClass";

  private static final String ARG_ARGS = "args";

  public static void main(String[] args) throws Exception {

    Options options = new Options();
    Daemonizer.buildOptions(options);

    options.addOption(ARG_MAIN_CLASS, true, "main class");
    options.addOption(ARG_ARGS, true, "arguments");

    GnuParser parser = new GnuParser();
    CommandLine cli = parser.parse(options, args);

    Daemonizer.handleDaemonization(cli);

    String mainClassValue = cli.getOptionValue(ARG_MAIN_CLASS);
    if (mainClassValue == null) {
      usage();
      System.exit(-1);
    }

    String argValues = cli.getOptionValue(ARG_ARGS);
    String[] newArgArray = argValues.split("\\s+");

    Class<?> mainClass = Class.forName(mainClassValue);
    Method method = mainClass.getMethod("main", new Class<?>[] {String[].class});

    method.invoke(null, (Object) newArgArray);
  }

  private static void usage() {
    System.out.println("usage: -mainClass path.to.your.MainClass");
  }
}
