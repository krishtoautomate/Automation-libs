package com.Driver;

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

import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Default Appium driver implementation.
 */
public class MobileDriver extends RemoteWebDriver {

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) {
        // TODO: Eventually we should not override this method.
        // TODO: Although, we have a legacy burden,
        // TODO: so it's impossible to do it the other way as of Oct 29 2022.
        // TODO: See https://github.com/SeleniumHQ/selenium/issues/11168
        return super.getScreenshotAs(new OutputType<X>() {
            @Override
            public X convertFromBase64Png(String base64Png) {
                String rfc4648Base64 = base64Png.replaceAll("\\r?\\n", "");
                return outputType.convertFromBase64Png(rfc4648Base64);
            }

            @Override
            public X convertFromPngBytes(byte[] png) {
                return outputType.convertFromPngBytes(png);
            }
        });
    }
}
