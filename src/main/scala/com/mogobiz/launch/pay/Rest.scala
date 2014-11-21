package com.mogobiz.launch.pay

import akka.io.IO
import com.mogobiz.pay.config.{MogopayActors, MogopayRoutes}
import com.mogobiz.system.BootedMogobizSystem
import spray.can.Http

object Rest extends App with BootedMogobizSystem with MogopayActors with MogopayRoutes {
  com.mogobiz.pay.jobs.ImportRatesJob.start(system)
  com.mogobiz.pay.jobs.ImportCountriesJob.start(system)
  com.mogobiz.pay.jobs.CleanAccountsJob.start(system)

  override val bootstrap = {
    super[MogopayRoutes].bootstrap()
    com.mogobiz.session.boot.DBInitializer()
    com.mogobiz.notify.boot.DBInitializer()
  }

  IO(Http)(system) ! Http.Bind(routesServices, interface = Settings.ServerListen, port = Settings.ServerPort)
}
