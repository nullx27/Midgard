package tech.grimm.midgard.lib.scheduler

import me.jakejmattson.discordkt.Discord
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.lang.reflect.Method
import java.util.*

class Scheduler {
    val tasks = mutableListOf<Task>()

    private val reflections = Reflections(
        "tech.grimm.midgard.tasks", Scanners.SubTypes,
        Scanners.TypesAnnotated,
        Scanners.MethodsReturn
    )

    init {
        register<Schedule>()

        createSchedules()
        println("Scheduled tasks: ${tasks.count()}")
    }

    private inline fun <reified T : Scheduable> register() = reflections
        .get(Scanners.MethodsReturn.with(T::class.java).`as`(Method::class.java))
        .forEach {
            if (it is Method) {
                (it.invoke(null) as T).register(this)
            }
        }

    private fun createSchedules() {
        tasks.forEach {
            Timer().scheduleAtFixedRate(it.executor, it.interval, it.period)
        }
    }
}

internal interface Scheduable {
    fun register(scheduler: Scheduler)
}