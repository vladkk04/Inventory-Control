package com.example.inventorycotrol.domain.usecase.productCategory

data class ProductCategoryUseCases(
    val createCategory: CreateProductCategoryUseCase,
    val deleteCategory: DeleteProductCategoryUseCase,
    val updateCategory: UpdateProductCategoryUseCase,
    val getCategories: GetProductCategoriesUseCase,
)