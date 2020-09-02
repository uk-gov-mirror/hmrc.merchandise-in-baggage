/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage

import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.inject.Injector
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._
import uk.gov.hmrc.merchandiseinbaggage.config.{AppConfig, MongoConfiguration}


trait BaseSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach with BeforeAndAfterAll

trait BaseSpecWithApplication extends BaseSpec with GuiceOneAppPerSuite with MongoConfiguration {
  private val server: MongoServer = new MongoServer(new MemoryBackend())
  server.bind(mongoConf.host, mongoConf.port)

  override def afterAll(): Unit = server.shutdown()

  def injector: Injector = app.injector
  implicit val appConf: AppConfig = new AppConfig

  def buildPost(url: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(POST, url).withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  def buildPatch(url: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(PATCH, url).withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  def buildGet(url: String): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, url).withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
}
