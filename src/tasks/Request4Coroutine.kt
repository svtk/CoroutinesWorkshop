package tasks

import contributors.*

suspend fun loadContributors(req: RequestData): List<User> {
    val service = createGitHubService(req.username, req.password)
    val repos = service.getOrgReposAsync(req.org).await()
    logRepos(req, repos)

    return repos.flatMap { repo ->
        val users = service.getRepoContributorsAsync(req.org, repo.name).await()
        logUsers(repo, users)
        users
    }.aggregate()
}