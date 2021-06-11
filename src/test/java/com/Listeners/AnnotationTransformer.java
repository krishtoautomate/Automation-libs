package com.Listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Created by Krish on 17.10.2018.
 */
public class AnnotationTransformer implements IAnnotationTransformer {

  @Override
  public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
      Method testMethod) {
    annotation.setRetryAnalyzer(Retry.class);
  }
}
