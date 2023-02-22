package fr.shining_cat.simplehiit.ui.session

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val hiitLogger: HiitLogger

) : AbstractLoggerViewModel(hiitLogger) {


}