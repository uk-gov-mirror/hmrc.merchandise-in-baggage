/*
 * Copyright 2020 HM Revenue & Customs
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

import play.api.libs.json.Json.{parse, toJson}
import uk.gov.hmrc.merchandiseinbaggage.{BaseSpecWithApplication, CoreTestData}

class DeclarationSpec extends BaseSpecWithApplication with CoreTestData {

  "serialise and de-serialise" in {
    val declaration = aDeclaration

    parse(toJson(declaration).toString()).validate[Declaration].get mustBe declaration
  }

  "be obfuscated" in {
    val declaration = aDeclaration.copy(maybeCustomsAgent = Some(aCustomsAgent), journeyDetails = aJourneyInASmallVehicle)

    declaration.obfuscated.nameOfPersonCarryingTheGoods mustBe Name("*****", "*****")
    declaration.obfuscated.email mustBe Email("********", "********")
    declaration.obfuscated.maybeCustomsAgent.get.name mustBe "**********"
    declaration.obfuscated.maybeCustomsAgent.get.address mustBe
      Address(Seq("*************", "**********"), Some("*******"), AddressLookupCountry("**", Some("**")))
    declaration.obfuscated.eori mustBe Eori("*********")
    declaration.obfuscated.journeyDetails.maybeRegistrationNumber mustBe Some("*******")
  }
}
