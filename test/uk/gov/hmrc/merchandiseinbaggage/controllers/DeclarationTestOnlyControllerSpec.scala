/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.controllers

import play.api.test.Helpers._
import uk.gov.hmrc.merchandiseinbaggage.BaseSpecWithApplication
import uk.gov.hmrc.merchandiseinbaggage.config.AppConfig
import uk.gov.hmrc.merchandiseinbaggage.views.html.DeclarationTestOnlyPage

import scala.concurrent.ExecutionContext.Implicits.global

class DeclarationTestOnlyControllerSpec extends BaseSpecWithApplication {

  val view = injector.instanceOf[DeclarationTestOnlyPage]
  implicit val config = new AppConfig
  val controller = new DeclarationTestOnlyController(component, view)


  "ready html page is served which contains copy showing it is a test-only page and a form with which I an enter and submit a declaration" in {
    val request = buildGet(routes.DeclarationTestOnlyController.declarations().url)
    val result = controller.declarations()(request)

    status(result) mustBe 200
    contentAsString(result) mustBe view()(request).toString
  }
}
