package com.example.favdish.utils

object Constants {
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
    const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

    const val EXTRA_DISH_DETAILS: String = "DishDetails"

    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION: String = "FilterSelection"
    const val API_ENDPOINT: String = "recipes/random"

    const val API_KEY: String = "apiKey"
    const val LIMIT_LICENSE: String = "limitLicense"
    const val TAGS: String = "tags"
    const val NUMBER: String = "number"
    const val BASE_URL = "https://api.spoonacular.com/"
    const val API_KEY_VALUE: String = "5dc515fe380e484d953f5d7fcf16173e"

    const val LIMIT_LICENSE_VALUE: Boolean = true
    const val TAGS_VALUE: String = "vegetarian,dessert"
    const val NUMBER_VALUE: Int = 1

    fun dishTypes(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("breakfast")
        list.add("lunch")
        list.add("snack")
        list.add("dinner")
        list.add("salad")
        list.add("slide dish")
        list.add("dessert")
        list.add("other")
        return list
    }

    fun dishCategory(): List<String> {
        return listOf(
            "BBQ",
            "Bakery",
            "Burger",
            "Cafe",
            "Chicken",
            "Dessert",
            "Drinks",
            "Hot Dogs",
            "Juices",
            "Sandwich",
            "Tea & Coffee",
            "Wraps",
            "Other"
        )
    }

    fun dishCookTime(): List<String> {
        return listOf(
            "10",
            "15",
            "20",
            "30",
            "45",
            "50",
            "60",
            "90",
            "120",
            "150",
            "180"
        )
    }

}