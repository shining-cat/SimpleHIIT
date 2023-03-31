package fr.shining_cat.simplehiit.ui

import androidx.lifecycle.ViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger

abstract class AbstractLoggerViewModel(
    private val hiitLogger: HiitLogger
): ViewModel() {

    fun getLogger() = hiitLogger
    fun logD(tag: String, msg: String){
        hiitLogger.d(tag, msg)
    }

    fun logE(tag: String, msg: String, throwable: Throwable? = null){
        hiitLogger.e(tag, msg, throwable)
    }

}