package me.oczi.common.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.File;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;

/**
 * A variety of commons utils used in the plugin.
 */
public interface CommonsUtils {

  /**
   * Regex to check another regex.
   */
  Pattern regexStringFormat = Pattern.compile("(%[0-9]*(s|d|n))");
  /**
   * Regex to check if it is an integer or float.
   */
  Pattern regexNumber = Pattern.compile("-?\\d+(\\.\\d+)?");
  /**
   * Regex to check if it is a boolean.
   */
  Pattern regexBoolean = Pattern.compile("\\d+");

  /**
   * Check if enum class contains a enum with first argument.
   * @param enumName Enum name to check.
   * @param clazz Class of enum.
   * @param <E> Enum type.
   * @return result of check.
   */
  static <E extends Enum<E>> boolean enumExist(String enumName, Class<E> clazz) {
    for (E enumConstant : clazz.getEnumConstants()) {
      if (enumConstant.name().equalsIgnoreCase(enumName)) {
        return true;
      }
    }
    return false;
  }

  static boolean isClassLoaded(String uri) {
    try {
      Class.forName(uri);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  /**
   * Format all "." to "/" for urls.
   * @param string String to format.
   * @return String formatted.
   */
  static String formatUrl(String string) {
    return string.replace(".", "/");
  }

  /**
   * Map the files of a path.
   * @param path Path to map.
   * @return map of files in path.
   */
  static Map<String, File> mapPath(String path) {
    return mapPath(new File(path));
  }

  /**
   * Map the files of a path.
   * @param path Path to map.
   * @return map of files in path.
   */
  static Map<String, File> mapPath(File path) {
    File[] arrayFiles = path.listFiles();
    if (arrayFiles == null) {
      return Collections.emptyMap();
    }

    Map<String, File> fileMap = new HashMap<>(arrayFiles.length);
    for (File file : arrayFiles) {
      fileMap.put(file.getName(), file);
    }

    return fileMap;
  }

  /**
   * Array of strings to a single string.
   * @param strings Strings to single string.
   * @return Array joined or first value of array.
   */
  static String arrayToString(String[] strings) {
    if (strings.length == 1) {
      return strings[0];
    } else if (strings.length > 1){
      return String.join(", ", strings);
    } else {
      return "";
    }
  }

  /**
   * Array to search a argument.
   * @param array Array to iterate.
   * @param match Object to search.
   * @return result of search.
   */
  static <T> boolean arrayContains(T[] array, T match) {
    return Arrays
        .stream(array)
        .anyMatch((t) -> t == match);
  }

  /**
   * Check is string contains a space.
   * @param str String to check.
   * @return result of check.
   */
  static boolean containsSpace(String str) {
    for (char c : str.toCharArray())
      if (!Character.isSpaceChar(c)) { return true; }
    return false;
  }

  /**
   * Check is string contains a regex.
   * @param str String to check.
   * @return result of check.
   */
  static boolean containsRegex(String str) {
    return regexStringFormat.matcher(str).matches();
  }

  /**
   * Apply {@link String#equalsIgnoreCase(String)} to
   * all the elements of array with first parameter.
   * @param match - String to compare
   * @param strings - array to iterate
   * @return is equals
   */
  static boolean stringEqualsTo(String match, String... strings) {
    for (String var : strings)
      if (match.equalsIgnoreCase(var)) { return true; }

    return false;
  }

  /**
   * Apply {@link String#equalsIgnoreCase(String)} to
   * all the elements of iterable with first parameter.
   * @param match - String to compare
   * @param strings - Iterable to iterate
   * @return is equals
   */
  static boolean stringEqualsTo(String match, Iterable<String> strings) {
    for (String var : strings)
      if (match.equalsIgnoreCase(var)) { return true; }

    return false;
  }

  /**
   * Check is object equals to any object of array.
   * @param object Object to compare with array.
   * @param objects Array of objects to compare.
   * @param <T> Type.
   * @return is equals
   */
  @SafeVarargs
  static <T> boolean equalsTo(T object, T... objects) {
    for (T t : objects)
      if (object == t) { return true; }

    return false;
  }

  /**
   * Check is object equals to any object of iterable.
   * @param object Object to compare with iterable.
   * @param objects Iterable of objects to compare.
   * @param <T> Type.
   * @return is equals
   */
  static <T> boolean equalsTo(T object, Iterable<T> objects) {
    for (T t : objects)
      if (object == t) { return true; }

    return false;
  }

  /**
   * Check if any string is null or empty
   * Used for plural cases.
   * @param strings - Strings to check.
   * @return is null or empty.
   */
  static boolean isNullOrEmpty(String... strings) {
    for (String var : strings)
      if (isNullOrEmpty(var)) { return true; }
    return false;
  }

  /**
   * Check is string null or empty.
   * @param str String to check
   * @return is null or empty.
   */
  static boolean isNullOrEmpty(String str) {
    return str == null || str.isEmpty();
  }

  /**
   * Check if a collection is null or empty.
   * @param objects - Objects to check.
   * @return is mull or empty.
   */
  static boolean isNullOrEmpty(Object... objects) {
    return objects == null || objects.length == 0;
  }


  /**
   * Check if a collection is null or empty.
   * @param collection - Collection to check
   * @return is Null or empty
   */
  static boolean isNullOrEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * Check if a map is null or empty.
   * @param map - Collection to check
   * @return is Null or empty
   */
  static boolean isNullOrEmpty(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  /**
   * A dirty trick to repeat
   * Strings and {@link String#join} them.
   * @param string - String to repeat.
   * @param charSequence - CharSequence to join the result.
   * @param repeat - Number of repeats for string.
   * @return String repeated joined by CharSequence.
   */
  static String joinRepeatedString(String string,
                                   CharSequence charSequence,
                                   int repeat) {
    if (repeat <= 1) {
      return string;
    }
    String[] strings = new String[repeat];
    for (int i = 0; i < repeat; i++) {
      strings[i] = string;
    }
    return String.join(charSequence, strings);
  }

  /**
   * {@link String#join}-alike
   * for iterables of any type.
   * @param iterable - Iterable to join
   * @return Collection joined as String
   */
  static <E> String joinIterable(Iterable<E> iterable) {
    return joinIterable(", ", iterable);
  }

  /**
   * {@link String#join}-alike
   * for iterables of any type.
   * @param iterable - Iterable to join
   * @return Collection joined as String
   */
  static <E> String joinIterable(String charSequence,
                                 Iterable<E> iterable) {
    StringBuilder builder = new StringBuilder();
    for (Object object : iterable) {
      if (builder.length() > 0) {
        builder.append(charSequence);
      }
      builder.append(object);
    }

    return builder.toString();
  }

  /**
   * Check if string parameters is null or empty,
   * and if is, will throw NullPointerException.
   * @param strings - Strings to check
   * @exception NullPointerException - If found a String empty or null.
   */
  static void checkStrings(String message, String... strings) {
    if (isNullOrEmpty(strings)) {
      throw new NullPointerException(message);
    }
  }

  /**
   * Join all the elements of collection into parentheses.
   * @param collection Collection to join.
   * @param <E> Type of elements.
   * @return Collection joined into parentheses.
   */
  static <E> String parenthesesString(Collection<E> collection) {
    return !isNullOrEmpty(collection)
        ? parenthesesString(
            joinIterable(collection))
        : "";
  }

  /**
   * Join all the elements of array into parentheses.
   * @param objects Array to join.
   * @param <T> Type of elements.
   * @return Collection joined into parentheses.
   */
  @SafeVarargs
  static <T> String parenthesesString(T... objects) {
    return objects.length > 0
        ? parenthesesString(
            Joiner.on(", ")
                .join(objects))
        : "";
  }

  /**
   * Return String into parentheses.
   * @param string - String to enclose.
   * @return String into Parentheses.
   */
  static String parenthesesString(String string) {
    return middleSplit("()", string);
  }

  static String middleSplit(String split, String string) {
    int mid = split.length() / 2;
    String[] splitter = {
        split.substring(0, mid),
        split.substring(mid)};
    return splitter[0] + string + splitter[1];
  }

  /**
   * Replace all the placeholders with the inserted objects.
   * <br>NOTE: If there are more placeholders than objects to replace,
   * there will be placeholders without replacing them.<br/>
   * @param message - Message with placeholders unmodified
   * @param objects - Replacement objects
   */
  static String format(String message,
                       Object... objects) {
    return format(message, "%s", objects);
  }

  /**
   * Replace all the placeholders with the inserted objects.
   * <br>NOTE: If there are more placeholders than objects to replace,
   * there will be placeholders without replacing them.<br/>
   * @param message - Message with placeholders unmodified
   * @param objects - Replacement objects
   */
  static String formatPlaceholder(String message,
                                  Object... objects) {
    return format(message, "{%s}", objects);
  }

  /**
   * Replace all the placeholders with the inserted objects.
   * <br>NOTE: If there are more placeholders than objects to replace,
   * there will be placeholders without replacing them.<br/>
   * @param message - Message with placeholders unmodified
   * @param format - Format concatenated to object number.
   * @param objects - Replacement objects
   */
  static String format(String message,
                       String format,
                       Object... objects) {
    for (int i = 0; i < objects.length; i++) {
      String placeholder = String.format(format, i);
      String o = String.valueOf(objects[i]);
      message = message.replace(placeholder, o);
    }

    return message;
  }

  /**
   * Format a UUID string without dashes
   * to a complete UUID.
   * @param notFormat UUID string to format.
   * @return UUID.
   */
  static UUID formatUuid(String notFormat) {
    BigInteger decimal1 = new BigInteger(
        notFormat.substring(0, 16), 16);
    BigInteger decimal2 = new BigInteger(
        notFormat.substring(16, 32), 16);
    return new UUID(decimal1.longValue(), decimal2.longValue());
  }

  static int filterIntegers(String string) {
    List<String> list = new ArrayList<>();
    for (Character c : string.toCharArray()) {
      if (Character.isDigit(c)) {
        list.add(c.toString());
      }
    }
    return Integer.parseInt(
        String.join("", list));
  }

  /**
   * Iterate all the elements
   * to format with the next value.
   * @param strings Strings to format in cycle.
   * @return A string formatted.
   */
  static String cyclicFormat(Iterable<String> strings) {
    return cyclicFormat(strings, false);
  }

  /**
   * Iterate all the elements
   * to format with the next value.
   * @param strings Strings to format in cycle.
   * @param endWithS End with %s.
   * @return A string formatted.
   */
  static String cyclicFormat(Iterable<String> strings, boolean endWithS) {
    String base = "";
    for (String string : strings) {
      if (base.isEmpty()) {
        base = string;
      } else {
        if (base.contains("%s")) {
          base = String.format(base, string);
        } else {
          throw new IllegalArgumentException("A string without %s broke the cycle.");
        }
      }
    }

    if (endWithS && !base.contains("%s")) {
      base += "%s";
    }
    return base;
  }

  // Method used to not break the api if i remove Google Guava.
  /**
   * Part list in I SubLists.
   * @param list List to part.
   * @param i Number to part the list.
   * @param <E> Element type.
   * @return SubLists.
   */
  static <E> List<List<E>> partitionList(List<E> list,
                                         int i) {
    return Lists.partition(list, i);
  }

  /**
   * Parse a string to boolean.
   * @param str String to boolean.
   * @return Boolean.
   */
  static boolean parseBoolean(String str) {
    if (regexBoolean.matcher(str).matches()) {
      int i = Integer.parseInt(str);
      return parseBoolean(i);
    }

    return Boolean.parseBoolean(str);
  }

  /**
   * Parse a integer to boolean.
   * @param intToBoolean Integer to boolean.
   * @return Boolean.
   */
  static boolean parseBoolean(int intToBoolean) {
    return intToBoolean == 1;
  }

  /**
   * Parse a boolean to integer.
   * @param toInt Boolean to integer.
   * @return Integer..
   */
  static int parseInt(boolean toInt) {
    return toInt ? 1 : 0;
  }

  /**
   * Parse a string to integer safety.
   * @param toInt String to integer.
   * @return String to integer or 0.
   */
  static int parseInt(String toInt) {
    return !Strings.isNullOrEmpty(toInt) &&
           regexNumber.matcher(toInt).matches()
        ? Integer.parseInt(toInt)
        : 0;
  }

  /**
   * Check is string contains only numbers.
   * @param string String to check.
   * @return result of check.
   */
  static boolean isNumeric(String string) {
    return regexNumber.matcher(string).matches();
  }

  /**
   * Check is string contains a number.
   * @param string String to check.
   * @return result of check.
   */
  static boolean findNumeric(String string) {
    return regexNumber.matcher(string).find();
  }
}
