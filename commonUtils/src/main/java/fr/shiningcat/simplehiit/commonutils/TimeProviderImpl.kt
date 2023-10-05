package fr.shiningcat.simplehiit.commonutils

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport

// We can't mock System classes, so this wrapper won't be tested
// there is no logic though, and it's really only a wrapper that allows testing other usecases of System.currentTimeMillis
// see: https://github.com/mockk/mockk/issues/98
@ExcludeFromJacocoGeneratedReport
class TimeProviderImpl : TimeProvider {

    override fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
