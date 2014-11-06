package com.mogobiz.launch

import java.io.File

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()

  val ServerListen = config.getString("spray.interface")
  val ServerPort = config.getInt("spray.port")
}