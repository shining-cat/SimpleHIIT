package fr.shining_cat.simplehiit.ui.session

import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class SessionMapper @Inject constructor(private val hiitLogger: HiitLogger) {

    fun map(): SessionViewState {
        return SessionViewState.SessionLoading
    }
}