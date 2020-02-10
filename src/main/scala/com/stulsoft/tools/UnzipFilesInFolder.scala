/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.tools

import java.io.{File, FileInputStream, FileOutputStream}
import java.time.Duration
import java.util.zip.GZIPInputStream

import scala.io.Source

/**
 * @author Yuriy Stul
 */
object UnzipFilesInFolder extends App {

  val startTime = System.currentTimeMillis()

  run("c:\\work\\access-logs\\AWSLogs\\288058947579\\elasticloadbalancing\\eu-west-1\\2020\\01\\", "c:\\work\\res1")
//  copyFile("c:\\work\\access-logs\\AWSLogs\\288058947579\\elasticloadbalancing\\eu-west-1\\2020\\01\\01\\288058947579_elasticloadbalancing_eu-west-1_app.Tracking-Prod-ELB.1127c424a362a964_20200101T2355Z_52.50.211.138_4gwqh4sr.log.gz",
//    "288058947579_elasticloadbalancing_eu-west-1_app.Tracking-Prod-ELB.1127c424a362a964_20200101T2355Z_52.50.211.138_4gwqh4sr.log")

  println(s"Duration: ${Utils.durationToString(Duration.ofMillis(System.currentTimeMillis() - startTime))}")

  def copyFile(srcPath: String, targetPath: String): Unit = {
    val in = new GZIPInputStream(new FileInputStream(srcPath))
    val output = new FileOutputStream(targetPath)

    for (line <- Source.fromInputStream(in).getLines())
      output.write((line + System.lineSeparator()).getBytes)

    in.close()
    output.close()
  }

  def run(srcPath:String, targetPath:String): Unit = {
    val srcDir = new File(srcPath)
    srcDir
      .listFiles()
      .foreach(subDir => {
//        println(s"subDir: ${subDir.getAbsolutePath}, dir: ${subDir.getName}")
        val targetDir = targetPath + File.separator + subDir.getName
        println(s"targetDir = $targetDir")

        val targetDirFile = new File(targetDir)
        if (!targetDirFile.exists())
          targetDirFile.mkdir()

        subDir
          .listFiles()
          .filter(file => file.isFile && file.getAbsolutePath.endsWith(".gz"))
          .foreach(file => {
            val srcPath = file.getAbsolutePath
            copyFile(srcPath, targetDir + File.separator + file.getName.replace(".gz",""))
          })
      })
  }
}
