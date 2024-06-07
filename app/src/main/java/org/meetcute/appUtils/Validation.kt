package org.meetcute.appUtils

import android.util.Patterns
import java.util.regex.Pattern

object Validation {

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    fun isValidMobile(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length in 7..13
        } else {
            false
        }
    }

}