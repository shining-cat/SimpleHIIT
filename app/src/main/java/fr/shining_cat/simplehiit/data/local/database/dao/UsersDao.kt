package fr.shining_cat.simplehiit.data.local.database.dao

import androidx.room.*
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity.Companion.userSelectedColumnName
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity.Companion.usersTableName

@ExcludeFromJacocoGeneratedReport
@Dao
abstract class UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(user: UserEntity):Long

    @Query("SELECT * FROM $usersTableName")
    abstract suspend fun getUsers(): List<UserEntity>

    @Query("SELECT * FROM $usersTableName WHERE $userSelectedColumnName = true")
    abstract suspend fun getSelectedUsers(): List<UserEntity>

    @Update
    abstract suspend fun update(userEntity: UserEntity): Int

    @Delete
    abstract suspend fun delete(userEntity: UserEntity): Int

}