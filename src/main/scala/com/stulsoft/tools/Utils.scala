/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.tools

import java.time.Duration

/**
 * @author Yuriy Stul
 */
object Utils {
  def durationToString(duration: Duration): String = {
    var seconds = duration.getSeconds
    val days = seconds / (3600 * 24)
    seconds -= days * (3600 * 24)
    val hours = seconds / 3600
    seconds -= hours * 3600
    val minutes = seconds / 60
    seconds -= minutes * 60
    f"""${if (days > 0) days.toString + " days " else ""}$hours%02d:$minutes%02d:$seconds%02d"""
  }
}
