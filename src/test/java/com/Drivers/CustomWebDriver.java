// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.Drivers;

import com.google.common.collect.ImmutableSet;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.internal.Require;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.tracing.TracedHttpClient;
import org.openqa.selenium.remote.tracing.Tracer;
import org.openqa.selenium.remote.tracing.opentelemetry.OpenTelemetryTracer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.openqa.selenium.remote.CapabilityType.LOGGING_PREFS;

@Augmentable
public class CustomWebDriver extends RemoteWebDriver {

    // TODO: This static logger should be unified with the per-instance localLogs
    private static final Logger logger = Logger.getLogger(RemoteWebDriver.class.getName());
    private Level level = Level.FINE;
    private ErrorHandler errorHandler = new ErrorHandler();
    private CommandExecutor executor;
    private Capabilities capabilities;
    private SessionId sessionId;
    private FileDetector fileDetector = new UselessFileDetector();
    private ExecuteMethod executeMethod;

    private JsonToWebElementConverter converter;

    private Logs remoteLogs;
    private LocalLogs localLogs;

    // For cglib
    protected CustomWebDriver() {
        this.capabilities = init(new ImmutableCapabilities());
    }

    public CustomWebDriver(Capabilities capabilities) {
        this(getDefaultServerURL(),
                Require.nonNull("Capabilities", capabilities), true);
    }

    public CustomWebDriver(Capabilities capabilities, boolean enableTracing) {
        this(getDefaultServerURL(),
                Require.nonNull("Capabilities", capabilities), enableTracing);
    }

    public CustomWebDriver(URL remoteAddress, Capabilities capabilities) {
        this(createExecutor(Require.nonNull("Server URL", remoteAddress), true),
                Require.nonNull("Capabilities", capabilities));
    }

    public CustomWebDriver(URL remoteAddress, Capabilities capabilities, boolean enableTracing) {
        this(createExecutor(Require.nonNull("Server URL", remoteAddress), enableTracing),
                Require.nonNull("Capabilities", capabilities));
    }

    public CustomWebDriver(CommandExecutor executor, Capabilities capabilities) {
        this.executor = Require.nonNull("Command executor", executor);
        capabilities = init(capabilities);

        if (executor instanceof NeedsLocalLogs) {
            ((NeedsLocalLogs) executor).setLocalLogs(localLogs);
        }

        try {
            startSession(capabilities);
        } catch (RuntimeException e) {
            try {
                quit();
            } catch (Exception ignored) {
                // Ignore the clean-up exception. We'll propagate the original failure.
            }

            throw e;
        }
    }

    private static CommandExecutor createExecutor(URL remoteAddress, boolean enableTracing) {
        ClientConfig config = ClientConfig.defaultConfig().baseUrl(remoteAddress);
        if (enableTracing) {
            Tracer tracer = OpenTelemetryTracer.getInstance();
            CommandExecutor executor = new HttpCommandExecutor(
                    Collections.emptyMap(),
                    config,
                    new TracedHttpClient.Factory(tracer, HttpClient.Factory.createDefault()));
            return new TracedCommandExecutor(executor, tracer);
        } else {
            return new HttpCommandExecutor(config);
        }
    }

    private static URL getDefaultServerURL() {
        try {
            return new URL(System.getProperty("webdriver.remote.server", "http://localhost:4444/"));
        } catch (MalformedURLException e) {
            throw new WebDriverException(e);
        }
    }

    private Capabilities init(Capabilities capabilities) {
        capabilities = capabilities == null ? new ImmutableCapabilities() : capabilities;

        logger.addHandler(LoggingHandler.getInstance());

        converter = new JsonToWebElementConverter(this);
        executeMethod = new RemoteExecuteMethod(this);

        ImmutableSet.Builder<String> builder = new ImmutableSet.Builder<>();

        boolean isProfilingEnabled = capabilities.is(CapabilityType.ENABLE_PROFILING_CAPABILITY);
        if (isProfilingEnabled) {
            builder.add(LogType.PROFILER);
        }

        LoggingPreferences mergedLoggingPrefs = new LoggingPreferences();
        mergedLoggingPrefs.addPreferences((LoggingPreferences) capabilities.getCapability(LOGGING_PREFS));

        if (!mergedLoggingPrefs.getEnabledLogTypes().contains(LogType.CLIENT) ||
                mergedLoggingPrefs.getLevel(LogType.CLIENT) != Level.OFF) {
            builder.add(LogType.CLIENT);
        }

        Set<String> logTypesToInclude = builder.build();

        LocalLogs performanceLogger = LocalLogs.getStoringLoggerInstance(logTypesToInclude);
        LocalLogs clientLogs = LocalLogs.getHandlerBasedLoggerInstance(
                LoggingHandler.getInstance(), logTypesToInclude);
        localLogs = LocalLogs.getCombinedLogsHolder(clientLogs, performanceLogger);
        remoteLogs = new RemoteLogs(executeMethod, localLogs);

        return capabilities;
    }

}
