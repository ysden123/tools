/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.tools

import java.io.File

import scala.io.{Source, StdIn}

/**
 * @author Yuriy Stul
 */
object FindFilesWithWords extends App {
  println("Finds files with words")
  println("Enter folder (empty to exit):")
  val folder = StdIn.readLine()
  if (folder.isEmpty)
    System.exit(0)

  println("Enter words separated by comma (empty to exit)")
  val wordsText = StdIn.readLine()
  if (wordsText.isEmpty)
    System.exit(0)
  val words = wordsText.split(',')
  val pattern = ("(?i)" + words.mkString(".*?")).r

  println(s"""Looking for files in $folder with words: ${words.mkString(", ")}""")

  buildFileList(folder).foreach(file => {
    val source = Source.fromFile(file)
    var lineNumber = 0
    source.getLines().foreach(line => {
      lineNumber += 1
      pattern findFirstIn line foreach (_ => println(s"File: ${file.getName}, line ($lineNumber): $line"))
    })
    source.close()
  })

  def buildFileList(folder: String): List[File] = {
    val dir = new File(folder)
    if (dir.exists() && dir.isDirectory) {
      dir.listFiles().filter(_.isFile).toList
    } else {
      List.empty
    }
  }
}
