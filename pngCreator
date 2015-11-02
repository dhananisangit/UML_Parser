import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import net.sourceforge.plantuml.SourceStringReader;


public class pngCreator {

    public  void createPng(String source, String src) {

        source+="@enduml";
        OutputStream png = null;
        try {

      //    System.out.println(source);
           // System.out.println("-----------");
         //   System.out.println(src);

          //  System.out.println(source.replaceAll("\\s+", System.getProperty("line.separator")));
             //String image;
           // image = source.replaceAll("\\s+",System.getProperty("line.separator"));
            png = new FileOutputStream("E:\\intellij\\projects\\demo_uml\\pauls source code");
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        SourceStringReader reader = new SourceStringReader(source);

        try {
            reader.generateImage(png);
        } catch (IOException b) {

            b.printStackTrace();
        }

    }
}
