package com.seaniscool

import com.beust.jcommander.{IStringConverter, Parameter}
import scala.collection.JavaConversions._
import java.io.File
import com.beust.jcommander.converters.FileConverter

/** Command line arguments.
  * This object is passed to [[com.beust.jcommander.JCommander]] to be filled
  * and is then used by [[com.seaniscool.CommandLine]].
  *
  * @author Sean Connolly
  */
object CommandLineArgs {

  @Parameter(
    description = "The WhatsApp text file.",
    required = true,
    variableArity = true,
    converter = classOf[FileConverter]
  )
  private val _whatsAppFile: java.util.List[File] = null

  @Parameter(
    names = Array("-o", "-output"),
    description = "The output directory.",
    required = false,
    arity = 1,
    converter = classOf[FileConverter]
  )
  private val _outputDirectory: java.util.List[File] = null

  @Parameter(
    names = Array("-d", "-debug"),
    description = "Debug mode"
  )
  val debugMode: Boolean = false

  def whatsAppFiles: List[File] = _whatsAppFile.toList

  /** Get the output directory.
    *
    * If one is not specified, the user's current working directory is used.
    *
    * @return the output directory
    */
  def outputDirectory: File = {
    if (_outputDirectory == null || _outputDirectory.isEmpty) {
      val workingDirectory = System.getProperty("user.dir")
      Log.debug("Defaulting output directory to " + workingDirectory)
      new File(workingDirectory)
    } else {
      _outputDirectory.get(0)
    }
  }

}
