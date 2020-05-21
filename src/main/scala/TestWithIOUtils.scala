import java.io.File
import java.nio.charset.StandardCharsets

import org.apache.commons.io.{FileUtils, IOUtils}

object TestWithIOUtils {
  val resourceName = "input.cap"

  def main(args: Array[String]): Unit = {
    def getResourceAsStream(name: String) = {
      Thread.currentThread().getContextClassLoader.getResourceAsStream(name)
    }

    IOUtils.readLines(getResourceAsStream(resourceName), StandardCharsets.UTF_8).forEach(line => println(s"line of size ${line.length}"))
    val all = FileUtils.readFileToString(new File(Thread.currentThread().getContextClassLoader.getResource(resourceName).toURI), StandardCharsets.UTF_8)
    println(s"loading file into memory: ${all.length}")
  }
}
