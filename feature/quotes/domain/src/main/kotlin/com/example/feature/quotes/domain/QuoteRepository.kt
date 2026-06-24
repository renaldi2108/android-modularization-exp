package com.example.feature.quotes.domain

interface QuoteRepository {
    suspend fun getQuotes(limit: Int = 30): List<Quote>
}
