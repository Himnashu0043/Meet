package org.meetcute.network.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.meetcute.network.data.model.User

interface UserDao {

//    @Query("SELECT * FROM user_table LIMIT 1")
//    fun getUser(): Flow<User?>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(user: User)
}