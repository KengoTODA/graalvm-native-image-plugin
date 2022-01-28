package org.mikeneck.graalvm.config;

import java.util.Arrays;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BundleUsage implements Comparable<BundleUsage> {

  @NotNull public String name = "";
  @Nullable public String[] classNames;
  @Nullable public String[] locales;

  public BundleUsage() {}

  BundleUsage(@NotNull String name) {
    this(name, null, null);
  }

  BundleUsage(@NotNull String name, @Nullable String[] classNames, @Nullable String[] locales) {
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
    if (!Arrays.equals(classNames, that.classNames)) return false;
    return Arrays.equals(locales, that.locales);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + Arrays.hashCode(classNames);
    result = 31 * result + Arrays.hashCode(locales);
    return result;
  }

  @Override
  public String toString() {
    return "BundleUsage{"
        + "name='"
        + name
        + '\''
        + ", classNames="
        + Arrays.toString(classNames)
        + ", locales="
        + Arrays.toString(locales)
        + '}';
  }

  @Override
  public int compareTo(@NotNull BundleUsage that) {
    return Comparator.comparing((BundleUsage u) -> u.name)
        .thenComparing((BundleUsage u) -> u.classNames, Arrays::compare)
        .thenComparing((BundleUsage u) -> u.locales, Arrays::compare)
        .compare(this, that);
  }
}
