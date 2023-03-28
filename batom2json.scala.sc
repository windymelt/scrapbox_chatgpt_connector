#!/usr/bin/env -S scala-cli shebang -S 3

// usage: batom2json.scala.sc ~/Downloads/Windymelt.bookmarks.atom > from_bookmark/Windymelt.json
// You need to install scala-cli from https://scala-cli.virtuslab.org/install

//> using scala "3.2.2"
//> using lib "io.circe::circe-core:0.14.5"
//> using lib "io.circe::circe-literal:0.14.5"
//> using lib "io.circe::circe-parser:0.14.5"
//> using lib "org.scala-lang.modules::scala-xml:2.1.0"

val doc = scala.xml.XML.loadFile(args.head)

val elem2titleLine: scala.xml.Node => Option[(String, String)] =
  case e: scala.xml.Elem => Some((e \ "title").text -> (e \ "summary").text)
  case _ => None

import io.circe.literal._

val entries = (doc \\ "feed" \\ "entry")
  .map(elem2titleLine)
  .flatten
  .map { case (t, l) =>
    json"""{"title":$t, "lines":$l}"""
  }

val result = json"""{"pages": $entries}"""
import io.circe.syntax._

println(result.noSpaces)
