package tasks

import contributors.RequestData
import contributors.User
import kotlin.concurrent.thread

fun loadContributorsBackground(req: RequestData, callback: (List<User>) -> Unit) {
    thread {
        loadContributorsBlocking(req)
    }
}