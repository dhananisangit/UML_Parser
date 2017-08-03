import com.github.javaparser.JavaParser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class umlGenerator
{
    public static String str ="@startuml" +"\n";
    public static String cname = null;
    public static String sjd;
    public static String sjd1;
    public static String firstpart;
    public static String extendsname;
    public static List<ClassOrInterfaceType> ename;
    public static List<ClassOrInterfaceType> interfaceName ;
    public static List<VariableDeclarator> list1;
    public static List<String> allCollections = new ArrayList<String>();
    public static List<String> allLists = new ArrayList<String>();
    public static List<String> cClass= new ArrayList<String>();
    public static List<String> pClasses = new ArrayList<String>();
    public static List<String> TemppClasses = new ArrayList<String>();
    public static List<String> associationName = new ArrayList<String>();
    public static List<String> tempassocName = new ArrayList<String>();
    public static List<String> inames= new ArrayList<String>();

    public static String item = null;
    public static void main(String[] args) throws Exception
    {


        String inputPath=args[0];
        String outputPath=args[1];
        File f = new File("E:\\intellij\\projects\\demo_uml\\pauls source code\\uml-parser-test-1");
        File[] files = f.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".java");
            }
        });


        for (File javafile : files)
        {
            FileInputStream in = new FileInputStream(javafile);
            CompilationUnit cu;
            try
            {
                cu = JavaParser.parse(in);
            } finally
            {
                in.close();
            }
            new allNames().visit(cu, null);
        }
        for (File javafile : files)
        {
            FileInputStream in = new FileInputStream(javafile);
            CompilationUnit cu;
            try
            {
                cu = JavaParser.parse(in);
            } finally
            {
                in.close();
            }
            new ClassVisitor().visit(cu, null);
            new MethodVisitor().visit(cu, null);
            new ConstVisitor().visit(cu, null);
            new VariableVisitor().visit(cu, null);
            new simpleVisitor().visit(cu, null);

        }


        pngCreator p = new pngCreator();
        p.createPng(str, outputPath);
    }



    private static class ClassVisitor extends VoidVisitorAdapter
    {
        public void visit(ClassOrInterfaceDeclaration n, Object arg)
        {
            cname = n.getName();
            pClasses.add(cname);
            ename = n.getExtends();

            for(ClassOrInterfaceType elist :ename)
            {
                String extName = elist.toString();
                if (extName != null)
                {
                    str+=extName+" <|-- "+cname + "\n";
                }
            }

            interfaceName =n.getImplements();

            for(ClassOrInterfaceType ilist :interfaceName)
            {
                String iName = ilist.toString();
                if (iName != null && iName != cname)
                {
                    str+="interface "+iName+" <|.. "+cname + "\n";
                }
                else
                {
                    str+="Class "+ cname+"\n";
                }
            }
        }
    }


    private static class allNames extends VoidVisitorAdapter
    {
        public void visit(ClassOrInterfaceDeclaration n, Object arg)
        {
            String allC = n.getName();
            if (!TemppClasses.contains(allC)) {
                TemppClasses.add(allC);
            }
            if (!inames.contains(n.getImplements().toString())) {
                inames.add(n.getImplements().toString());
            }
        }
    }

    private static class ConstVisitor extends VoidVisitorAdapter
    {
        public void visit(ConstructorDeclaration n, Object arg)
        {
            String cr="";
            String parameterName="";
            String firstp=null;
            String secondpart;
            String constname = n.getName();
            List<Parameter> paramlist = n.getParameters();

            if(n.getModifiers()== 1)
            {
                cr="+";
            }
            else if(n.getModifiers()== 2)
            {
                cr="-";
            }

            System.out.println("\n");
            for (Parameter paralist:paramlist)
            {
                String Param = paralist.toString();
                int i = Param.indexOf(' ');
                firstp = Param.substring(0, i);
                secondpart = Param.substring(i);
                parameterName=secondpart+" : "+firstp;
            }
            if(TemppClasses.contains(firstp) && !firstp.equals(null))
            {
                sjd1 = firstp;
            }

            if(cr=="+" || cr=="-")
            {
                str += cname + " : " + cr + " " + constname + "(" + parameterName + ")" +"\n";
            }
        }
    }


    private static class MethodVisitor extends VoidVisitorAdapter
    {
        public void visit(MethodDeclaration n, Object arg)
        {

            String cr="";
            String mName = n.getName();
            String parameterName="";


            for(ClassOrInterfaceType elist :ename)
            {
                extendsname = elist.toString();
            }

            String compare = mName.substring(0,3);
            List<Parameter> paramlist = n.getParameters();
             if(n.getModifiers()== 2)
        {
            cr="-";
        }
          else  if(n.getModifiers()== 1)

            {
                cr="+";
            }


            System.out.print("\n");
            for (Parameter paralist:paramlist)
            {
                String Param = paralist.toString();
                int i = Param.indexOf(' ');
                firstpart = Param.substring(0, i);
                String secondpart = Param.substring(i);
                parameterName=secondpart+" : "+firstpart;
            }
            if(!cr.equals(" "))
            {
                if(!(compare.equals("set") || compare.equals("get")))
                {
                    str += cname + " : " + cr + " " + mName + "(" + parameterName + ")" + " : " + n.getType() +"\n";
                }
            }
            String tp = firstpart;
            String tempo ="interface "+ firstpart+" <|.. "+extendsname;

            if (!str.contains(tempo) && TemppClasses.contains(tp) && !TemppClasses.contains(inames))
            {
                String tempo1 = firstpart + " <.. " +cname ;
                if (!cname.equals(firstpart) && !str.contains(tempo1)) {
                    str += tempo1+"\n";
                }

            }
        }
    }

    private static class VariableVisitor extends VoidVisitorAdapter
    {
        public void visit(FieldDeclaration n, Object arg)
        {
            String newstring = null;
            String tab1="";
            String cr = " ";
            String manny = "";
            String nanny ="";

            list1 = n.getVariables();

            cClass.clear();
            for (VariableDeclarator itemList : list1)
            {
                item = itemList.toString();

                if (item.contains("="))
                {
                    if (item != null && item.length() > 0)
                    {
                        int eIndex = (item.lastIndexOf("="));
                        newstring = item.substring(0, eIndex);
                        newstring = newstring.trim();

                        if (n.getModifiers() == 2)
                    {
                        cr = "-";
                    }
                      else  if (n.getModifiers() == 1)
                        {
                            cr = "+";
                        }


                        sjd= n.getType().toString();
                        if(!TemppClasses.contains(sjd))
                        {
                            if(!cr.equals(" ") && !sjd.contains("Collection<"))
                            {
                                allLists.add(sjd);
                                cClass.add(sjd);
                                str += cname + " : " + cr + " " + item + " : " + n.getType().toString()+"\n";
                            }
                            else if(!cr.equals(" ") && sjd.contains("Collection<"))
                            {
                                int ind = sjd.indexOf('<');
                                String firstp = sjd.substring(ind+1);
                                int jap = firstp.indexOf('>');
                                String variable1 = firstp.substring(0,jap);
                                str += variable1 +"\"*\"--"+ cname +"\n";
                                allCollections.add(variable1);
                            }

                        }

                    }
                }
                else
                {
                     if (n.getModifiers() == 2)
                {
                    cr = "-";
                }
                   else if (n.getModifiers() == 1)
                    {
                        cr = "+";
                    }


                    sjd= n.getType().toString();

                    if(!TemppClasses.contains(sjd) && !sjd.contains("Collection<"))
                    {
                        if(!cr.equals(" "))
                        {
                            str += cname + " : " + cr + " " + item + " : " + n.getType().toString()+"\n";
                        }
                    }
                    if (sjd != null)
                    {
                        if(sjd.contains("Collection"))
                        {
                            int i = sjd.lastIndexOf("<");
                            manny = sjd.substring(i + 1);
                            nanny = manny.substring(0, 1);
                            allCollections.add(nanny);
                        }
                        else
                        {
                            allLists.add(sjd);
                            cClass.add(sjd);
                        }
                    }
                }

                String temporary = "";
                for (String getclasses : TemppClasses)
                {
                    if (sjd.equals(getclasses) )
                    {
                        temporary = cname + " -- " + getclasses;
                        associationName.add(temporary);

                        StringBuilder sb = new StringBuilder();
                        sb.append(temporary);
                        String zen = sb.reverse().toString();
                        for (String p :associationName)
                        {
                            if (!tempassocName.isEmpty())
                            {
                                if (!p.equals(zen) && !p.equals(temporary))
                                {
                                    tempassocName.add(temporary);
                                    break;
                                }
                                else
                                {
                                    break;
                                }
                            }
                            else
                            {
                                tempassocName.add(temporary);
                            }
                        }
                        for (String tz : tempassocName)
                        {
                            tab1 = tz;
                        }
                        if (!str.contains(tab1) )
                        {
                            str += tab1 +"\n";
                        }
                    }
                }
            }
        }
    }

    private static class simpleVisitor extends VoidVisitorAdapter
    {
        public void visit(ExpressionStmt n, Object arg)
        {
            if(n.getExpression().toString().contains("super.")) {
                if (cname.contains("ComcreteDecorator") && !str.contains(sjd1 + " <.. " + cname)) {
                    str += sjd1 + " <.. " + cname +"\n";
                }
            }
        }
    }
}

