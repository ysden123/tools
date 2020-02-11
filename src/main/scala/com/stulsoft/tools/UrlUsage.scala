/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.tools

import java.io.{File, FileInputStream}
import java.time.Duration

import scala.io.Source

/**
 * @author Yuriy Stul
 */

case class IpUsage(ip: String, var counter: Int)

case class Usage(url: String, var counter: Int,
                 //                 var ipUsages: scala.collection.mutable.TreeMap[String, IpUsage] = scala.collection.mutable.TreeMap.empty) {
                 var ipUsages: scala.collection.mutable.Map[String, IpUsage] = scala.collection.mutable.Map.empty) {

  def increaseIpUsage(ip: String): Unit = {
    ipUsages.get(ip) match {
      case Some(ipu) => ipu.counter += 1
      case None =>
        ipUsages += (ip -> IpUsage(ip, 1))
    }
  }
}

object UrlUsage extends App {
  val dir = new File("c:\\work\\res1")
  val usages = Set(
    Usage("/admin/saveManualSubIdRevenue", 0),
    Usage("/admin/getSubIdRevenue", 0),
    Usage("/admin/saveRevenueAndDeposit", 0),
    Usage("/admin/migrateToSubIdRevenue", 0),
    Usage("/admin/migrateToSubIdRevenueRowId", 0),

    Usage("/plc/get_pages_stats", 0),
    Usage("/plc/get_site_stats", 0),
    Usage("/plc/get_site_stats_by_userid", 0),
    Usage("/plc/get_widget_stats", 0),
    Usage("/plc/get_top_pages_stats", 0),

    Usage("/admin/exportRMPConversionBing", 0),
    Usage("/admin/exportRMPConversion", 0),

    Usage("/bulk", 0),

    Usage("/pixel", 0),
    Usage("/admin/get_all_pixels", 0),
    Usage("/admin/count_pixel_data", 0),
    Usage("/admin/force_rds_sync_pixel", 0),
  )

  val startTime = System.currentTimeMillis()
  var fileCounter = 0L
  var lineCounter = 0L
  dir
    .listFiles()
    .foreach(subDir => {
      subDir
        .listFiles()
        .foreach(file => {
          fileCounter += 1L
          val source = Source.fromInputStream(new FileInputStream(file))
          for (line <- source.getLines()) {
            lineCounter += 1L
            usages.foreach(usage => {
              if (line.contains(usage.url)) {
                usage.counter = usage.counter + 1
                usage.increaseIpUsage(extractIp(line))
              }
            })
          }
          source.close()
        })
    })

  val formatter = java.text.NumberFormat.getIntegerInstance()

  usages.foreach(usage => {
    val result = s"${usage.url} is used ${formatter.format(usage.counter)} times "
    if (usage.counter > 0) {
      val ipUsages = usage
        .ipUsages
        .toSeq
        .sortWith { case ((_, ipUsage1), (_, ipUsage2)) => ipUsage1.counter > ipUsage2.counter }
        .map { case (_, ipUsage) => s"${ipUsage.ip} - ${formatter.format(ipUsage.counter)}" }
        .mkString(", ")
      println(s"*** $result. IP usage: $ipUsages ***")
    } else
      println(s"$result")
  })

  println(s"Total number of log files is ${formatter.format(fileCounter)}, total number of lines is ${formatter.format(lineCounter)}")
  println(s"Duration: ${Utils.durationToString(Duration.ofMillis(System.currentTimeMillis() - startTime))}")

  def extractIp(line: String): String = {
    line.split(' ')(3).split(':')(0)
  }
}
