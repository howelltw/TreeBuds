package com.h2.treebuds.room

import androidx.room.TypeConverter
import java.util.*
import java.util.stream.Collectors

class ListConverter {
  @TypeConverter
  fun stringToListOfStrings(data: String?): List<String> {
    if (data == null) {
      return Collections.emptyList()
    }
    return data.split(",")
  }

  @TypeConverter
  fun listOfStringsToString(stringList: List<String>): String {
    return stringList.stream().collect(Collectors.joining(","))
  }

}
