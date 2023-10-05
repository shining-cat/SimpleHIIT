package fr.shiningcat.simplehiit.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity.Companion.userSelectedColumnName
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity.Companion.usersTableName
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(user: UserEntity):Long

    @Query("SELECT * FROM $usersTableName")
    abstract fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM $usersTableName")
    abstract suspend fun getUsersList(): List<UserEntity>

    @Query("SELECT * FROM $usersTableName WHERE $userSelectedColumnName = true")
    abstract fun getSelectedUsers(): Flow<List<UserEntity>>

    @Update
    abstract suspend fun update(userEntity: UserEntity): Int

    @Delete
    abstract suspend fun delete(userEntity: UserEntity): Int

    @Query("DELETE FROM $usersTableName")
    abstract suspend fun deleteAllUsers()
}