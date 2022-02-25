package testUI.Utils;

public class TestUIException extends RuntimeException {
  public TestUIException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public TestUIException(String errorMessage) {
    super(errorMessage);
    // putLogError(errorMessage);
  }

  public static void handleError(String message) {
    // if (Configuration.softAsserts) Logger.putSoftAssert(message);
    // else throw new TestUIException(message);
  }
}
