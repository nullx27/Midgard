package tech.grimm.midgard.lib.scheduler

public fun schedule(name: String, construct: ScheduleBuilder.() -> Unit): Schedule = Schedule(name, construct)

public data class ScheduleBuilder(val scheduler: Scheduler, val name: String) {
    private val tasks = mutableListOf<Task>()

    public fun task(name: String, interval: Long, period: Long = 1000, action: Task.() -> Unit) {
        val task = Task(name, interval, period)
        task.action()
        tasks.add(task)
    }

    internal fun registerTasks() {
        scheduler.tasks.addAll(tasks)
    }
}

public class Schedule(private val name: String, private val collector: ScheduleBuilder.() -> Unit) : Scheduable {
    override fun register(scheduler: Scheduler) {
        val scheduleBuilder = ScheduleBuilder(scheduler, name)
        collector.invoke(scheduleBuilder)
        scheduleBuilder.registerTasks()
    }
}