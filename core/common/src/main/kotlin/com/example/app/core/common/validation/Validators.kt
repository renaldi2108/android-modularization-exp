package com.example.app.core.common.validation

object Validators {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun email(value: String): String? =
        if (value.isNotBlank() && !EMAIL_REGEX.matches(value)) "Format email tidak valid" else null

    fun password(value: String, minLength: Int = 8): String? =
        if (value.isNotBlank() && value.length < minLength) "Password minimal $minLength karakter" else null

    fun required(value: String, label: String = "Field"): String? =
        if (value.isBlank()) "$label wajib diisi" else null

    fun allValid(errors: List<String?>, requiredFilled: List<String>): Boolean =
        errors.all { it == null } && requiredFilled.all { it.isNotBlank() }
}
