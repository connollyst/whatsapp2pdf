package com.seaniscool

import scala.collection.mutable.ListBuffer

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

  def addLine(line: Line) = {
    lines += line
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
    while (trimmed.lines.last.isBlank) {
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
