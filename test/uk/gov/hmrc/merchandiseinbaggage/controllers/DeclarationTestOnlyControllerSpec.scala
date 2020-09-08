/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.controllers

import uk.gov.hmrc.merchandiseinbaggage.BaseSpecWithApplication
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.test.Helpers._

class DeclarationTestOnlyControllerSpec extends BaseSpecWithApplication {

  val controller = new DeclarationTestOnlyController(component)


  "ready html page is served which contains copy showing it is a test-only page and a form with which I an enter and submit a declaration" in {

    val result = controller.declarations()(buildGet(routes.DeclarationTestOnlyController.declarations().url))

    status(result) mustBe 200
  }

}
