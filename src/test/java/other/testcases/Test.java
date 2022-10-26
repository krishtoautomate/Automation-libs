package other.testcases;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {


        String[][] names = new String[3][2];
//        String[][] names = { {"Sam", "Smith"}, {"Robert", "Delgro"}, {"James", "Gosling"}, };

        names [0][0] = "Test1";
        names [0][1] = "one";

        names [1][0] = "Test1";
        names [1][1] = "two";

        names [2][0] = "Test3";
        names [2][1] = "three";

        System.out.println("answer : "+ Arrays.deepToString(names));

//        System.out.println("answer : "+names [0][0]+ ":" + names [0][1]);
//        System.out.println("answer : "+names [1][0]+ ":" + names [1][1]);





    }

}
