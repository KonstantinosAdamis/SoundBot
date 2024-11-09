import javax.sound.sampled._
import java.io.ByteArrayInputStream

object SoundBot {

  // Function to generate a sine wave tone
  def generateTone(frequency: Float, durationMs: Int, sampleRate: Float = 44100): Array[Byte] = {
    val numSamples = (durationMs * sampleRate / 1000).toInt
    val samples = new Array[Byte](numSamples)
    val amplitude = 127 // Max value for an 8-bit audio sample (range from -128 to 127)
    val twoPiF = 2.0 * Math.PI * frequency / sampleRate

    for (i <- 0 until numSamples) {
      val sample = (Math.sin(i * twoPiF) * amplitude).toByte
      samples(i) = sample
    }

    samples
  }

  // Function to play the sound
  def playSound(tone: Array[Byte], sampleRate: Float = 44100): Unit = {
    val format = new AudioFormat(sampleRate, 8, 1, true, false) // 8-bit mono audio format
    val dataLineInfo = new DataLine.Info(classOf[SourceDataLine], format)

    try {
      val line = AudioSystem.getLine(dataLineInfo).asInstanceOf[SourceDataLine]
      line.open(format)
      line.start()

      // Play the sound data
      line.write(tone, 0, tone.length)
      line.drain()
      line.stop()
      line.close()

    } catch {
      case e: LineUnavailableException =>
        println("Audio line unavailable: " + e.getMessage)
    }
  }

  // Main method to simulate the bot generating sounds
  def main(args: Array[String]): Unit = {
    println("Sound Bot is ready to generate sounds. Type 'exit' to stop.")

    var running = true
    while (running) {
      println("Enter frequency (Hz) and duration (ms), or 'exit' to quit:")
      val userInput = scala.io.StdIn.readLine()

      if (userInput.toLowerCase == "exit") {
        running = false
        println("Goodbye!")
      } else {
        try {
          val parts = userInput.split(" ")
          val frequency = parts(0).toFloat
          val duration = parts(1).toInt

          println(s"Generating sound with frequency: $frequency Hz, duration: $duration ms")

          // Generate and play the sound
          val tone = generateTone(frequency, duration)
          playSound(tone)

        } catch {
          case e: Exception =>
            println("Invalid input. Please enter valid frequency and duration.")
        }
      }
    }
  }
}
