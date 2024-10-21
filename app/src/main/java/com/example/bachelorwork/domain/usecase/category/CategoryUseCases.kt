package com.example.bachelorwork.domain.usecase.category

data class CategoryUseCases(
    val createCategory: CreateCategoryUseCase,
    val getCategory: GetCategoryUseCase,
    val deleteCategory: DeleteCategoryUseCase,
)