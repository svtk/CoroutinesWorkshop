package tasks

import contributors.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun loadContributorsConcurrent(req: RequestData): List<User> = coroutineScope {
    val service = createGitHubService(req.username, req.password)
    val repos = service.getOrgReposAsync(req.org).await()
    logRepos(req, repos)

    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        async {
            val users = service.getRepoContributorsAsync(req.org, repo.name).await()
            logUsers(repo, users)
            users
        }
    }
    deferreds.awaitAll().flatten().aggregate()
}