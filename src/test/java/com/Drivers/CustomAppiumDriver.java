/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.Drivers;

import io.appium.java_client.*;
import io.appium.java_client.remote.AppiumCommandExecutor;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.ErrorHandler;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.html5.RemoteLocationContext;
import org.openqa.selenium.remote.http.HttpClient;

import java.net.URL;

/**
 * Default Appium driver implementation.
 */
public class CustomAppiumDriver extends AppiumDriver {

    private static final ErrorHandler errorHandler = new ErrorHandler(new ErrorCodesMobile(), true);
    protected final RemoteLocationContext locationContext;
    // frequently used command parameters
    private final URL remoteAddress;
    private final ExecuteMethod executeMethod;

    /**
     * Creates a new instance based on command {@code executor} and {@code capabilities}.
     *
     * @param executor     is an instance of {@link HttpCommandExecutor}
     *                     or class that extends it. Default commands or another vendor-specific
     *                     commands may be specified there.
     * @param capabilities take a look at {@link Capabilities}
     */
    public CustomAppiumDriver(HttpCommandExecutor executor, Capabilities capabilities) {
        super(executor, capabilities);
        this.executeMethod = new AppiumExecutionMethod(this);
        locationContext = new RemoteLocationContext(executeMethod);
        super.setErrorHandler(errorHandler);
        this.remoteAddress = executor.getAddressOfRemoteServer();
    }

    public CustomAppiumDriver(AppiumClientConfig clientConfig, Capabilities capabilities) {
        this(new AppiumCommandExecutor(MobileCommand.commandRepository, clientConfig), capabilities);
    }

    public CustomAppiumDriver(URL remoteAddress, Capabilities capabilities) {
        this(new AppiumCommandExecutor(MobileCommand.commandRepository, remoteAddress),
                capabilities);
    }

    public CustomAppiumDriver(URL remoteAddress, HttpClient.Factory httpClientFactory,
                              Capabilities capabilities) {
        this(new AppiumCommandExecutor(MobileCommand.commandRepository, remoteAddress,
                httpClientFactory), capabilities);
    }

    public CustomAppiumDriver(AppiumDriverLocalService service, Capabilities capabilities) {
        this(new AppiumCommandExecutor(MobileCommand.commandRepository, service),
                capabilities);
    }

    public CustomAppiumDriver(AppiumDriverLocalService service, HttpClient.Factory httpClientFactory,
                              Capabilities capabilities) {
        this(new AppiumCommandExecutor(MobileCommand.commandRepository, service, httpClientFactory),
                capabilities);
    }

    public CustomAppiumDriver(AppiumServiceBuilder builder, Capabilities capabilities) {
        this(builder.build(), capabilities);
    }

    public CustomAppiumDriver(AppiumServiceBuilder builder, HttpClient.Factory httpClientFactory,
                              Capabilities capabilities) {
        this(builder.build(), httpClientFactory, capabilities);
    }

    public CustomAppiumDriver(HttpClient.Factory httpClientFactory, Capabilities capabilities) {
        this(AppiumDriverLocalService.buildDefaultService(), httpClientFactory,
                capabilities);
    }

    public CustomAppiumDriver(Capabilities capabilities) {
        this(AppiumDriverLocalService.buildDefaultService(), capabilities);
    }

    public void findElementAndClick(By by) {
        WebElement element = findElement(by);
        element.click();
    }

    public void findElementAndTap(By by) {
        WebElement element = findElement(by);
        // Custom implementation for the tap() method
        actions().moveToElement(element).click().perform();
    }

    public AppiumDriver getDriverInstance() {
        return this;
    }

    public Actions actions() {
        Actions a = new Actions(this);
        return a;
    }

    @Override
    public WebElement findElement(By by) {
        WebElement element = super.findElement(by);
        // Custom logic for finding the element
        return element;
    }

    public void sendKeys(CharSequence... keysToSend) {
        actions().sendKeys(keysToSend);
    }

    public WebElement findElement(By by, String... message) {
        WebElement element = super.findElement(by);
        // Custom logic for finding the element
        return element;
    }

    @Override
    public void get(String url) {
        // Custom logic for navigating to a URL
        super.get(url);
    }

}
