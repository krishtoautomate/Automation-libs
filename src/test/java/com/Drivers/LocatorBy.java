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

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LocatorBy extends By {

    private final List<By> bys;

    private LocatorBy(List<By> bys) {
        this.bys = bys;
    }

    public static By all(By... bys) {
        return new LocatorBy(Arrays.asList(bys));
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        for (By by : bys) {
            try {
                List<WebElement> elements = by.findElements(context);
                if (!elements.isEmpty()) {
                    return elements;
                }
            } catch (Exception e) {
                // continue
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return bys.stream().map(By::toString).collect(Collectors.joining(", "));
    }

}


