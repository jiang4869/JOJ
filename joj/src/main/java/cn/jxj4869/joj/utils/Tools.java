package cn.jxj4869.joj.utils;


import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公用工具类
 *
 * @author jiangxiaoju
 */
@SuppressWarnings("unchecked")
public class Tools {
    public static int getRandomNumber(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min =" + min + ">" + "max=" + max);
        }
        Random rnd = new Random();
        int randomNumber = rnd.nextInt(max - min + 1);
        return min + randomNumber;
    }

    public static String getRandomName(int min, int max) {
        StringBuffer stringBuffer = new StringBuffer();
        int len = getRandomNumber(min, max);
        for (int i = 0; i < len; i++) {
            stringBuffer.append((char) getRandomNumber('a', 'z'));
        }
        return stringBuffer.toString();
    }

    public static int getRandomAge(int min, int max) {
        return getRandomNumber(min, max);
    }


    public static List generateNameList(int n) {
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nameList.add(getRandomName(2, 10));
        }
        return nameList;
    }


    /**
     * 固定在类路径下的/img/avatar中有九张图片，命名规则为1.jpg 2.jpg 等依次类推
     *
     * @return
     */
    public static String getAvatar() {

        int idx = Tools.getRandomNumber(1, 9);

        String avatars = "/img/avatars/" + idx + ".jpg";
        System.out.println(avatars);
        return avatars;
    }


    /**
     * html转义
     *
     * @param str
     * @return
     */
    public static String toHTMLChar(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '&') {
                sb.append("&#38;");
            } else if (c == '"') {
                sb.append("&#34;");
            } else if (c == '\'') {
                sb.append("&#39;");
            } else if (c == '<') {
                sb.append("&lt;");
            } else if (c == '>') {
                sb.append("&gt;");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * html反转义
     *
     * @param str
     * @return
     */
    public static String toPlainChar(String str) {
        if (str == null) {
            return "";
        }
        return str.replaceAll("&#38;", "&").replaceAll("&#34;", "\"").replaceAll("&#39;", "'").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
    }

    /**
     * 去掉html中的js部分
     *
     * @return
     */
    public static String dropScript(String html) {
        return html == null ? null : html.replaceAll("(?i)(?<=(\\b|java))script\\b", "ｓcript");
    }


    /**
     * 按照reg解析text,获取第i组
     *
     * @param text
     * @param reg
     * @param i
     * @return
     */
    public static String regFind(String text, String reg, int i) {
        Matcher m = Pattern.compile(reg, Pattern.CASE_INSENSITIVE).matcher(text);
        return m.find() ? m.group(i) : "";
    }

    /**
     * 按照reg解析text,获取第1组
     *
     * @param text
     * @param reg
     * @return
     */
    public static String regFind(String text, String reg) {
        return regFind(text, reg, 1);
    }

    /**
     * 按照reg解析text,获取第i组(区分大小写)
     *
     * @param text
     * @param reg
     * @param i
     * @return
     */
    public static String regFindCaseSensitive(String text, String reg, int i) {
        Matcher m = Pattern.compile(reg).matcher(text);
        return m.find() ? m.group(i) : "";
    }

    /**
     * 按照reg解析text,获取第1组(区分大小写)
     *
     * @param text
     * @param reg
     * @return
     */
    public static String regFindCaseSensitive(String text, String reg) {
        return regFindCaseSensitive(text, reg, 1);
    }

    /**
     * 全角转半角
     *
     * @param input 全角字符串.
     * @return 半角字符串
     */
    public static String toDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 将一段时间从毫秒数转换为通用的表示方式
     *
     * @param length 毫秒数
     * @param hasDay 转换结果是否含有"天"
     * @return
     */
    public static String transPeriod(Long length, boolean hasDay) {
        long d = length / 86400000;
        long h = (hasDay ? length % 86400000 : length) / 3600000;
        long m = length % 3600000 / 60000;
        long s = length % 60000 / 1000;
        return (hasDay && d > 0 ? (d + "天") : "") + h + ":" + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
    }





    /**
     * 为shjs寻找正确的class
     *
     * @param srcLang
     * @return
     */
    public static String findClassPrismJS(String srcLang) {
        srcLang = " " + srcLang.toLowerCase() + " ";
        if (srcLang.contains("c++") || srcLang.contains("cpp") || srcLang.contains("g++")) {
            return "language-cpp";
        } else if (srcLang.contains(" c ") || srcLang.contains("gcc")) {
            return "language-c";
        }  else if (srcLang.contains("java ")) {
            return "language-java";
        }  else if (srcLang.contains("python")) {
            return "language-python";
        } else {
            return "language-c";
        }
    }


    /**
     * 显示用于显示每种语言
     * 1. C++
     * 2. C
     * 3. Java
     * 4. Python
     * 5. other
     *
     * @param language
     * @return
     */
    public static String findShowLanguage(String language) {
        language = " " + language.toLowerCase() + " ";
        if (language.contains("c++") || language.contains("cpp") || language.contains("g++")) {
            return "C++";
        } else if (language.contains("c") || language.contains("gcc")) {
            return "C";
        } else if (language.contains("java")) {
            return "Java";
        } else if (language.contains("python")) {
            return "Python";
        } else {
            return "Other";
        }
    }

    /**
     * 用户显示的状态名称
     * 1. AC  Accepted | Accepted
     * 2. PE  Presentation Error | Presentation Error
     * 3. WA  Wrong Answer (on...) | Wrong Answer
     * 4. TLE Time Limit Exceeded | Time Limit Exceeded
     * 5. MLE Memory Limit Exceeded | Memory Limit Exceeded
     * 6. OLE Output Limit Exceeded | Output Limit Exceeded
     * 7. RE  Runtime Error | Runtime Error
     * 8. CE  Compilation Error | Compilation Error
     * 9. UE  Unknown Error |
     * 10. Running & Judging
     * 11. Pending  // 提交后，保存到数据库中的状态。
     *
     * @param status
     * @return
     */
    public static Integer findStatusType(String status) {
        status = status.toLowerCase();
        if (status.contains("accepted")) {
            return 1;
        } else if (status.contains("presentation")) {
            return 2;
        } else if (status.contains("wrong answer")) {
            return 3;
        } else if (status.contains("time limit")) {
            return 4;
        } else if (status.contains("memory limit")) {
            return 5;
        } else if (status.contains("output limit")) {
            return 6;
        } else if (status.contains("runtime error")) {
            return 7;
        } else if (status.contains("compilation error")) {
            return 8;
        } else if (status.contains("running")) {
            return 10;
        } else if (status.contains("pending")) {
            return 11;
        } else {
            return 9;
        }

    }


    public static String trim(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        } else {
            return str.trim();
        }
    }


}
