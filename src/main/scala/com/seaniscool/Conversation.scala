package com.seaniscool

import scala.collection.mutable.ListBuffer

/** A conversation is a series of messages. One text file is parsed into one
  * conversation.
  *
  * @author Sean Connolly
  */
class Conversation {

  val messages = new ListBuffer[Message]

  def add(message: Message) = {
    messages += message
  }

  def last: Message = {
    if (messages.isEmpty) {
      add(new Message)
    }
    messages.last
  }

  override def toString: String = {
    val builder = new StringBuilder(getClass.getSimpleName)
    builder.append("{")
    builder.append("size=").append(messages.length)
    builder.append(", ")
    builder.append("messages=")
    builder.append("\n")
    for (message <- messages) {
      builder.append(message.trim())
      builder.append("\n")
    }
    builder.append("}")
    builder.toString()
  }

}
