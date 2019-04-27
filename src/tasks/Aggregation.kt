package tasks

import contributors.User

// TODO: Write aggregation code.
//  In the initial list each user is present several times, once for each
//  repository he or she contributed to.
//  Merge duplications: each user should be present only once in the resulting list
//  with the total value of contributions for all the repositories.
//  Users should be sorted in a descending order by their contributions.
fun List<User>.aggregate(): List<User> =
    this

// Example
fun main() {
    val actual = listOf(
        User("Alice", 1), User("Bob", 3),
        User("Alice", 2), User("Bob", 7),
        User("Charlie", 3), User("Alice", 5)
    ).aggregate()
    val expected = listOf(
        User("Bob", 10),
        User("Alice", 8),
        User("Charlie", 3))
    check(actual == expected) {
        "\nExpected: $expected\nActual: $actual"
    }
}