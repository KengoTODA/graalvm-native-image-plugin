/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.mikeneck.graalvm;

import java.util.stream.Collectors;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

public class GraalvmNativeImagePlugin implements Plugin<Project> {

    public void apply(@NotNull Project project) {
        NativeImageExtension nativeImageExtension = new NativeImageExtension(project);
        project.getExtensions().add("nativeImage", nativeImageExtension);

        TaskContainer taskContainer = project.getTasks();

        InstallNativeImageTask installNativeImageTask = taskContainer.create(
                "installNativeImage", InstallNativeImageTask.class, project);
        installNativeImageTask.setExtension(nativeImageExtension);
        installNativeImageTask.setDescription("Installs native-image command by graalVm Updater command");
        installNativeImageTask.setGroup("graalvm");

        taskContainer.create("nativeImage", NativeImageTask.class, task -> {
            task.setExtension(nativeImageExtension);
            task.dependsOn("jar");
            task.setDescription("Creates native executable");
            task.setGroup("graalvm");
            task.dependsOn(installNativeImageTask);
        });

        GenerateNativeImageConfigTask nativeImageConfigFiles =
                taskContainer.create(
                        "nativeImageConfigFiles",
                        GenerateNativeImageConfigTask.class,
                        project);
        nativeImageConfigFiles.dependsOn("classes");
        nativeImageConfigFiles.setDescription("Generates native image config json files via test run.");
        nativeImageConfigFiles.setGroup("graalvm");
        nativeImageConfigFiles.setNativeImageExtension(nativeImageExtension);

        MergeNativeImageConfigTask mergeNativeImageConfig = 
                taskContainer.create(
                        "mergeNativeImageConfig",
                        MergeNativeImageConfigTask.class,
                        project);
        mergeNativeImageConfig.destinationDir(project.getBuildDir().toPath().resolve("native-image-config"));
        Provider<FileCollection> configDirs = project.provider(() -> nativeImageConfigFiles
                .getJavaExecutions()
                .stream()
                .map(exec -> exec.outputDirectory)
                .collect(Collectors.toList()))
                .map(project::files);
        mergeNativeImageConfig.fromDirectories(configDirs);
        mergeNativeImageConfig.setGroup("graalvm");
        mergeNativeImageConfig.setDescription("Merge native image config json files into one file.");
        mergeNativeImageConfig.dependsOn(nativeImageConfigFiles);

        taskContainer.create("generateNativeImageConfig", task -> {
            task.setGroup("graalvm");
            task.setDescription("Generates native image config json files.");
            task.dependsOn(nativeImageConfigFiles, mergeNativeImageConfig);
        });
    }
}
