package tasks

import contributors.*
import retrofit2.Call
import retrofit2.Response

fun loadContributorsBlocking(req: RequestData) : List<User> {
    val service = createGitHubService(req.username, req.password)
    val repos = service.getOrgRepos(req.org).getResponseBodyBlocking()
    logRepos(req, repos)

    return repos.flatMap { repo ->
        val users = service.getRepoContributors(req.org, repo.name)
            .getResponseBodyBlocking()
        logUsers(repo, users)
        users
    }.aggregate()
}

fun <T> Call<T>.getResponseBodyBlocking(): T {
    return execute() // Executes requests and blocks current thread
        .also { checkResponse(it) }
        .body()!!
}

fun checkResponse(response: Response<*>) {
    check(response.isSuccessful) {
        "Failed with ${response.code()}: ${response.message()}\n${response.errorBody()?.string()}"
    }
}
