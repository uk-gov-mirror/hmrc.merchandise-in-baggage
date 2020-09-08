/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.controllers

import javax.inject.Inject
import play.api.mvc._
import uk.gov.hmrc.merchandiseinbaggage.config.AppConfig
import uk.gov.hmrc.merchandiseinbaggage.views.html.DeclarationTestOnlyPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import scala.concurrent.{ExecutionContext, Future}

class DeclarationTestOnlyController @Inject()(mcc: MessagesControllerComponents,
                                              views: DeclarationTestOnlyPage)
                                             (implicit val ec: ExecutionContext, appConfig: AppConfig)
  extends FrontendController(mcc) {

  def declarations(): Action[AnyContent] = Action.async { implicit request  =>
    Future.successful(Ok(views()))
  }

  def onSubmit(): Action[AnyContent] = Action.async { implicit request  =>
    Future.successful(Ok("on submit"))
  }
}

