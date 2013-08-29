package com.seaniscool

import com.beust.jcommander.{IStringConverter, Parameter}
import scala.collection.JavaConversions._
import java.io.File
import com.beust.jcommander.converters.FileConverter

/**
 *
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
    arity = 1,
    converter = classOf[FileConverter]
  )
  private val _outputDirectory: java.util.List[File] = null

  def whatsAppFiles: List[File] = _whatsAppFile.toList

  def outputDirectory: File = _outputDirectory.get(0)


}
