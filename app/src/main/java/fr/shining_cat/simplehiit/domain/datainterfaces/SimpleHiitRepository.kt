package fr.shining_cat.simplehiit.domain.datainterfaces

import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User

@ExcludeFromJacocoGeneratedReport
interface SimpleHiitRepository {

    suspend fun insertUser(user:User): Output<Long>
    suspend fun getUsers():Output<List<User>>
    suspend fun updateUser(user:User):Output<Int>
    suspend fun deleteUser(user:User):Output<Int>
    //
    suspend fun insertSession(session: Session): Output<Int>
    suspend fun getSessionsForUser(user: User):Output<List<Session>>
}