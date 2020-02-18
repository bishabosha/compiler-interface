import Util._

lazy val scala212 = "2.12.10"

ThisBuild / headerLicense  := Some(HeaderLicense.Custom(
  """Scala compiler interface
    |
    |Copyright Lightbend, Inc. and Mark Harrah
    |
    |Licensed under Apache License 2.0
    |(http://www.apache.org/licenses/LICENSE-2.0).
    |
    |See the NOTICE file distributed with this work for
    |additional information regarding copyright ownership.
    |""".stripMargin
))

def commonSettings: Seq[Setting[_]] = Seq(
  Test / publishArtifact := false,
  crossPaths := false,
  compileOrder := CompileOrder.JavaThenScala,
  Compile / unmanagedSourceDirectories := Seq((Compile / javaSource).value),
  autoScalaLibrary := false,
  exportJars := true,
  headerLicense := (ThisBuild / headerLicense).value,
)

lazy val compilerInterfaceRoot = (project in file("."))
  .aggregate(compilerInterface)
  .settings(
    publish / skip := true,
    crossScalaVersions := Vector(),
    headerLicense := (ThisBuild / headerLicense).value,
    onLoadMessage := {
      """                                _ __                _       __            ____
        |    _________  ____ ___  ____  (_) /__  _____      (_)___  / /____  _____/ __/___ _________
        |   / ___/ __ \/ __ `__ \/ __ \/ / / _ \/ ___/_____/ / __ \/ __/ _ \/ ___/ /_/ __ `/ ___/ _ \
        |  / /__/ /_/ / / / / / / /_/ / / /  __/ /  /_____/ / / / / /_/  __/ /  / __/ /_/ / /__/  __/
        |  \___/\____/_/ /_/ /_/ .___/_/_/\___/_/        /_/_/ /_/\__/\___/_/  /_/  \__,_/\___/\___/
        |                     /_/
        |welcome to the build for sbt/compiler-interface.
        |""".stripMargin +
          (if (sys.props("java.specification.version") != "1.8")
            s"""!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
               |  Java version is ${sys.props("java.specification.version")}. We recommend 1.8.
               |!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!""".stripMargin
          else "")
    },
  )

// defines Java structures used across Scala versions, such as the API structures and relationships extracted by
//   the analysis compiler phases and passed back to sbt.  The API structures are defined in a simple
//   format from which Java sources are generated by the sbt-contraband plugin.
lazy val compilerInterface = (project in file("compiler-interface"))
  .enablePlugins(ContrabandPlugin)
  .settings(
    commonSettings,
    name := "Compiler Interface",
    scalaVersion := scala212,
    managedSourceDirectories in Compile +=
      baseDirectory.value / "src" / "main" / "contraband-java",
    sourceManaged in (Compile, generateContrabands) := baseDirectory.value / "src" / "main" / "contraband-java",
    crossPaths := false,
    autoScalaLibrary := false,
    mimaPreviousArtifacts := Set(
      "1.0.0", "1.0.1", "1.0.2", "1.0.3", "1.0.4", "1.0.5",
      "1.1.0", "1.1.1", "1.1.2", "1.1.3",
      "1.2.0", "1.2.1", "1.2.2",
    ) map (version =>
      organization.value %% moduleName.value % version
        cross (if (crossPaths.value) CrossVersion.binary else CrossVersion.disabled)
    ),
    mimaBinaryIssueFilters ++= {
      import com.typesafe.tools.mima.core._
      import com.typesafe.tools.mima.core.ProblemFilters._
      Seq(
        exclude[ReversedMissingMethodProblem]("xsbti.compile.ExternalHooks#Lookup.hashClasspath"),
        exclude[ReversedMissingMethodProblem]("xsbti.compile.ScalaInstance.loaderLibraryOnly"),
        exclude[DirectMissingMethodProblem]("xsbti.api.AnalyzedClass.of"),
        exclude[DirectMissingMethodProblem]("xsbti.api.AnalyzedClass.create"),
        exclude[ReversedMissingMethodProblem]("xsbti.AnalysisCallback.classesInOutputJar"),
        exclude[ReversedMissingMethodProblem]("xsbti.compile.IncrementalCompiler.compile"),
        exclude[DirectMissingMethodProblem]("xsbti.compile.IncrementalCompiler.compile")
      )
    },
  )

ThisBuild / organization := "org.scala-sbt"
ThisBuild / organizationName := "sbt"
ThisBuild / organizationHomepage := Some(url("https://www.scala-sbt.org/"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/sbt/compiler-interface"),
    "scm:git@github.com:sbt/compiler-interface.git"
  )
)
ThisBuild / developers := List(
  Developer("eed3si9n", "Eugene Yokota", "@eed3si9n", url("https://github.com/eed3si9n")),
)

ThisBuild / description := "a binary contract between Zinc and Scala compilers"
ThisBuild / licenses := List("Apache-2.0" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/sbt/compiler-interface"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true
