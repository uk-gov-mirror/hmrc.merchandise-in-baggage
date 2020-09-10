/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.controllers

import cats.Id
import cats.data.EitherT
import javax.inject.Inject
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._
import uk.gov.hmrc.merchandiseinbaggage.controllers.Forms._
import uk.gov.hmrc.merchandiseinbaggage.model.api.DeclarationRequest
import uk.gov.hmrc.merchandiseinbaggage.model.core.{BusinessError, Declaration, DeclarationId, DeclarationNotFound, InvalidDeclarationRequest}
import uk.gov.hmrc.merchandiseinbaggage.repositories.DeclarationRepository
import uk.gov.hmrc.merchandiseinbaggage.views.html._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import scala.concurrent.{ExecutionContext, Future}

class DeclarationTestOnlyController @Inject()(mcc: MessagesControllerComponents,
                                              views: DeclarationTestOnlyPage,
                                              declarationFoundView: DeclarationFoundTestOnlyPage,
                                              repository: DeclarationRepository
                                             )
                                             (implicit val ec: ExecutionContext)
  extends FrontendController(mcc) with Forms {

  def declarations(): Action[AnyContent] = Action.async { implicit request  =>
    Future.successful(Ok(views(declarationForm("declarationForm"))))
  }

  def findDeclaration(declarationId: String): Action[AnyContent] = Action.async { implicit request  =>
    import cats.instances.future._
    EitherT.fromOptionF[Future, BusinessError, Declaration](repository
      .findByDeclarationId(DeclarationId(declarationId)), DeclarationNotFound).fold({
      case DeclarationNotFound => NotFound
    }, dec => Ok(declarationFoundView(dec)))
  }

  def onSubmit(): Action[AnyContent] = Action.async { implicit request  =>
    val newDeclaration: EitherT[Id, InvalidDeclarationRequest.type, Future[Declaration]] =
      for {
        declarationRequest <- EitherT.fromOption(Json.parse(bindForm.data(declarationFormIdentifier))
                                .asOpt[DeclarationRequest], InvalidDeclarationRequest)
        inserted           <- EitherT.pure(repository.insert(declarationRequest.toDeclarationInInitialState))
      } yield inserted

    newDeclaration fold (
      _                 => Future.successful(InternalServerError("InvalidRequest")),
      _.map(declaration => Redirect(routes.DeclarationTestOnlyController.findDeclaration(declaration.declarationId.value)))
    )
  }

  protected def bindForm(implicit request: Request[_]): Form[DeclarationData] =
    declarationForm(declarationFormIdentifier).bindFromRequest
}


