package ma.ensaj.pets.session

import android.content.Context

class SessionManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("PetAppPrefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }

    fun saveAuthToken(token: String) {
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun saveUserId(userId: Long) {
        editor.putLong(USER_ID, userId)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }

    fun fetchUserId(): Long {
        return sharedPreferences.getLong(USER_ID, -1)
    }

    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}