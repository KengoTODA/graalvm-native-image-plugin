package org.mikeneck.graalvm.config.task;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

@ExtendWith(ConfigFileProvidersTest.FileProvider.class)
class ConfigFileProvidersTest {

  static class FileProvider implements ParameterResolver, AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
      ExtensionContext.Store store =
          context.getStore(ExtensionContext.Namespace.create(FileProvider.class));
      String methodName = context.getRequiredTestMethod().getName();
      Path path = store.remove(methodName, Path.class);
      if (path == null) {
        return;
      }
      Files.walkFileTree(
          path,
          new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException {
              Files.deleteIfExists(file);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                throws IOException {
              Files.deleteIfExists(dir);
              return FileVisitResult.CONTINUE;
            }
          });
      Files.deleteIfExists(path);
    }

    @Override
    public boolean supportsParameter(
        ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
      return parameterContext.getParameter().getType().equals(Path.class);
    }

    @Override
    public Object resolveParameter(
        ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
      ExtensionContext.Store store =
          extensionContext.getStore(ExtensionContext.Namespace.create(FileProvider.class));
      String methodName = extensionContext.getRequiredTestMethod().getName();
      return store.getOrComputeIfAbsent(
          methodName,
          (key) -> {
            long now = System.currentTimeMillis();
            try {
              return Files.createTempDirectory(String.format("config-file-providers-test-%d", now))
                  .toAbsolutePath();
            } catch (IOException e) {
              throw new UncheckedIOException(
                  String.format("failed to create file[config-file-providers-test-%d]", now), e);
            }
          },
          Path.class);
    }
  }
}
