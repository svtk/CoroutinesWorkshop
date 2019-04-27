package tasks

import contributors.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun loadContributorsChannels(
    req: RequestData,
    callback: suspend (List<User>, completed: Boolean) -> Unit
) = coroutineScope {

    val service = createGitHubService(req.username, req.password)
    val repos = service.getOrgReposAsync(req.org).await()
    logRepos(req, repos)

    val channel = Channel<List<User>>()
    for (repo in repos) {
        launch {
            val users = service.getRepoContributorsAsync(req.org, repo.name).await()
            logUsers(repo, users)
            channel.send(users)
        }
    }

    var allUsers = emptyList<User>()
    repeat(repos.size) {
        val users = channel.receive()
        allUsers = (allUsers + users).aggregate()
        callback(allUsers, it == repos.lastIndex)
    }
}
