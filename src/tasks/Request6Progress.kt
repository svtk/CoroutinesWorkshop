package tasks

import contributors.*

suspend fun loadContributorsProgress(
    req: RequestData,
    updateResults: (List<User>, completed: Boolean) -> Unit
) {
    val service = createGitHubService(req.username, req.password)
    val repos = service.getOrgReposAsync(req.org).await()
    logRepos(req, repos)

    var allUsers = emptyList<User>()
    for (repo in repos) {
        val users = service.getRepoContributorsAsync(req.org, repo.name).await()
        logUsers(repo, users)
        allUsers = (allUsers + users).aggregate()
        updateResults(allUsers, false)
    }
    updateResults(allUsers, true)
}
