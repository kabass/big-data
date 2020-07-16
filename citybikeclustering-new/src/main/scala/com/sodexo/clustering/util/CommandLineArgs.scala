package com.sodexo.clustering.util

import com.beust.jcommander.Parameter

object CommandLineArgs {
  @Parameter(
    names = Array("-h", "--help"), help = true)
  var help = false

  @Parameter(
    names = Array("-e", "--env"),
    description = "the environnement : suffix of configuration file",
    required = true)
  var environment: String = _

}
