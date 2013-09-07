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

  /** Is this line blank?
    *
    * @return
    */
  def isBlank: Boolean = {
    text.trim.isEmpty
  }

  /** Is this line to be excluded from printing?
    *
    * Certain lines shouldn't be printed, omitted media, video attachments, etc.
    * This function determines if this line should be excluded or not.
    *
    * @return true if this line should be excluded
    */
  def isPrintable: Boolean = {
    for (pattern <- LinesToExclude.PATTERNS)
      if (pattern.matcher(text).find)
        return false
    if (isAttachment)
      if (!isSupportedImage)
        return false
    true
  }

  /** Does this line contain an attachment?
    *
    * @return true if this line contains an attachment
    */
  def isAttachment: Boolean = {
    for (pattern <- LinesWithAttachments.PATTERNS)
      if (pattern.matcher(text).find)
        return true
    false
  }

  /** Does this line contain an image attachment in a supported format?
    *
    * @return true if this line contains a supported image attachment
    */
  def isSupportedImage: Boolean = {
    for (format <- LinesWithAttachments.IMAGE_FORMATS) {
      if (format.matcher(text).find()) {
        return true
      }
    }
    false
  }

  def image: String = {
    var fileName = body
    for (pattern <- LinesWithAttachments.PATTERNS)
      fileName = pattern.matcher(fileName).replaceAll("")
    fileName.trim
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

private object LinesToExclude {

  val PATTERNS = List(
    Pattern.compile("<media omitted>$", Pattern.CASE_INSENSITIVE),
    Pattern.compile("<vCard omitted>$", Pattern.CASE_INSENSITIVE),
    Pattern.compile("vCard attached:.*\\.vcf$", Pattern.CASE_INSENSITIVE),
    Pattern.compile("location: https?://maps.google.com", Pattern.CASE_INSENSITIVE)
  )

}

private object LinesWithAttachments {

  val PATTERNS = List(
    // IOS attachments
    Pattern.compile("<attached>$", Pattern.CASE_INSENSITIVE),
    // Android attachments
    Pattern.compile("\\(file attached\\)$", Pattern.CASE_INSENSITIVE)
  )

  val IMAGE_FORMATS = List(
    // IOS image attachments
    Pattern.compile("\\.JPE?G <attached>$", Pattern.CASE_INSENSITIVE),
    Pattern.compile("\\.PNG <attached>$", Pattern.CASE_INSENSITIVE),
    Pattern.compile("\\.GIF <attached>$", Pattern.CASE_INSENSITIVE),
    // Android image attachments
    Pattern.compile("\\.JPE?G \\(file attached\\)$", Pattern.CASE_INSENSITIVE),
    Pattern.compile("\\.PNG \\(file attached\\)$", Pattern.CASE_INSENSITIVE),
    Pattern.compile("\\.GIF \\(file attached\\)$", Pattern.CASE_INSENSITIVE)
  )

}