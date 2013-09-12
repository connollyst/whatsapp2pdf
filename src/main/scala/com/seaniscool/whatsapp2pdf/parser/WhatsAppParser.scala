package com.seaniscool.whatsapp2pdf.parser

import com.seaniscool.whatsapp2pdf.Log
import java.io.File

/** The parser, transforms raw text to a [[com.seaniscool.whatsapp2pdf.parser.Conversation]].
  *
  * @author Sean Connolly
  */
class WhatsAppParser(outputDirectory: File) {

  def parse(file: File): Conversation = {
    Log.info("Parsing " + file + " to " + outputDirectory)
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


