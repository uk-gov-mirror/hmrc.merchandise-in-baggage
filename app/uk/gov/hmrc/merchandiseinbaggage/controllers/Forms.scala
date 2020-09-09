/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, _}

object Forms {

  val declarationFormIdentifier = "declarationForm"

  def declarationForm(formIdentifier: String): Form[DeclarationData] =
    Form(
      mapping(
        formIdentifier -> text
      )(DeclarationData.apply)(DeclarationData.unapply)
    )
}

case class DeclarationData(data: String)
