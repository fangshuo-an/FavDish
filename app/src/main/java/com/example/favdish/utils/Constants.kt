package com.example.favdish.utils

object Constants {
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
    const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

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