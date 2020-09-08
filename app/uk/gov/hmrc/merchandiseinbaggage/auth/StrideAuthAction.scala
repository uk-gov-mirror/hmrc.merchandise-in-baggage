/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.merchandiseinbaggage.auth

import javax.inject.Inject
import org.slf4j.LoggerFactory.getLogger
import play.api.mvc.Results.Forbidden
import play.api.mvc._
import play.api.{Configuration, Environment}
import uk.gov.hmrc.auth.core.AuthProvider.PrivilegedApplication
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.Credentials
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals.credentials
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.merchandiseinbaggage.config.StrideConfiguration
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.bootstrap.config.AuthRedirects

import scala.concurrent.{ExecutionContext, Future}

class StrideAuthAction @Inject()(override val authConnector: AuthConnector,
                                 mcc: MessagesControllerComponents,
                                 val conf: Configuration, val en: Environment //TODO to be removed
                                )(implicit ec: ExecutionContext)
  extends ActionBuilder[AuthRequest, AnyContent]
    with AuthorisedFunctions
    with AuthRedirects with StrideConfiguration {

  override def parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser

  override def config: Configuration = conf

  override def env: Environment = en

  override protected def executionContext: ExecutionContext = ec

  private val logger = getLogger(getClass)

  override def invokeBlock[A](request: Request[A], block: AuthRequest[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    val strideEnrolment = Enrolment(strideConf.strideRole)

    def redirectToStrideLogin(message: String) = {
      logger.warn(s"user is not authenticated - redirecting user to login: $message")
      val uri = if (request.host.contains("localhost")) s"http://${request.host}${request.uri}" else s"${request.uri}"
      toStrideLogin(uri)
    }

    authorised(strideEnrolment and AuthProviders(PrivilegedApplication)).retrieve(credentials) {
      case Some(c: Credentials) => block(new AuthRequest(request, c))
      case None =>
        Future successful redirectToStrideLogin("User does not have credentials")
    }.recover {
      case e: NoActiveSession =>
        redirectToStrideLogin(e.getMessage)
      case e: InternalError =>
        redirectToStrideLogin(e.getMessage)
      case e: AuthorisationException =>
        logger.warn(s"User is forbidden because of ${e.reason}, $e")
        Forbidden
    }
  }
}
