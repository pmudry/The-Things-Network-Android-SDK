package org.ttn.android.sdk.v0.domain;

import java.util.ArrayList;
import java.util.Collection;

/*
 * Copyright 2016 Fabio Tiriticco / Fabway
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by fabiotiriticco on 20/11/15.
 *
 */
@Deprecated
public class CollectionContainer<T> {

    public Collection<T> mCollection = new ArrayList<>();

    @Override
    public String toString() {
        String output = "collection container:";
        for (T item : mCollection) {
            output += "\n  " + item.toString();
        }
        return output;
    }
}