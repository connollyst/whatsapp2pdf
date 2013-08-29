package com.seaniscool

import com.google.common.base.Charsets
import com.google.common.io.Files
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Pattern

/**
 *
 *
 * @author Sean Connolly
 */
class WhatsAppParser(outputDirectory: File) {

  var lineCount = 0

  def parse(file: File) {
    println("Parsing " + file + " to " + outputDirectory)
    val reader = Files.newReader(file, Charsets.UTF_8)
    var line = ""
    while ( {
      line = reader.readLine()
      line != null
    }) {
      lineCount += 1
      val isBlank = line.trim.isEmpty
      if (!isBlank) {
        val message = parseLine(line)
        println(message)
      }
    }
  }

  private def parseLine(line: String): Message = {
    val date = getDate(line)
    val user = getUser(line)
    new Message(date, user, "TODO", "TODO")
  }

  private def getDate(line: String): Date = {
    val dateMatcher = WhatsAppParser.DATE_REGEX.matcher(line)
    if (dateMatcher.find()) {
      val date = dateMatcher.group(1)
      WhatsAppParser.DATE_FORMAT.parse(date)
    } else {
      throw new RuntimeException("No date found on line #" + lineCount + ": " + line)
    }
  }

  private def stripDate(line: String): String = {
    WhatsAppParser.DATE_REGEX.matcher(line).replaceFirst("")
  }

  private def getUser(line: String): Option[String] = {
    val lineWithoutDate = stripDate(line)
    val userMatcher = WhatsAppParser.USER_REGEX.matcher(lineWithoutDate)
    if (userMatcher.find()) {
      new Some(userMatcher.group(1))
    } else {
      None
    }
  }

}

object WhatsAppParser {

  private val DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
  private val DATE_REGEX = Pattern.compile("(\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})")
  private val USER_REGEX = Pattern.compile(": (.*):")


}
