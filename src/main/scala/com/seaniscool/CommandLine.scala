package com.seaniscool

import com.beust.jcommander.JCommander

/** The main entry point to the application.
  * Interprets the command line arguments and kicks off the parser.
  *
  * @author Sean Connolly
  */
object CommandLine {

  def main(args: Array[String]) {
    try {
      parseArgs(args)
      val parser = new WhatsAppParser(CommandLineArgs.outputDirectory)
      val writer = new PDFWriter(CommandLineArgs.outputDirectory)
      for (file <- CommandLineArgs.whatsAppFiles) {
        val conversation = parser.parse(file)
        writer.write(file, conversation)
      }
    } catch {
      case e: Exception =>
        if (CommandLineArgs.debugMode) {
          e.printStackTrace()
        } else {
          println("Error: " + e.getMessage)
        }
    }
  }

  private def parseArgs(args: Array[String]) {
    new JCommander(CommandLineArgs, args.toArray: _*)
    Log.debug("Debugging mode: " + CommandLineArgs.debugMode)
  }

}
