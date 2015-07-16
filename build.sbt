import chrome.Impl._
import chrome.permissions.APIPermission._
import net.lullabyte.{Chrome, ChromeSbtPlugin}

lazy val root = project.in(file("."))
  .enablePlugins(ChromeSbtPlugin)
  .settings(
    name := "System Monitor",
    version := "0.1.0",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-language:implicitConversions",
      "-language:existentials",
      "-Xlint",
      "-deprecation",
      "-Xfatal-warnings",
      "-feature"
    ),
    persistLauncher := true,
    persistLauncher in Test := false,
    relativeSourceMaps := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.0" withSources() withJavadoc(),
      "com.github.japgolly.scalajs-react" %%% "core" % "0.9.1" withSources() withJavadoc(),
      "com.github.japgolly.scalajs-react" %%% "extra" % "0.9.1" withSources() withJavadoc(),
      "com.github.japgolly.scalacss" %%% "core" % "0.3.0" withSources() withJavadoc(),
      "com.github.japgolly.scalacss" %%% "ext-react" % "0.3.0" withSources() withJavadoc(),
      "net.lullabyte" %%% "scala-js-chrome" % "0.2.0" withSources() withJavadoc()
    ),
    jsDependencies += "org.webjars" % "react" % "0.13.3" / "react-with-addons.min.js" commonJSName "React",
    skip in packageJSDependencies := false,
    chromeManifest := AppManifest(
      name = name.value,
      version = version.value,
      app = App(
        background = Background(
          scripts = List("deps.js", "main.js", "launcher.js")
        )
      ),
      defaultLocale = Some("en"),
      icons = Chrome.icons(
        "assets/icons",
        "app.png",
        Set(16, 32, 48, 64, 96, 128, 256, 512)
      ),
      permissions = Set(
        System.CPU,
        System.Display,
        System.Memory,
        System.Network,
        Storage
      )
    )
  )


