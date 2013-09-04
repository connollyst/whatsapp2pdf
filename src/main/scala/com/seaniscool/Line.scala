package com.seaniscool

import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.{Pattern, Matcher}
import com.google.common.base.Objects

/** A line of raw text to be parsed.
  * May be the start of a message or not, may even be a blank line.
  *
  * @author Sean Connolly
  */
class Line(text: String, val number: Int) {

  val date = extractDate
  val user = extractUser
  val body = extractBody

  def isNewMessage: Boolean = {
    containsDate
  }

  def isBlank: Boolean = {
    text.trim.isEmpty
  }

  def isExcludable: Boolean = {
    for (pattern <- LinesToExclude.PATTERNS) {
      if (pattern.matcher(text).find) {
        return true
      }
    }
    false
  }

  def containsDate: Boolean = {
    getDateMatcher.find
  }

  def stripDate: String = {
    getDateMatcher.replaceFirst("")
  }

  private def extractDate: Option[Date] = {
    val dateMatcher = getDateMatcher
    if (dateMatcher.find) {
      val date = dateMatcher.group(1)
      Some(Line.DATE_FORMAT.parse(date))
    } else {
      None
    }
  }

  def containsUser: Boolean = {
    getUserMatcher.find
  }

  def stripDateAndUser: String = {
    getUserMatcher.replaceFirst("")
  }

  def extractUser: Option[String] = {
    val userMatcher = getUserMatcher
    if (userMatcher.find) {
      new Some(userMatcher.group(1))
    } else {
      None
    }
  }

  def extractBody: String = {
    val stripped = stripDateAndUser
    if (!stripped.isEmpty) {
      stripped
    } else {
      ""
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

object LinesToExclude {

  val PATTERNS = List(
    Pattern.compile("<media omitted>$"),
    Pattern.compile("<media omitted>$"),
    Pattern.compile("<vCard omitted>$"),
    Pattern.compile("vCard attached:.*\\.vcf$"),
    Pattern.compile("location: https?://maps.google.com/?q=\\d+\\.\\d+,\\d+\\.\\d+")
  )

}