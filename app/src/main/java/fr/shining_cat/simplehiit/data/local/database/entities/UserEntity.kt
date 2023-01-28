package fr.shining_cat.simplehiit.data.local.database.entities

import androidx.room.*
import fr.shining_cat.simplehiit.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity.Companion.usersTableName

@ExcludeFromJacocoGeneratedReport
@Entity(tableName = usersTableName)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = userIdColumnName, index = true) val userId: Long = 0L,
    @ColumnInfo(name = userNameColumnName) val name: String
) {

    companion object {
        const val usersTableName = "simple_hiit_users"
        //
        const val userIdColumnName = "userId"
        const val userNameColumnName = "name"
    }
}