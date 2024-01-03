/*
 *  Copyright 2023 The original authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package dev.morling.onebrc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

import static java.lang.Double.parseDouble;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingDouble;

public class CalculateAverage_remi {
    private static double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    public static void main(String[] args) throws IOException {
        record Measure(String city, double value) { }
        var map =
            Files.lines(Path.of("./measurements.txt"))
                .parallel()
                .map(line -> {
                    var index = line.indexOf(';');
                    return new Measure(line.substring(0, index), parseDouble(line.substring(index + 1)));
                })
                .collect(groupingBy(Measure::city, TreeMap::new, collectingAndThen(summarizingDouble(Measure::value),
                    stats -> round(stats.getMin()) + "/" + round(stats.getAverage()) + "/" + round(stats.getMax()))));
        System.out.println(map);
    }
}
