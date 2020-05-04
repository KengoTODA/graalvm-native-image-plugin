/*
 * Copyright 2020 Shinya Mochida
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.graalvm;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.internal.provider.DefaultProvider;

class OutputDirectoryProvider extends DefaultProvider<File> {

    static <T> UnaryOperator<T> outputDirectoryIsNotNull() {
        return object -> {
            if (object == null) throw new InvalidUserDataException("outputDirectory is null");
            return object;
        };
    }

    static UnaryOperator<File> outputDirectoryIsNotExistingFile() {
        return file -> {
            if (file.exists() && file.isFile()) {
                throw new InvalidUserDataException(String.format("outputDirectory(%s) is file", file));
            }
            return file;
        };
    }

    static UnaryOperator<Path> outputDirectoryIsNotExistingFilePath() {
        return path -> {
            if (Files.exists(path) && Files.isRegularFile(path)) {
                throw new InvalidUserDataException(String.format("outputDirectory(%s) is file", path));
            }
            return path;
        };
    }

    @SafeVarargs
    private static <T> Function<T, File> converter(Function<T, File> toFile, UnaryOperator<T>... validation) {
        UnaryOperator<T> operator = Arrays.stream(validation)
                .reduce(object -> object, (left, right) -> obj -> left.andThen(right).apply(obj));
        return operator.andThen(toFile);
    }

    @SafeVarargs
    <T> OutputDirectoryProvider(T object, Function<T, File> toFile, UnaryOperator<T>... validation) {
        super(() -> converter(toFile, validation).apply(object));
    }

    static OutputDirectoryProvider ofFile(File nullable) {
        return new OutputDirectoryProvider(nullable, file -> file, outputDirectoryIsNotNull(), outputDirectoryIsNotExistingFile());
    }

    static OutputDirectoryProvider ofPath(Path nullable) {
        return new OutputDirectoryProvider(nullable, Path::toFile, outputDirectoryIsNotNull(), outputDirectoryIsNotExistingFilePath());
    }
}
