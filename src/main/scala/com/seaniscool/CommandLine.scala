package com.seaniscool

import com.beust.jcommander.{IStringConverter, JCommander}
import java.io.File


/**
 *
 *
 * @author Sean Connolly
 */
object CommandLine {

  def main(args: Array[String]) {
    parseArgs(args)
    val parser = new WhatsAppParser(CommandLineArgs.outputDirectory)
    for (file <- CommandLineArgs.whatsAppFiles) {
      parser.parse(file)
    }
  }

  private def parseArgs(args: Array[String]) {
    new JCommander(CommandLineArgs, args.toArray: _*)
  }



}
