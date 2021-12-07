package other.testcases;

public class StringExp {

  public static void main(String[] args) {

    // String str = "a d, m, i.n";
    // String delimiters = "\\s+|,\\s*|\\.\\s*";

    // String str = "Mobility Bill 5 3 7 5 2 1 8 5 5 , Amount due, CR $0.02";
    String str = "Mobility Bill 5 3 7 5 2 1 8 5 5 , Amount due, CR $0.01";
    // String str = "Total monthly charges (before taxes) 50dollars per month";
    // String str = "$294.55";

    // One Bill 2 7 0 0 7 6 2 2 7 , Amount due, CR $6.72

    System.out.println(str.split(",")[0]);


    // System.out.println(str.replaceAll("[a-zA-Z]|\\W|\\s+", ""));//50

    // System.out.println(str.replaceAll("^[a-zA-Z$]|\\s+", ""));

    // int i = Integer.parseInt(str.replaceAll("[a-zA-Z$]|\\s+", ""));

    // System.out.println(Float.parseFloat(str.replaceAll("^[a-zA-Z$]|\\s+", "")));


    // System.out.println(strs


  }

}
