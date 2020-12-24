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

import play.api.i18n.Messages
import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.merchandiseinbaggage.util.Obfuscator.{maybeObfuscate, obfuscate}

case class Country(code: String, countryName: String, alphaTwoCode: String, isEu: Boolean, countrySynonyms: List[String]) {
  def displayName(implicit messages: Messages): String = messages(countryName)
  lazy val obfuscated: Country =
    Country(obfuscate(code), obfuscate(countryName), obfuscate(alphaTwoCode), isEu, countrySynonyms.map(c => obfuscate(c)))
}

object Country {
  implicit val formats: OFormat[Country] = Json.format[Country]
}

case class AddressLookupCountry(code: String, name: Option[String]) {
  lazy val obfuscated: AddressLookupCountry = AddressLookupCountry(obfuscate(code), maybeObfuscate(name))
}

case class Address(lines: Seq[String], postcode: Option[String], country: AddressLookupCountry) {
  lazy val obfuscated: Address =
    Address(lines.map(line => obfuscate(line)), maybeObfuscate(postcode), country.obfuscated)
}

object Address {
  implicit val formatCountry: OFormat[AddressLookupCountry] = Json.format[AddressLookupCountry]

  implicit val formatAddressLookupAddress: OFormat[Address] = Json.format[Address]
}
