package fr.shining_cat.simplehiit.utils

class TimeProviderImpl:TimeProvider {

    override fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}