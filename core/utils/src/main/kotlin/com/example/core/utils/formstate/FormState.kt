package com.example.core.utils.formstate

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormState<T>(initialValue: T) {
    private val _value = MutableStateFlow(initialValue)
    val value: StateFlow<T> = _value.asStateFlow()

    private val _errors = MutableStateFlow<Map<String, String?>>(emptyMap())
    val errors: StateFlow<Map<String, String?>> = _errors.asStateFlow()

    private val _isDirty = MutableStateFlow(false)
    val isDirty: StateFlow<Boolean> = _isDirty.asStateFlow()

    fun update(transform: (T) -> T) {
        _value.value = transform(_value.value)
        _isDirty.value = true
    }

    fun setError(field: String, message: String?) {
        _errors.update { current ->
            current.toMutableMap().apply {
                if (message == null) remove(field) else put(field, message)
            }
        }
    }

    fun clearErrors() { _errors.value = emptyMap() }

    fun reset(value: T) {
        _value.value = value
        _errors.value = emptyMap()
        _isDirty.value = false
    }

    val isValid: Boolean
        get() = _errors.value.values.none { it != null }

    fun errorFor(field: String): String? = _errors.value[field]
}
