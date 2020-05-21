import java.io.{IOException, InputStream}

import zio._
import zio.stream.{ZStream, ZTransducer}

object Test extends zio.App {
  val resourceName = "input.cap"

  override def run(args: List[String]): URIO[zio.ZEnv, Int] = {
    def getResourceAsStream(name: String) = {
      Thread.currentThread().getContextClassLoader.getResourceAsStream(name)
    }

    val managedInputStream: ZManaged[Any, IOException, InputStream] = ZManaged.makeEffect(getResourceAsStream(resourceName))(_.close())
      .mapError({
        case io: IOException => io
        case ex: Throwable => new IOException(ex)
      })

    (for {
      _ <- ZStream.fromInputStreamManaged(managedInputStream)
        .transduce(ZTransducer.utf8Decode)
        .transduce[ZEnv, IOException, String](ZTransducer.splitLines)
        .zipWithIndex
        .tap(l => console.putStrLn(s"line ${l._2} of size ${l._1.length}"))
        .runDrain
    } yield ()).fold(_ => 1, _ => 0)
  }
}
