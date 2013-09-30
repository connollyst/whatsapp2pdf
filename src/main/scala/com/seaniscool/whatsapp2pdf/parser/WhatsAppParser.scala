package com.seaniscool.whatsapp2pdf.parser

import java.io.File

/** The parser, transforms raw text to a [[com.seaniscool.whatsapp2pdf.parser.Conversation]].
  *
  * @author Sean Connolly
  */
class WhatsAppParser {

  def parse(file: File): Conversation = {
    val conversation = new Conversation
    val reader = LineReader.newReader(file)
    while (reader.ready()) {
      val line = reader.next()
      if (line.isNewMessage) {
        conversation.add(new Message(line))
      } else {
        conversation.last.append(line)
      }
    }
    conversation
  }

}


