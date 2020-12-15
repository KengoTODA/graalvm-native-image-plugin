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
package org.mikeneck.graalvm.config.task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public interface FileOutput {

    OutputStream newOutputStream() throws IOException;

    default void withOutputStream(@NotNull OutputStreamOperation operation) throws IOException {
        try (OutputStream outputStream = newOutputStream()) {
            UnCloseableOutputStream unCloseableOutputStream = 
                    UnCloseableOutputStream.delegateTo(outputStream);
            operation.consume(unCloseableOutputStream);
        }
    }

    @NotNull
    static FileOutput to(@NotNull File file) {
        return to(file.toPath());
    }

    @NotNull
    static FileOutput to(@NotNull Supplier<Path> directory, @NotNull String fileName) {
        return () -> Files.newOutputStream(
                directory.get().resolve(fileName),
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    @NotNull
    static FileOutput to(@NotNull Path file) {
        return new FileOutputImpl(file);
    }
}
