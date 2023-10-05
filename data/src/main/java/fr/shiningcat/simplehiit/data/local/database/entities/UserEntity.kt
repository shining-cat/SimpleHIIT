package fr.shiningcat.simplehiit.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity.Companion.usersTableName

@ExcludeFromJacocoGeneratedReport
@Entity(tableName = usersTableName)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = userIdColumnName, index = true)
    val userId: Long = 0L,
    @ColumnInfo(name = userNameColumnName) val name: String,
    @ColumnInfo(name = userSelectedColumnName) val selected: Boolean
) {

    companion object {
        const val usersTableName = "simple_hiit_users"

        //
        const val userIdColumnName = "userId"
        const val userNameColumnName = "name"
        const val userSelectedColumnName = "selected"
    }
}
