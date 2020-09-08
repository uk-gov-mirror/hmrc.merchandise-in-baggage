/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.controllers

import javax.inject.Inject
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.{ExecutionContext, Future}

class DeclarationTestOnlyController @Inject()(mcc: MessagesControllerComponents)(implicit val ec: ExecutionContext)
  extends BackendController(mcc) {

  def declarations(): Action[AnyContent] = Action.async { implicit request  =>
    Future.successful(Ok("hello"))
  }
}

