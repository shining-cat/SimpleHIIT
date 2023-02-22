package fr.shining_cat.simplehiit.ui.statistics

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val hiitLogger: HiitLogger

) : AbstractLoggerViewModel(hiitLogger) {


}