package cn.jxj4869.joj;

import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.submitter.CodeForcesSubmitter;
import cn.jxj4869.joj.utils.Tools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author JiangXiaoju
 * @date 2020-06-16 11:33
 */
@SpringBootTest
public class ToolsTest {


//    @Autowired
//    private CodeForcesSubmitter submitter;

    @Test
    public void regTest(){

//        Submission submission = new Submission();
//        submission.setSourceCode("#include<bits/stdc++.h>\n" +
//                "using namespace std;\n" +
//                "int main() {\n" +
//                "\tint n;\n" +
//                "\tint a,b,c,d;\n" +
//                "\tcin >> n;\n" +
//                "\tvector<int> v;\n" +
//                "\tint i, num,j;\n" +
//                "\tfor (i = 0; i < n; i++) {\n" +
//                "\t\tcin >> num;\n" +
//                "\t\tv.push_back(num);\n" +
//                "\t}\n" +
//                "\tint ans = 0;\n" +
//                "\tfor (i = 1; i <= 30; i++) {\n" +
//                "\t\tint tmp = 0;\n" +
//                "\t\tfor (auto val : v) {\n" +
//                "\t\t\ttmp += val;\n" +
//                "\t\t\tif (val > i)\n" +
//                "\t\t\t\ttmp = 0;\n" +
//                "\t\t\ttmp = max(tmp, 0);\n" +
//                "\t\t\tans = max(ans, tmp - i);\n" +
//                "\t\t}\n" +
//                "\t}\n" +
//                "\tcout << ans << endl;\n" +
//                "\treturn 0;\n" +
//                "}  ");
//        submission.setLanguage("54");
//        submitter.setSubmission(submission);
//        submitter.work();
//        System.out.println(submission);
    }

    class Student{
        private String str;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }

    @Test
    public void test(){
//        Compilation Error compilation
        System.out.println("Compilation Error".toLowerCase().contains("compilation"));
        String test="ABCD";
        Student student = new Student();
        student.setStr(test);
        test.toLowerCase();
        System.out.println(student.getStr());
    }
}
