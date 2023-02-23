package fr.shining_cat.simplehiit.ui.statistics

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.GetAllUsersUseCase
import fr.shining_cat.simplehiit.domain.usecases.GetStatsForUserUseCase
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getStatsForUserUseCase: GetStatsForUserUseCase,
    private val mapper: StatisticsMapper,
    private val hiitLogger: HiitLogger
) : AbstractLoggerViewModel(hiitLogger) {

    private val _statisticsViewState = MutableStateFlow<StatisticsViewState>(StatisticsViewState.StatisticsLoading)
    val statisticsViewState = _statisticsViewState.asStateFlow()
    private val _allUsersViewState = MutableStateFlow<List<User>>(emptyList())
    val allUsersViewState = _allUsersViewState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllUsersUseCase.execute().collect(){
                when(it){
                    is Output.Success -> {
                        _allUsersViewState.emit(it.result)
                        retrieveStatsForUser(it.result.firstOrNull())
                    }
                    is Output.Error -> {
                        _statisticsViewState.emit(StatisticsViewState.StatisticsError(it.errorCode.code))
                    }
                }
            }
        }
    }

    fun retrieveStatsForUser(user: User?){
        viewModelScope.launch {
            if(user == null){
                hiitLogger.e("StatisticsViewModel", "retrieveStatsForUser::called with NULL User")
                _statisticsViewState.emit(StatisticsViewState.StatisticsError(Constants.Errors.NO_USER_PROVIDED.code))
            } else {
                val now = System.currentTimeMillis()
                val statistics = getStatsForUserUseCase.execute(user = user, now = now)
                _statisticsViewState.emit(mapper.map(statistics))
            }
        }
    }

}