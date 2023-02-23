package fr.shining_cat.simplehiit.ui.session

import fr.shining_cat.simplehiit.domain.models.User

sealed class SessionViewState{
    object SessionLoading:SessionViewState()
    data class SessionErrorState(val errorCode: String):SessionViewState()
}
