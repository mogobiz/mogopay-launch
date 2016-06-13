/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.launch.pay

import akka.http.scaladsl.Http
import com.mogobiz.pay.config.MogopayRoutes
import com.mogobiz.system.BootedMogobizSystem
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

object Rest extends App with BootedMogobizSystem with MogopayRoutes {
  val logger = Logger(LoggerFactory.getLogger("com.mogobiz.launch.pay.Rest"))
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
  logger.info(banner)
  Http().bindAndHandle(routes, Settings.Interface, Settings.Port)

}
