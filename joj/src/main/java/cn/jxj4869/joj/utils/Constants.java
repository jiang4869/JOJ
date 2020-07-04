package cn.jxj4869.joj.utils;

import cn.jxj4869.joj.pojo.Info;

import java.util.*;

/**
 * 定义一些常量
 *
 * @author JiangXiaoju
 * @date 2020-06-12 20:19
 */

public class Constants {
    /*
        分页参数
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer DEFAULT_PROBLEM_PAGE_SIZE = 30;
    public static final Integer DEFAULT_Submission_PAGE_SIZE = 30;
    public static final Integer DEFAULT_CONTEST_PAGE_SIZE = 30;
    public static final Integer DEFAULT_LATEST_SUBMIT_PAGE_SIZE=5;
    public static final Integer DEFAULT_ADMIN_PAGE_SIZE=20;

    /*
       OJ列表
    */
    static public final List<String> OJList = new ArrayList<String>();

    static {
        OJList.add("HDU");
//        OJList.add("CodeForces");

    }

    /*
        OJ的输入格式说明
     */
    static public final Map<String, String> lf = new HashMap<String, String>();

    static {
        lf.put("HDU", "%I64d & %I64u");
//        lf.put("CodeForces", "%I64d & %I64u");
    }


    /*
        各个OJ支持的语言
     */
    static public final Map<String, Map<String, String>> ojLanguageList = new HashMap<>();

    static {
        Map<String, String> hduLanguageList = new LinkedHashMap<String, String>();
        // HDU LanguageList
        hduLanguageList.put("0", "G++");
        hduLanguageList.put("1", "GCC");
        hduLanguageList.put("2", "C++");
        hduLanguageList.put("3", "C");
        hduLanguageList.put("4", "Pascal");
        hduLanguageList.put("5", "Java");

        ojLanguageList.put("HDU", hduLanguageList);

//        // CodeForces LanguageList
//        Map<String, String> cfLanguageList = new LinkedHashMap<String, String>();
//        cfLanguageList.put("3", "Delphi 7");
//        cfLanguageList.put("4", "Free Pascal 3.0.2");
//        cfLanguageList.put("6", "PHP 7.2.13");
//        cfLanguageList.put("7", "Python 2.7.15");
//        cfLanguageList.put("8", "Ruby 2.0.0p645");
//        cfLanguageList.put("9", "C# Mono 6.8");
//        cfLanguageList.put("12", "Haskell GHC 8.10.1");
//        cfLanguageList.put("13", "Perl 5.20.1");
//        cfLanguageList.put("19", "OCaml 4.02.1");
//        cfLanguageList.put("20", "Scala 2.12.8");
//        cfLanguageList.put("28", "D DMD32 v2.091.0");
//        cfLanguageList.put("31", "Python 3.7.2");
//        cfLanguageList.put("32", "Go 1.14");
//        cfLanguageList.put("34", "JavaScript V8 4.8.0");
//        cfLanguageList.put("36", "Java 1.8.0_241");
//        cfLanguageList.put("40", "PyPy 2.7 (7.2.0)");
//        cfLanguageList.put("41", "PyPy 3.6 (7.2.0)");
//        cfLanguageList.put("48", "Kotlin 1.3.70");
//        cfLanguageList.put("49", "Rust 1.42.0");
//        cfLanguageList.put("51", "PascalABC.NET 3.4.2");
//        cfLanguageList.put("55", "Node.js 12.6.3");
//        cfLanguageList.put("59", "Microsoft Visual C++ 2017");
//        cfLanguageList.put("60", "Java 11.0.6");
//        cfLanguageList.put("61", "GNU G++17 9.2.0 (64 bit, msys 2)");
//        cfLanguageList.put("54", "GNU G++17 7.3.0");
//        ojLanguageList.put("CodeForces", cfLanguageList);
    }


    /*
        将OJ支持多语言与ace编辑器对应的值进行映射

        <option value="c_cpp" selected>c</option>
        <option value="c_cpp">c++</option>
        <option value="java">java</option>
        <option value="python">python</option>
     */

    static public final Map<String, Map<String, String>> aceOjLanguageList = new HashMap<>();

    static {
        Map<String, String> hduLanguageList = new LinkedHashMap<String, String>();
        // HDU LanguageList
        hduLanguageList.put("G++", "c_cpp");
        hduLanguageList.put("GCC", "c_cpp");
        hduLanguageList.put("C++", "c_cpp");
        hduLanguageList.put("C", "c_cpp");
        hduLanguageList.put("Pascal", "pascal");
        hduLanguageList.put("Java", "java");

        aceOjLanguageList.put("HDU", hduLanguageList);


//        // CodeForces LanguageList
//        Map<String, String> cfLanguageList = new LinkedHashMap<String, String>();
//        cfLanguageList.put("Delphi 7", "other");
//        cfLanguageList.put("Free Pascal 3.0.2", "pascal");
//        cfLanguageList.put("PHP 7.2.13", "php");
//        cfLanguageList.put("Python 2.7.15", "python");
//        cfLanguageList.put("Ruby 2.0.0p645", "ruby");
//        cfLanguageList.put("C# Mono 6.8", "csharp");
//        cfLanguageList.put("Haskell GHC 8.10.1", "hashell");
//        cfLanguageList.put("Perl 5.20.1", "perl");
//        cfLanguageList.put("OCaml 4.02.1", "ocaml");
//        cfLanguageList.put("Scala 2.12.8", "scala");
//        cfLanguageList.put("D DMD32 v2.091.0", "d");
//        cfLanguageList.put("Python 3.7.2", "python");
//        cfLanguageList.put("Go 1.14", "other");
//        cfLanguageList.put("JavaScript V8 4.8.0", "javascript");
//        cfLanguageList.put("Java 1.8.0_241", "java");
//        cfLanguageList.put("PyPy 2.7 (7.2.0)", "python");
//        cfLanguageList.put("PyPy 3.6 (7.2.0)", "python");
//        cfLanguageList.put("Kotlin 1.3.70", "kotlin");
//        cfLanguageList.put("Rust 1.42.0", "rust");
//        cfLanguageList.put("PascalABC.NET 3.4.2", "pascal");
//        cfLanguageList.put("Node.js 12.6.3", "javascript");
//        cfLanguageList.put("Microsoft Visual C++ 2017", "c_cpp");
//        cfLanguageList.put("Java 11.0.6", "java");
//        cfLanguageList.put("GNU G++17 9.2.0 (64 bit, msys 2)", "c_cpp");
//        cfLanguageList.put("GNU G++17 7.3.0", "c_cpp");
//        aceOjLanguageList.put("CodeForces", cfLanguageList);
    }

}
