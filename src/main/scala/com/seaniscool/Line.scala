package com.seaniscool

import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.{Matcher, Pattern}
import com.google.common.base.Objects

/** A line of raw text to be parsed.
  * May be the start of a message or not, may even be a blank line.
  *
  * @author Sean Connolly
  */
class Line(text: String, number: Int) {

  def isNewMessage: Boolean = {
    containsDate
  }

  def isBlank: Boolean = {
    text.trim.isEmpty
  }

  def containsDate: Boolean = {
    getDateMatcher.find
  }

  def stripDate: String = {
    getDateMatcher.replaceFirst("")
  }

  def getDate: Date = {
    val dateMatcher = getDateMatcher
    if (dateMatcher.find) {
      val date = dateMatcher.group(1)
      Line.DATE_FORMAT.parse(date)
    } else {
      throw new RuntimeException("No date found on line #" + number + ": " + text)
    }
  }

  def containsUser: Boolean = {
    getUserMatcher.find
  }

  def stripDateAndUser: String = {
    getUserMatcher.replaceFirst("")
  }

  def getUser: Option[String] = {
    val userMatcher = getUserMatcher
    if (userMatcher.find) {
      new Some(userMatcher.group(1))
    } else {
      None
    }
  }

  def getMessageBody: Option[String] = {
    val stripped = stripDateAndUser
    if (!stripped.isEmpty) {
      new Some(stripped)
    } else {
      None
    }
  }

  private def getDateMatcher: Matcher = {
    Line.DATE_REGEX.matcher(text)
  }

  private def getUserMatcher: Matcher = {
    val stripped = stripDate
    Line.USER_REGEX.matcher(stripped)
  }

  override def toString: String = {
    Objects.toStringHelper(getClass)
      .add("number", number)
      .add("text", text)
      .toString
  }

}

object Line {

  private val DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
  private val DATE_REGEX = Pattern.compile("(\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})")
  private val USER_REGEX = Pattern.compile(": ([^:]*):")

}