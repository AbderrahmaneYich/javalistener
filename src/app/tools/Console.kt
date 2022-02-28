package app.tools

import config.Env
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.LogManager
import java.util.logging.Logger

object Console {
    @JvmStatic
    val animationChars = charArrayOf('|', '/', '-', '\\')

    object Colors {
        @JvmStatic
        val RESET = "\u001B[0m"

        @JvmStatic
        val RED = "\u001B[31m"

        @JvmStatic
        val CYAN = "\u001B[36m"

        @JvmStatic
        val GREEN = "\u001B[32m"

        //    @JvmStatic
//    val BLACK = "\u001B[30m"
        @JvmStatic
        val YELLOW = "\u001B[33m"
//    @JvmStatic
//    val BLUE = "\u001B[34m"
//    @JvmStatic
//    val PURPLE = "\u001B[35m"
//    @JvmStatic
//    val WHITE = "\u001B[37m"

    }

    @JvmStatic
    fun initLogger(activateLogFile: Boolean) {
        LogManager.getLogManager().reset()
        val globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
        globalLogger.level = java.util.logging.Level.OFF
        if (activateLogFile) {
            try {
                val outFilePath = "info-listener.log"
                val errFilePath = "error-listener.log"
                val out = PrintStream(FileOutputStream("${Env.getLoggerPath()}/$outFilePath"))
                val err = PrintStream(FileOutputStream("${Env.getLoggerPath()}/$errFilePath"))
                println("outFilePath:" + Env.getLoggerPath() + "/" + outFilePath)
                println("errFilePath:" + Env.getLoggerPath() + "/" + errFilePath)
                System.setOut(out)
                System.setErr(err)
            } catch (e: FileNotFoundException) {
                printStackTrace(e)
            }
        }
    }

    @JvmStatic
    fun redText(text: String): String {
        return Colors.RED + text + Colors.RESET
    }

    @JvmStatic
    fun println(msg: String, color: String) {
        kotlin.io.println(if (color !== "no Colors") color + msg + Colors.RESET else msg)
    }

    @JvmStatic
    fun print(msg: String, color: String) {
        kotlin.io.print(
                if (color !== "no Colors") color + msg + Colors.RESET else msg
        )
    }

    @JvmStatic
    fun printInSameLine(msg: String, color: String) {
        kotlin.io.print((if (color !== "no Colors") color + msg + Colors.RESET else msg) + "\r")
//        val lines = msg.split("\n").size
//        var msgR = if (color !== "no Colors") color + msg + Colors.RESET else msg
////        var msgR = msg
//        for (it in 1..lines) msgR += " \r"
//        System.out.print(msgR)

    }

    @JvmStatic
    fun printlnWithDate(msg: String, color: String) {
        val dt1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        println(dt1.format(Date()) + " : " + msg, color)
    }

    @JvmStatic
    fun println(msg: String) {
        println(msg, "no Colors")
    }

    @JvmStatic
    fun errPrintln(msg: String) {
        System.err.println(msg)
    }

    @JvmStatic
    fun printStackTrace(e: Exception) {
        e.printStackTrace();
    }

    @JvmStatic
    fun print(msg: String) {
        print(msg, "no Colors")
    }

    @JvmStatic
    fun printlnWithDate(msg: String) {
        printlnWithDate(msg, "no Colors")
    }

    internal object Mute {
        @JvmStatic
        var webResourcePost = false
    }
}
