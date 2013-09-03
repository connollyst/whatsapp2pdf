package com.seaniscool

import com.beust.jcommander.JCommander


/** The main entry point to the application.
  * Interprets the command line arguments and kicks off the parser.
  *
  * @author Sean Connolly
  */
object CommandLine {

  def main(args: Array[String]) {
    parseArgs(args)
    val parser = new WhatsAppParser(CommandLineArgs.outputDirectory)
    val writer = new PDFWriter(CommandLineArgs.outputDirectory)
    for (file <- CommandLineArgs.whatsAppFiles) {
      val conversation = parser.parse(file)
      println(conversation)
      writer.write(file.getName, conversation)
    }
  }

  private def parseArgs(args: Array[String]) {
    new JCommander(CommandLineArgs, args.toArray: _*)
    Log.debug("Debugging mode: " + CommandLineArgs.debugMode)
  }

}
