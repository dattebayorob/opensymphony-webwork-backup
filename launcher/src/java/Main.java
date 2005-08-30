import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 * User: plightbo
 * Date: Aug 9, 2005
 * Time: 12:22:06 AM
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  java -jar webwork-launcher.jar [command] (optional command args)");
            System.out.println("");
            System.out.println("Where [command] is one of the following:");
            System.out.println("  prototype");
            System.out.println("  prototype:xxx");
            System.out.println("  webflow");
            System.out.println("  webflow:xxx");
            System.out.println("");
            System.out.println("Execute the commands for additional usage instructions.");
            System.out.println("Note: the *:xxx commands are just shortcuts for ");
            System.out.println("      running the command on a webapp in the webapps dir.");
            System.out.println("      For example, 'prototype:sandbox' will start prototype");
            System.out.println("      automatically for the webapp 'sandbox'.");
            return;
        }

        // check the JDK version
        String version = System.getProperty("java.version");
        boolean jdk15 = version.indexOf("1.5") != -1;

        String javaHome = (String) System.getenv().get("JAVA_HOME");
        String altJavaHome = System.getProperty("java.home");
        ArrayList urls = new ArrayList();
        try {
            findJars(new File("lib"), urls);
            // if the jar is available use that ...
            urls.add(new File("webwork-2.2-beta-1.jar").toURL());
            // ... but it might not be (ie: we're in development in IDEA), so use this as backup
            urls.add(new File("build/java/").toURL());

            // load tools.jar from JAVA_HOME
            File tools = new File(javaHome, "lib/tools.jar");
            if (!tools.exists()) {
                // hmm, not there, how about java.home?
                tools = new File(altJavaHome, "../lib/tools.jar");
            }
            if (!tools.exists()) {
                // try the OS X common path
                tools = new File(javaHome, "../Classes/classes.jar");
            }
            if (!tools.exists()) {
                // try the other OS X common path
                tools = new File(altJavaHome, "../Classes/classes.jar");
            }
            if (!tools.exists()) {
                // did the user specify it by hand?
                String prop = System.getProperty("tools");
                if (prop != null) {
                    tools = new File(prop);
                }
            }
            if (!tools.exists()) {
                System.out.println("Error: Could not find tools.jar! Please do one of the following: ");
                System.out.println("");
                System.out.println("        - Use the JDK's JVM (ie: c:\\jdk1.5.0\\bin\\java)");
                System.out.println("        - Specify JAVA_HOME to point to your JDK 1.5 home");
                System.out.println("        - Specify a direct path to tools.jar via, as shown below:");
                System.out.println("");
                System.out.println("       java -Dtools=/path/to/tools.jar -jar webwork-launcher.jar ...");
                return;
            }

            // finally, add the verified tools.jar
            urls.add(tools.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Could not find URLs -- see stack trace.");
        }

        String command = args[0];
        String[] programArgs = new String[args.length - 1];
        System.arraycopy(args, 1, programArgs, 0, programArgs.length);
        if (command.startsWith("prototype:")) {
            command = "prototype";
            String name = checkWebAppArgs(args);
            programArgs = new String[]{"/" + name,
                    "webapps/" + name + "/src/webapp",
                    "webapps/" + name + "/src/java"};
        }

        if ("prototype".equals(command)) {
            if (!jdk15) {
                System.out.println("Sorry, but prototype only runs on Java 1.5.");
                System.out.println("You are running: " + version);
                System.out.println("Please try again with Java 1.5, or deploy");
                System.out.println("  as a normal J2EE webapp to use Java 1.4.");
                return;
            }

            launch("com.opensymphony.webwork.Prototype", programArgs, urls);
            return;
        }

        if (command.startsWith("webflow:")) {
            command = "webflow";
            String name = checkWebAppArgs(args);
            programArgs = new String[]{"-config", "webapps/" + name + "/src/webapp/WEB-INF/classes",
                    "-views", "webapps/" + name + "/src/webapp",
                    "-output", "."};
        }

        if ("webflow".equals(command)) {
            launch("com.opensymphony.webwork.webFlow.WebFlow", programArgs, urls);
        }
    }

    private static String checkWebAppArgs(String[] args) {
        int colon = args[0].indexOf(':');
        String name = null;
        try {
            name = args[0].substring(colon + 1);
        } catch (Exception e) {
        }
        if (name == null || name.equals("")) {
            System.out.println("Error: you must specify the webapp you wish");
            System.out.println("       to deploy. The webapp name must be the");
            System.out.println("       name of the directory found in webapps/.");
            System.out.println("");
            System.out.println("Example: java -jar webwork-launcher.jar prototype:sandbox");
            System.exit(1);
        }

        return name;
    }

    private static void launch(String program, String[] programArgs, ArrayList urls) {
        URLClassLoader cl = new URLClassLoader((URL[]) urls.toArray(new URL[urls.size()]));
        Thread.currentThread().setContextClassLoader(cl);
        try {
            Class clazz = cl.loadClass(program);
            Method main = clazz.getDeclaredMethod("main", new Class[]{String[].class});
            main.invoke(null, new Object[]{programArgs});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void findJars(File file, ArrayList urls) throws MalformedURLException {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                findJars(f, urls);
            } else if (f.getName().endsWith(".jar")) {
                urls.add(f.toURL());
            }
        }
    }
}