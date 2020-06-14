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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class FileOutputImpl implements FileOutput {

    private final Path file;

    FileOutputImpl(Path file) {
        this.file = file;
    }

    @Override
    public OutputStream newOutputStream() throws IOException {
        return Files.newOutputStream(
                file,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public String toString() {
        @SuppressWarnings("StringBufferReplaceableByString") 
        final StringBuilder sb = new StringBuilder("FileOutputImpl{");
        sb.append(file);
        sb.append('}');
        return sb.toString();
    }
}
