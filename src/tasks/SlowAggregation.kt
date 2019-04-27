package tasks

import contributors.*

fun List<User>.aggregateSlow(): List<User> =
    aggregate()
        .also {
            // Imitate CPU consumption / blocking
            Thread.sleep(500)
        }