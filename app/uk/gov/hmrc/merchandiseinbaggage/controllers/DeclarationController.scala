/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.merchandiseinbaggage.controllers

import cats.instances.future._
import play.api.Logger
import play.api.i18n.Messages
import play.api.libs.json.Json.toJson
import play.api.mvc._
import uk.gov.hmrc.merchandiseinbaggage.model.api.{Declaration, DeclarationId, Eori, MibReference}
import uk.gov.hmrc.merchandiseinbaggage.model.core.{DeclarationNotFound, PaymentCallbackRequest}
import uk.gov.hmrc.merchandiseinbaggage.service.DeclarationService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class DeclarationController @Inject()(declarationService: DeclarationService, mcc: MessagesControllerComponents)(
  implicit val ec: ExecutionContext)
    extends BackendController(mcc) {

  implicit def messages(implicit request: Request[_]): Messages = mcc.messagesApi.preferred(request)

  private val logger = Logger(this.getClass)

  def onDeclarations(): Action[Declaration] = Action(parse.json[Declaration]).async { implicit request =>
    declarationService.persistDeclaration(request.body).map { declaration =>
      logger.warn(s"new ${declaration.declarationType} declaration with Ref: ${declaration.mibReference}")
      Created(toJson(declaration.declarationId))
    }
  }

  def amendDeclaration(): Action[Declaration] = Action(parse.json[Declaration]).async { implicit request =>
    declarationService.amendDeclaration(request.body).map { declaration =>
      logger.warn(s"amend ${declaration.declarationType} declaration with Ref: ${declaration.mibReference}")
      Ok(toJson(declaration.declarationId))
    }
  }

  def onRetrieve(declarationId: DeclarationId): Action[AnyContent] = Action.async {

    declarationService
      .findByDeclarationId(declarationId)
      .fold(
        {
          case DeclarationNotFound =>
            logger.warn(s"$declarationId not found")
            NotFound
          case e =>
            logger.error(s"Error for $declarationId - [$e]]")
            InternalServerError("Something went wrong")
        },
        foundDeclaration => {
          Ok(toJson(foundDeclaration))
        }
      )
  }

  def findBy(mibReference: MibReference, eori: Eori): Action[AnyContent] = Action.async {
    declarationService
      .findBy(mibReference, eori)
      .fold(
        {
          case DeclarationNotFound =>
            logger.warn(s"Declaration not found for params: $mibReference, $eori")
            NotFound
          case e =>
            logger.error(s"Error during findBy query - $e - params: $mibReference, $eori")
            InternalServerError("Something went wrong")
        },
        foundDeclaration => {
          Ok(toJson(foundDeclaration))
        }
      )
  }

  def handlePaymentCallback: Action[PaymentCallbackRequest] = Action(parse.json[PaymentCallbackRequest]).async { implicit request =>
    val callbackRequest = request.body
    logger.warn(s"got the payment callback with request: $callbackRequest")

    declarationService
      .processPaymentCallback(callbackRequest)
      .fold(
        {
          case DeclarationNotFound =>
            logger.warn(s"Declaration with params [$callbackRequest] not found")
            NotFound
          case e =>
            logger.error(s"Error for Declaration with params [$callbackRequest] - [$e]]")
            InternalServerError("Something went wrong")
        },
        _ => Ok
      )
  }
}
