package contributors

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("Contributors")

fun logRepos(req: RequestData, repos: List<Repo>) {
    log.info("${req.org}: loaded ${repos.size} repos")
}

fun logUsers(repo: Repo, users: List<User>) {
    log.info("${repo.name}: loaded ${users.size} contributors")
}