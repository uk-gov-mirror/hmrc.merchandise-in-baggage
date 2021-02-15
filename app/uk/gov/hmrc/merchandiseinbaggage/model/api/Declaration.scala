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

package uk.gov.hmrc.merchandiseinbaggage.model.api

import java.time.LocalDateTime

import play.api.libs.json.{Json, OFormat}

case class Declaration(
  declarationId: DeclarationId,
  sessionId: SessionId,
  declarationType: DeclarationType,
  goodsDestination: GoodsDestination,
  declarationGoods: DeclarationGoods,
  nameOfPersonCarryingTheGoods: Name,
  email: Option[Email],
  maybeCustomsAgent: Option[CustomsAgent],
  eori: Eori,
  journeyDetails: JourneyDetails,
  dateOfDeclaration: LocalDateTime,
  mibReference: MibReference,
  maybeTotalCalculationResult: Option[TotalCalculationResult] = None,
  emailsSent: Boolean = false,
  paymentSuccess: Option[Boolean] = None,
  lang: String = "en",
  source: Option[String] = None)

object Declaration {
  val id = "declarationId"
  val sessionId = "sessionId"
  implicit val format: OFormat[Declaration] = Json.using[Json.WithDefaultValues].format[Declaration]
}
