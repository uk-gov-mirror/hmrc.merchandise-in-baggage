/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.controllers

import play.api.test.Helpers._
import play.modules.reactivemongo.ReactiveMongoComponent
import uk.gov.hmrc.merchandiseinbaggage.BaseSpecWithApplication
import uk.gov.hmrc.merchandiseinbaggage.controllers.Forms._
import uk.gov.hmrc.merchandiseinbaggage.repositories.DeclarationRepository
import uk.gov.hmrc.merchandiseinbaggage.views.html.DeclarationTestOnlyPage
import uk.gov.hmrc.mongo.MongoConnector

import scala.concurrent.ExecutionContext.Implicits.global

class DeclarationTestOnlyControllerSpec extends BaseSpecWithApplication {

  val view = injector.instanceOf[DeclarationTestOnlyPage]
  val reactiveMongo = new ReactiveMongoComponent { override def mongoConnector: MongoConnector = MongoConnector(mongoConf.uri)}
  val repository = new DeclarationRepository(reactiveMongo.mongoConnector.db)
  val controller = new DeclarationTestOnlyController(component, view, repository)


  "ready html page is served which contains copy showing it is a test-only page and a form with which I an enter and submit a declaration" in {
    val request = buildGet(routes.DeclarationTestOnlyController.declarations().url)
    val result = controller.declarations()(request)

    status(result) mustBe 200
    contentAsString(result) mustBe view(Forms.declarationForm(declarationFormIdentifier))(request).toString
  }
}
