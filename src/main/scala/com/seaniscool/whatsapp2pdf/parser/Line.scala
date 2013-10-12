package com.seaniscool.whatsapp2pdf.parser

import com.google.common.base.Objects
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Pattern
import scala.Predef._
import scala.Some

/** A line of raw text to be parsed.
  * May be the start of a message or not, may even be a blank line.
  *
  * @author Sean Connolly
  */
class Line(text: String, val number: Int) {

  private var _date: Option[Date] = None
  private var _user: Option[String] = None
  private var _body: Option[String] = None

  val matcher = Line.REGEX_IOS.matcher(clean)
  if (matcher.find) {
    val dateMatch = matcher.group(2)
    val userMatch = matcher.group(4)
    val bodyMatch = matcher.group(5)
    _date = dateMatch match {
      case null => None
      case _ => Some(Line.DATE_FORMAT.parse(dateMatch))
    }
    _user = userMatch match {
      case null => None
      case _ => Some(userMatch)
    }
    _body = bodyMatch match {
      case null => None
      case _ => Some(bodyMatch)
    }
  }

  def date = _date

  def user = _user

  def body = _body

  /** Is this line the start of a new message?
    * This is indicated by a date on the line.
    *
    * @return true if the line starts a new message
    */
  def isNewMessage: Boolean = {
    _date.isDefined
  }

  /** Is this line blank?
    *
    * @return true if the line is blank
    */
  def isBlank: Boolean = {
    clean.isEmpty
  }

  /** Return a clean version of the text.
    *
    * @return
    */
  def clean: String = {
    if (text.startsWith("\uFEFF")) {
      text.substring(1).trim
    } else {
      text.trim
    }
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
    if (isBlank)
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

  /** Returns the file name of the attached image on this line.
    *
    * @return the file name of the attachment
    */
  def image: String = {
    var fileName = body.getOrElse(Line.NOTHING)
    for (pattern <- LinesWithAttachments.PATTERNS)
      fileName = pattern.matcher(fileName).replaceAll(Line.NOTHING)
    fileName.trim
  }


  override def toString: String = {
    Objects.toStringHelper(getClass)
      .add("number", number)
      .add("text", text)
      .toString
  }

}

object Line {

  private val NOTHING = ""
  private val DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss")
  private val REGEX_IOS = Pattern.compile("((\\d{2}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}): (([^“\\\":]*):)?)?\\s?(.*)")
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