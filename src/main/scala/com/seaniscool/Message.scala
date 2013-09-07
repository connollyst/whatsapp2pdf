package com.seaniscool

import scala.collection.mutable.ListBuffer
import java.util.Date

/** A message the unit of a [[com.seaniscool.Conversation]], consisting of a
  * date, a user name, and one or more [[com.seaniscool.Line]]s.
  *
  * @author Sean Connolly
  */
class Message(val lines: ListBuffer[Line]) {

  def this(line: Line) = {
    this(ListBuffer(line))
  }

  def this() = {
    this(new ListBuffer[Line])
  }

  /** Append a line to the message.
    *
    * @param line the line
    */
  def append(line: Line) = {
    lines += line
  }

  /** Get the first line of the message.
    *
    * @return the first line
    */
  def firstLine: Line = {
    if (lines.isEmpty) {
      throw new RuntimeException("Can't get first line of message with no lines.")
    }
    lines.head
  }

  /** Get the date the message was sent
    *
    * @return the date
    */
  def date: Date = {
    // TODO handle exceptions
    firstLine.date.get
  }

  /** Get the name of the user who sent the message.
    *
    * @return the user name
    */
  def user: String = {
    firstLine.user.getOrElse("")
  }

  def clean(): Message = {
    val cleaned = new Message(lines)
    val linesToExclude = new ListBuffer[Line]
    for (line <- cleaned.lines) {
      if (!line.isPrintable) {
        Log.debug("Excluding " + line)
        linesToExclude += line
      }
    }
    cleaned.lines --= linesToExclude
    cleaned.trim()
  }

  /** Trim leading and trailing blank lines from the message. Blank lines in the
    * message are preserved.
    *
    * Note: the message itself is untouched, a new Message is returned
    *
    * @return the trimmed message
    */
  def trim(): Message = {
    val trimmed = new Message(lines)
    while (!trimmed.lines.isEmpty && trimmed.lines.last.isBlank) {
      trimmed.lines.trimEnd(1)
    }
    trimmed
  }

  /** Is the message itself blank?
    * That is, is it empty or are all it's lines blank?
    *
    * @return whether the message is blank
    */
  def isBlank: Boolean = {
    trim().isEmpty
  }

  /** Is the message empty?
    * That is, does it not contain any lines at all?
    *
    * @return whether the message is empty
    */
  def isEmpty: Boolean = {
    lines.isEmpty
  }

  override def toString: String = {
    val builder = new StringBuilder(getClass.getSimpleName)
    builder.append("{")
    builder.append("length=").append(lines.length)
    builder.append(", ")
    builder.append("lines=")
    builder.append("\n")
    for (line <- lines) {
      builder.append(line)
      builder.append("\n")
    }
    builder.append("}")
    builder.toString()
  }

}
