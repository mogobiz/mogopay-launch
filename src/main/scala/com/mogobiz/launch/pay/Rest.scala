/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.launch.pay

import akka.io.IO
import com.mogobiz.pay.config.{ MogopayRoutes }
import com.mogobiz.system.BootedMogobizSystem
import spray.can.Http

object Rest extends App with BootedMogobizSystem with MogopayRoutes {
  com.mogobiz.pay.jobs.ImportRatesJob.start(system)
  com.mogobiz.pay.jobs.ImportCountriesJob.start(system)
  com.mogobiz.pay.jobs.CleanAccountsJob.start(system)
  com.mogobiz.pay.jobs.RefundJob.start(system)

  override val bootstrap = {
    super[MogopayRoutes].bootstrap()
  }
  val banner =
    """
      | __  __
      ||  \/  | ___   __ _  ___  _ __   __ _ _   _
      || |\/| |/ _ \ / _` |/ _ \| '_ \ / _` | | | |
      || |  | | (_) | (_| | (_) | |_) | (_| | |_| |
      ||_|  |_|\___/ \__, |\___/| .__/ \__,_|\__, |
      |              |___/      |_|          |___/
      |    """.stripMargin
  println(banner)
  IO(Http)(system) ! Http.Bind(routesServices, interface = Settings.ServerListen, port = Settings.ServerPort)
}
