package tech.grimm.midgard.lib.scheduler

import java.util.TimerTask
import kotlin.concurrent.timerTask

class Task(private val name: String, val interval: Long, val period: Long) {
    public lateinit var executor: TimerTask;

    fun execute(action: Task.() -> Unit): Unit {
        executor = timerTask { action.invoke(this@Task) }
    }
}