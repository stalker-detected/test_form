package utils;

import java.io.*;
import java.util.Properties;




public class PropertiesLoader {

    static private Properties prop=new Properties();
    private static final String fileName = "GF.properties";
    private static String propertiesFilePath;

    static {new PropertiesLoader();}



    public PropertiesLoader() {

        InputStream input = null;
        OutputStream output = null;

        try {
            //
            ClassLoader cl = PropertiesLoader.class.getClassLoader();
            propertiesFilePath = new File(cl.getResource(fileName).getFile()).getAbsolutePath();
            //System.out.println(propertiesFilePath);
            input = new FileInputStream(propertiesFilePath);
            if (input==null) {
                System.out.println("Sorry, file is unable! " + fileName);
            }
            prop.load(input);
            input.close();


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProp(String key) {
        return (prop == null)? String.valueOf("Property is not set!") : prop.getProperty(key);
    }
}


