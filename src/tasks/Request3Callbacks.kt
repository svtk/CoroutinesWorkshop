package tasks

import contributors.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun loadContributorsCallbacks(req: RequestData, callback: (List<User>) -> Unit) {
    val service = createGitHubService(req.username, req.password)
    service.getOrgRepos(req.org).onSuccessfulResponse { repos ->
        logRepos(req, repos)
        val allContributors = mutableListOf<User>()
        for (repo in repos) {
            service.getRepoContributors(req.org, repo.name).onSuccessfulResponse { contributors ->
                logUsers(repo, contributors)
                allContributors += contributors
            }
        }
        // TODO: Why this code doesn't work? How to fix that?
        callback(allContributors.aggregate())
    }
}

inline fun <T> Call<T>.onSuccessfulResponse(crossinline callback: (T) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            checkResponse(response)
            callback(response.body()!!)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            log.error("Call failed", t)
        }
    })
}
