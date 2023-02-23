package fr.shining_cat.simplehiit.ui.session

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.ui.home.HomeViewState
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val mapper: SessionMapper,
    private val hiitLogger: HiitLogger

) : AbstractLoggerViewModel(hiitLogger) {

    private val _viewState = MutableStateFlow<SessionViewState>(SessionViewState.SessionLoading)
    val viewState = _viewState.asStateFlow()


}