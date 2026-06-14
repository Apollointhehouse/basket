package kyta.composter

import kyta.composter.protocol.Protocol.bootstrap
import kyta.composter.server.MinecraftServer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    Composter.init()
}

object Composter {
    val server = MinecraftServer(this)
    private var currentTick: Long = 0

    fun init() {

        val tickLoop = Executors.newSingleThreadScheduledExecutor()
        tickLoop.scheduleAtFixedRate({
            val tickStart = System.currentTimeMillis()
            currentTick++

            try {
                server.tick(currentTick)
            } catch (x: Throwable) {
                server.logger.error("an error occurred while ticking the server (tick #{})", currentTick, x)
            }
            server.logger.debug("tick #{} ended in {}ms.", currentTick, (System.currentTimeMillis() - tickStart))
        }, 0, 50, TimeUnit.MILLISECONDS)

        // start server
        bootstrap()
        server.startServer()
    }
}
