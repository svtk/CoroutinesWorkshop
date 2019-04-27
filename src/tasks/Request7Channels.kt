package tasks

import contributors.*

suspend fun loadContributorsChannels(
    req: RequestData,
    callback: suspend (List<User>, completed: Boolean) -> Unit
) {
    TODO()
}
