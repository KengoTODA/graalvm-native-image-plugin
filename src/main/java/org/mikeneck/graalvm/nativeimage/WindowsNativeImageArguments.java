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
package org.mikeneck.graalvm.nativeimage;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.Directory;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.bundling.Jar;
import org.jetbrains.annotations.NotNull;

class WindowsNativeImageArguments implements NativeImageArguments {

    private static final char DOUBLE_QUOT = '"';

    private final NativeImageArguments delegate;

    WindowsNativeImageArguments(NativeImageArguments delegate) {
        this.delegate = delegate;
    }

    @NotNull
    private String wrapValue(String value) {
        return String.format("%s%s%s", DOUBLE_QUOT, value, DOUBLE_QUOT);
    }

    @NotNull
    @Override
    public String classpath() {
        return wrapValue(delegate.classpath());
    }

    @NotNull
    @Override
    public String outputPath() {
        return wrapValue(delegate.outputPath());
    }

    @NotNull
    @Override
    public Optional<String> executableName() {
        return delegate.executableName().map(this::wrapValue);
    }

    @NotNull
    @Override
    public List<String> additionalArguments() {
        return delegate.additionalArguments()
                .stream()
                .map(this::wrapValue)
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public String mainClass() {
        return wrapValue(delegate.mainClass());
    }

    @Override
    public void setRuntimeClasspath(@NotNull Provider<Configuration> runtimeClasspath) {
        delegate.setRuntimeClasspath(runtimeClasspath);
    }

    @Override
    public void setMainClass(@NotNull Provider<String> mainClass) {
        delegate.setMainClass(mainClass);
    }

    @Override
    public void addJarFile(@NotNull File jarFile) {
        delegate.addJarFile(jarFile);
    }

    @Override
    public void addJarFile(@NotNull Provider<File> jarFile) {
        delegate.addJarFile(jarFile);
    }

    @Override
    public void addJarFile(@NotNull Jar jar) {
        delegate.addJarFile(jar);
    }

    @Override
    public void setOutputDirectory(@NotNull Provider<Directory> outputDirectory) {
        delegate.setOutputDirectory(outputDirectory);
    }

    @Override
    public void setExecutableName(@NotNull Provider<String> executableName) {
        delegate.setExecutableName(executableName);
    }

    @Override
    public void addArguments(@NotNull Provider<Iterable<String>> arguments) {
        delegate.addArguments(arguments);
    }

    @NotNull
    @Nested
    public NativeImageArguments getDelegate() {
        return delegate;
    }
}
