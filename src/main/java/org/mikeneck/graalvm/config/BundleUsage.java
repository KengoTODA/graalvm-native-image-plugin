package org.mikeneck.graalvm.config;

import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BundleUsage implements Comparable<BundleUsage> {

  @NotNull public String name = "";
  @Nullable public List<String> classNames;
  @Nullable public List<String> locales;

  public BundleUsage() {}

  BundleUsage(@NotNull String name) {
    this(name, null, null);
  }

  BundleUsage(
      @NotNull String name, @Nullable List<String> classNames, @Nullable List<String> locales) {
    this.name = name;
    this.classNames = classNames;
    this.locales = locales;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BundleUsage that = (BundleUsage) o;
    if (!name.equals(that.name)) return false;
    if (!Objects.equals(classNames, that.classNames)) return false;
    return Objects.equals(locales, that.locales);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + Objects.hashCode(classNames);
    result = 31 * result + Objects.hashCode(locales);
    return result;
  }

  @Override
  public String toString() {
    return "BundleUsage{"
        + "name='"
        + name
        + '\''
        + ", classNames="
        + classNames
        + ", locales="
        + locales
        + '}';
  }

  @Override
  public int compareTo(@NotNull BundleUsage o) {
    int nameResult = this.name.compareTo(o.name);
    if (nameResult != 0) {
      return nameResult;
    }
    if (o.classNames == null) {
      return 1;
    } else if (classNames == null) {
      return -1;
    }
    Iterator<String> iterator = o.classNames.iterator();
    for (String className : classNames) {
      if (!iterator.hasNext()) {
        return 1;
      }
      String other = iterator.next();
      int current = className.compareTo(other);
      if (current != 0) {
        return current;
      }
    }
    if (iterator.hasNext()) {
      return -1;
    }
    if (o.locales == null) {
      return 1;
    } else if (locales == null) {
      return -1;
    }
    iterator = o.locales.iterator();
    for (String locale : locales) {
      if (!iterator.hasNext()) {
        return 1;
      }
      String other = iterator.next();
      int current = locale.compareTo(other);
      if (current != 0) {
        return current;
      }
    }
    if (iterator.hasNext()) {
      return -1;
    }
    return 0;
  }
}
