/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.config

import javax.inject.Singleton
import uk.gov.hmrc.merchandiseinbaggage.config.AppConfigSource._
import pureconfig.generic.auto._ // Do not remove this

@Singleton
class AppConfig() extends AuthConfiguration with MongoConfiguration

trait AuthConfiguration {
  lazy val authConfig: AuthConfig = configSource("microservice.services.auth").loadOrThrow[AuthConfig]
  import authConfig._
  lazy val authBaseUrl = s"$protocol://$host:$port"
}

trait MongoConfiguration {
  lazy val mongoConf: MongoConfig = configSource("mongodb").loadOrThrow[MongoConfig]
}

trait StrideConfiguration {
  lazy val strideConf: StrideConfig = configSource("stride.role").loadOrThrow[StrideConfig]
}

final case class MongoConfig(uri: String, host: String = "localhost", port: Int = 27017, collectionName: String = "declaration")
final case class AuthConfig(protocol: String, host: String, port: Int)
final case class StrideConfig(strideRole: String)
