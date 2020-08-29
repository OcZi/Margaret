package me.oczi.common.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

/**
 * A variety of commons utils used in plugin.
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

  static <E extends Enum<E>> boolean enumExist(String enumName, Class<E> clazz) {
    for (E enumConstant : clazz.getEnumConstants()) {
      if (enumConstant.name().equalsIgnoreCase(enumName)) {
        return true;
      }
    }
    return false;
  }

  static String formatUrl(String string) {
    return string.replace(".", "/");
  }


  static Map<String, File> mapPath(String path) {
    return mapPath(new File(path));
  }

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

  static String arrayToString(String[] params) {
    if (params.length == 1) {
      return params[0];
    } else if (params.length > 1){
      return String.join(", ", params);
    } else {
      return "";
    }
  }

  static boolean arrayContains(String[] array, String match) {
    return Arrays
        .stream(array)
        .anyMatch(element -> element.equalsIgnoreCase(match));
  }

  static boolean containsSpace(String string) {
    for (char c : string.toCharArray())
      if (!Character.isSpaceChar(c)) { return true; }
    return false;
  }

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
  static boolean equalsTo(String match, String... strings) {
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
  static boolean equalsTo(String match, Iterable<String> strings) {
    for (String var : strings)
      if (match.equalsIgnoreCase(var)) { return true; }

    return false;
  }

  /**
   * Check if string parameters is null or empty.
   * Used for plural cases.
   * @param strings - Strings to check
   * @return is Null or empty
   */
  static boolean isNullOrEmpty(String... strings) {
    for (String var : strings)
      if (isNullOrEmpty(var)) { return true; }
    return false;
  }

  static boolean isNullOrEmpty(String var) {
    return var == null || var.isEmpty();
  }

  /**
   * Check if a collection is null or empty.
   * @param objects - Objects to check
   * @return is Null or empty
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
   * A dirty trick.
   * @param stringRepeater - String to repeat by intRepeat.
   * @param stringSequence - String sequence to join the String Repeat.
   * @param intRepeat - Number of repeat String Repeat.
   * @return String formatted
   */
  static String joinRepeatedString(String stringRepeater,
                                   String stringSequence,
                                   int intRepeat) {
    if (intRepeat <= 1) { return stringRepeater; }
    String[] strings = new String[intRepeat];
    for (int i = 0; i < intRepeat; i++) {
      strings[i] = stringRepeater;
    }
    return String.join(stringSequence, strings);
  }

  /**
   * {@link String#join}-alike
   * for collections of any type.
   * @param collection - Collection to join
   * @return Collection joined as String
   */
  static <E> String joinCollection(Collection<E> collection) {
    return joinCollection(", ", collection);
  }

  static <E> String joinCollection(String charSequence,
                                   Collection<E> collection) {
    StringBuilder builder = new StringBuilder();
    for (Object object : collection) {
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

  static List<String> stringEnclosed(String enclose, String string, int i) {
    List<String> list = Lists.newArrayList();
    for (int j = 0; j < i; j++) {
      list.add(middleSplit(enclose, string));
    }
    return list;
  }

  static List<String> generateRegex(int i) {
    List<String> list = Lists.newArrayList();
    for (int j = 0; j < i; j++) { list.add("%s"); }
    return list;
  }

  static <E> String parenthesesString(E string, int i) {
    List<E> list = Lists.newArrayList();
    for (int j = 0; j < i; j++) { list.add(string); }
    return parenthesesString(list);
  }

  static <E> String parenthesesString(Collection<E> list) {
    return !list.isEmpty()
        ? parenthesesString(
            Joiner.on(", ").join(list.toArray())) : "";
  }

  @SafeVarargs
  static <T> String parenthesesString(T... strings) {
    return strings.length > 0
        ? parenthesesString(
            Joiner.on(", ").join(strings)) : "";
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
   * Cast all of the keys/values of Map to Object.
   * @param map - Map to cast
   * @return Map of key/value String
   */
  @Deprecated
  static Map<Object, Object> allMapToObject(Map<?, ?> map) {
    Map<Object, Object> newMap = new HashMap<>();

    for (Map.Entry<?, ?> entry : map.entrySet()) {
      newMap.put(entry.getKey(), entry.getValue());
    }

    return newMap;
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
      message = message.replace(placeholder, objects[i].toString());
    }

    return message;
  }

  static String cyclicFormat(Collection<String> strings) {
    return cyclicFormat(strings, false);
  }

  static String cyclicFormat(Collection<String> strings, boolean endWithS) {
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

  static boolean parseBoolean(String str) {
    if (regexBoolean.matcher(str).matches()) {
      int i = Integer.parseInt(str);
      return parseBoolean(i);
    }

    return Boolean.parseBoolean(str);
  }

  static boolean parseBoolean(int intToBoolean) {
    return intToBoolean == 1;
  }

  static int parseInt(boolean toInt) {
    return toInt ? 1 : 0;
  }

  static int parseInt(String toInt) {
    return !Strings.isNullOrEmpty(toInt)
        && regexNumber.matcher(toInt).matches()
        ? Integer.parseInt(toInt) : 0;
  }

  static boolean isNumeric(String string) {
    return regexNumber.matcher(string).matches();
  }

  static boolean findNumeric(String string) {
    return regexNumber.matcher(string).find();
  }
}
