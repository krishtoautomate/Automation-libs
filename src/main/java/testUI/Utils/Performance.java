package testUI.Utils;

import java.util.ArrayList;
import java.util.List;

public class Performance {
  private static List<Long> time = new ArrayList<>();

  public static void setTime(long time) {
    Performance.time.add(time);
  }

  public static List<Long> getListOfCommandsTime() {
    return Performance.time;
  }

  public static void logAverageTime() {
    long total = 0;
    for (long t : time) {
      total += t;
    }
    if (time.size() != 0) {
      time.size();
    }
  }
}
