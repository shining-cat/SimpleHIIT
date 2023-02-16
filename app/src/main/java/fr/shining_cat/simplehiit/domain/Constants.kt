package fr.shining_cat.simplehiit.domain

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
object Constants {

    const val NO_RESULTS_FOUND = "no results found"

    enum class Errors{
        DATABASE_FETCH_FAILED,
        DATABASE_INSERT_FAILED,
        DATABASE_DELETE_FAILED,
        DATABASE_UPDATE_FAILED,
        NO_USER_PROVIDED,
        EMPTY_RESULT
    }
}