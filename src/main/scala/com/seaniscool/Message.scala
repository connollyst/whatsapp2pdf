package com.seaniscool

import com.google.common.base.Objects
import scala.collection.mutable.ListBuffer

/**
 *
 *
 * @author Sean Connolly
 */
class Message(private val lines: ListBuffer[Line]) {

  def this(line: Line) = {
    this(ListBuffer(line))
  }

  def this() = {
    this(new ListBuffer[Line])
  }

  def addLine(line: Line) = {
    lines += line
  }

  def trim(): Message = {
    val trimmed = new Message(lines)
    while (trimmed.lines.last.isBlank) {
      trimmed.lines.trimEnd(1)
    }
    trimmed
  }

  def isBlank: Boolean = {
    trim().isEmpty
  }

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
