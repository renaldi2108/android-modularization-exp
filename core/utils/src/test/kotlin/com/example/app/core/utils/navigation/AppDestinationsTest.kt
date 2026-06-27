package com.example.app.core.utils.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class AppDestinationsTest {

    @Test
    fun `rute fitur memiliki nilai stabil`() {
        assertEquals("dashboard", AppDestinations.Dashboard)
        assertEquals("products", AppDestinations.Products)
        assertEquals("users", AppDestinations.Users)
        assertEquals("posts", AppDestinations.Posts)
        assertEquals("todos", AppDestinations.Todos)
        assertEquals("quotes", AppDestinations.Quotes)
        assertEquals("carts", AppDestinations.Carts)
        assertEquals("recipes", AppDestinations.Recipes)
        assertEquals("comments", AppDestinations.Comments)
    }
}
