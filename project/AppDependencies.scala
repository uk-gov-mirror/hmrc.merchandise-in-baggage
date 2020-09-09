import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-27"  % "2.24.0",
    "uk.gov.hmrc"             %% "simple-reactivemongo"       % "7.30.0-play-27",
    "uk.gov.hmrc"             %% "bootstrap-frontend-play-27" % "2.24.0",
    "uk.gov.hmrc"             %% "play-frontend-hmrc"         % "0.17.0-play-27",
    "uk.gov.hmrc"             %% "play-frontend-govuk"        % "0.49.0-play-27",
    "uk.gov.hmrc"             %% "play-ui"                    % "8.3.0-play-26",
    "com.github.pureconfig"   %% "pureconfig"                 % "0.13.0",
    "org.typelevel"           %% "cats-core"                  % "2.0.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-27"   % "2.24.0"  % Test,
    "org.scalatest"           %% "scalatest"                % "3.1.2"   % Test,
    "com.typesafe.play"       %% "play-test"                % current   % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"             % "0.35.10" % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "4.0.3"   % "test, it",
    "de.bwaldvogel"           %  "mongo-java-server"        % "1.34.0" % Test
  )
}
