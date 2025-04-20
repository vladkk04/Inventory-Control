package com.example.bachelorwork.di

import android.content.Context
import com.example.bachelorwork.data.local.AppDatabase
import com.example.bachelorwork.data.local.repositories.OrderLocalDataSourceImpl
import com.example.bachelorwork.data.local.repositories.OrganisationLocalDataSourceImpl
import com.example.bachelorwork.data.local.repositories.OrganisationUserLocalDataSourceImpl
import com.example.bachelorwork.data.local.repositories.ProductCategoryLocalDataSourceImpl
import com.example.bachelorwork.data.local.repositories.ProductLocalDataSourceImpl
import com.example.bachelorwork.data.local.repositories.UserLocalDataSourceImpl
import com.example.bachelorwork.data.remote.repositories.FileRepositoryImpl
import com.example.bachelorwork.data.remote.repositories.ProductUpdateStockRemoteDataSourceImpl
import com.example.bachelorwork.data.remote.repositories.ProfileRemoteDataSourceImpl
import com.example.bachelorwork.data.remote.repositories.UserRemoteDataSourceImpl
import com.example.bachelorwork.data.remote.repositories.organisation.OrderRemoteDataSourceImpl
import com.example.bachelorwork.data.remote.repositories.organisation.OrganisationRemoteDataSourceImpl
import com.example.bachelorwork.data.remote.repositories.organisation.OrganisationUserRemoteDataSourceImpl
import com.example.bachelorwork.data.remote.repositories.organisation.ProductCategoryRemoteDataSourceImpl
import com.example.bachelorwork.data.remote.services.FileApiService
import com.example.bachelorwork.data.remote.services.OrderApiService
import com.example.bachelorwork.data.remote.services.OrganisationApiService
import com.example.bachelorwork.data.remote.services.OrganisationUserApiService
import com.example.bachelorwork.data.remote.services.ProductCategoryApiService
import com.example.bachelorwork.data.remote.services.ProductUpdateStockApiService
import com.example.bachelorwork.data.remote.services.ProfileApiService
import com.example.bachelorwork.data.remote.services.UserApiService
import com.example.bachelorwork.data.repositories.BarcodeScannerRepositoryImpl
import com.example.bachelorwork.domain.manager.DataStoreManager
import com.example.bachelorwork.domain.repository.local.BarcodeScannerRepository
import com.example.bachelorwork.domain.repository.local.OrderLocalDataSource
import com.example.bachelorwork.domain.repository.local.OrganisationLocalDataSource
import com.example.bachelorwork.domain.repository.local.OrganisationUserLocalDataSource
import com.example.bachelorwork.domain.repository.local.ProductCategoryLocalDataSource
import com.example.bachelorwork.domain.repository.local.ProductLocalDataSource
import com.example.bachelorwork.domain.repository.local.UserLocalDataSource
import com.example.bachelorwork.domain.repository.remote.FileRepository
import com.example.bachelorwork.domain.repository.remote.OrderRemoteDataSource
import com.example.bachelorwork.domain.repository.remote.OrganisationRemoteDataSource
import com.example.bachelorwork.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.bachelorwork.domain.repository.remote.ProductCategoryRemoteDataSource
import com.example.bachelorwork.domain.repository.remote.ProductRemoteDataSource
import com.example.bachelorwork.domain.repository.remote.ProductUpdateStockRemoteDataSource
import com.example.bachelorwork.domain.repository.remote.ProfileRemoteDataSource
import com.example.bachelorwork.domain.repository.remote.UserRemoteDataSource
import com.example.bachelorwork.domain.usecase.file.FileUseCases
import com.example.bachelorwork.domain.usecase.file.GetFilesUseCase
import com.example.bachelorwork.domain.usecase.file.UploadFileUseCase
import com.example.bachelorwork.domain.usecase.order.CreateOrderUseCase
import com.example.bachelorwork.domain.usecase.order.DeleteOrderUseCase
import com.example.bachelorwork.domain.usecase.order.GetOrdersUseCase
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.order.UpdateOrderUseCase
import com.example.bachelorwork.domain.usecase.organisation.CreateOrganisationUseCase
import com.example.bachelorwork.domain.usecase.organisation.DeleteOrganisationUseCase
import com.example.bachelorwork.domain.usecase.organisation.GetOrganisationsUseCase
import com.example.bachelorwork.domain.usecase.organisation.OrganisationUseCases
import com.example.bachelorwork.domain.usecase.organisation.SwitchOrganisationUseCase
import com.example.bachelorwork.domain.usecase.organisationUser.AssignRoleOrganisationUser
import com.example.bachelorwork.domain.usecase.organisationUser.ChaneStatusOrganisationUserUseCase
import com.example.bachelorwork.domain.usecase.organisationUser.DeleteOrganisationUser
import com.example.bachelorwork.domain.usecase.organisationUser.GetOrganisationUsersUseCase
import com.example.bachelorwork.domain.usecase.organisationUser.InvitationUserUseCase
import com.example.bachelorwork.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.bachelorwork.domain.usecase.organisationUser.UpdateOrganisationUser
import com.example.bachelorwork.domain.usecase.product.CreateProductUseCase
import com.example.bachelorwork.domain.usecase.product.DeleteProductUseCase
import com.example.bachelorwork.domain.usecase.product.GetProductsUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.product.UpdateProductUseCase
import com.example.bachelorwork.domain.usecase.productCategory.CreateProductCategoryUseCase
import com.example.bachelorwork.domain.usecase.productCategory.DeleteProductCategoryUseCase
import com.example.bachelorwork.domain.usecase.productCategory.GetProductCategoriesUseCase
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.domain.usecase.productCategory.UpdateProductCategoryUseCase
import com.example.bachelorwork.domain.usecase.productUpdateStock.GetStockUseCase
import com.example.bachelorwork.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.bachelorwork.domain.usecase.productUpdateStock.UpdateStockUseCase
import com.example.bachelorwork.domain.usecase.profile.AcceptOrganisationInvitation
import com.example.bachelorwork.domain.usecase.profile.DeclineOrganisationInvitation
import com.example.bachelorwork.domain.usecase.profile.GetOrganisationsInviting
import com.example.bachelorwork.domain.usecase.profile.GetProfileUseCase
import com.example.bachelorwork.domain.usecase.profile.ProfileUseCases
import com.example.bachelorwork.domain.usecase.user.CreateUserUseCase
import com.example.bachelorwork.domain.usecase.user.GetUserUseCase
import com.example.bachelorwork.domain.usecase.user.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideFileRepository(
        api: FileApiService,
        @ApplicationContext context: Context
    ): FileRepository = FileRepositoryImpl(api, context)

    @Provides
    @ViewModelScoped
    fun provideFileUseCases(
        repository: FileRepository
    ) = FileUseCases(
        getFilesUseCase = GetFilesUseCase(repository),
        uploadFileUseCase = UploadFileUseCase(repository)
    )


    @Provides
    @ViewModelScoped
    fun provideBarcodeScannerRepository(
        @ApplicationContext applicationContext: Context
    ): BarcodeScannerRepository = BarcodeScannerRepositoryImpl(applicationContext)


    @Provides
    @ViewModelScoped
    fun provideProductUpdateStockRemoteDataSource(
        api: ProductUpdateStockApiService,
        dataStoreManager: DataStoreManager
    ): ProductUpdateStockRemoteDataSource =
        ProductUpdateStockRemoteDataSourceImpl(api, dataStoreManager)

    @Provides
    @ViewModelScoped
    fun provideProductUpdateStockUseCases(
        remote: ProductUpdateStockRemoteDataSource,
    ): ProductUpdateStockUseCases =
        ProductUpdateStockUseCases(
            updateStock = UpdateStockUseCase(remote),
            getStock = GetStockUseCase(remote)
        )


    @Provides
    @ViewModelScoped
    fun provideProductRepository(db: AppDatabase): ProductLocalDataSource =
        ProductLocalDataSourceImpl(db.productDao)


    @Provides
    @ViewModelScoped
    fun provideProductUseCases(
        remote: ProductRemoteDataSource,
        local: ProductLocalDataSource
    ): ProductUseCases =
        ProductUseCases(
            createProduct = CreateProductUseCase(remote, local),
            updateProduct = UpdateProductUseCase(remote, local),
            deleteProduct = DeleteProductUseCase(remote, local),
            getProducts = GetProductsUseCase(remote, local)
        )

    @Provides
    @ViewModelScoped
    fun provideProductCategoryLocalRepository(db: AppDatabase): ProductCategoryLocalDataSource =
        ProductCategoryLocalDataSourceImpl(db.productCategoryDao)

    @Provides
    @ViewModelScoped
    fun provideProductCategoryUseCases(
        local: ProductCategoryLocalDataSource,
        remote: ProductCategoryRemoteDataSource
    ): ProductCategoryUseCases =
        ProductCategoryUseCases(
            createCategory = CreateProductCategoryUseCase(local, remote),
            deleteCategory = DeleteProductCategoryUseCase(remote, local),
            updateCategory = UpdateProductCategoryUseCase(remote, local),
            getCategories = GetProductCategoriesUseCase(remote, local)
        )

    @Provides
    @ViewModelScoped
    fun provideOrderRemoteDataSource(
        api: OrderApiService,
        dataStoreManager: DataStoreManager
    ): OrderRemoteDataSource = OrderRemoteDataSourceImpl(api, dataStoreManager)

    @Provides
    @ViewModelScoped
    fun provideOrderRepository(db: AppDatabase): OrderLocalDataSource =
        OrderLocalDataSourceImpl(db.orderDao)

    @Provides
    @ViewModelScoped
    fun provideOrderUseCases(
        remote: OrderRemoteDataSource,
        repository: OrderLocalDataSource
    ): OrderUseCases =
        OrderUseCases(
            createOrder = CreateOrderUseCase(remote, repository),
            updateOrder = UpdateOrderUseCase(remote, repository),
            deleteOrder = DeleteOrderUseCase(remote, repository),
            getOrders = GetOrdersUseCase(remote, repository)
        )

    @Provides
    @ViewModelScoped
    fun provideOrganisationRemoteRepositoryRepository(
        api: OrganisationApiService,
        dataStoreManager: DataStoreManager
    ): OrganisationRemoteDataSource = OrganisationRemoteDataSourceImpl(api, dataStoreManager)

    @Provides
    @ViewModelScoped
    fun provideOrganisationRepository(db: AppDatabase): OrganisationLocalDataSource =
        OrganisationLocalDataSourceImpl(db.organisationDao)

    @Provides
    @ViewModelScoped
    fun provideOrganisationUseCases(
        localOrganisationUser: OrganisationUserLocalDataSource,
        remoteOrganisationUser: OrganisationUserRemoteDataSource,
        local: OrganisationLocalDataSource,
        remote: OrganisationRemoteDataSource
    ): OrganisationUseCases =
        OrganisationUseCases(
            create = CreateOrganisationUseCase(localOrganisationUser, remoteOrganisationUser, remote, local),
            delete = DeleteOrganisationUseCase(remote, local),
            get = GetOrganisationsUseCase(remote, local),
            switch = SwitchOrganisationUseCase(remote)
        )


    @Provides
    @ViewModelScoped
    fun provideProductCategoryRemoteRepository(
        api: ProductCategoryApiService,
        dataStoreManager: DataStoreManager
    ): ProductCategoryRemoteDataSource = ProductCategoryRemoteDataSourceImpl(api, dataStoreManager)



    @Provides
    @ViewModelScoped
    fun provideOrganisationUserRepositoryRepository(
        api: OrganisationUserApiService,
        dataStoreManager: DataStoreManager
    ): OrganisationUserRemoteDataSource =
        OrganisationUserRemoteDataSourceImpl(api, dataStoreManager)


    @Provides
    @ViewModelScoped
    fun provideOrganisationUserUseCases(
        local: OrganisationUserLocalDataSource,
        remote: OrganisationUserRemoteDataSource
    ): OrganisationUserUseCases =
        OrganisationUserUseCases(
            getOrganisationUsersUseCase = GetOrganisationUsersUseCase(
                remote,
                local
            ),
            deleteOrganisationUser = DeleteOrganisationUser(remote),
            assignRoleOrganisationUser = AssignRoleOrganisationUser(remote),
            updateOrganisationUser = UpdateOrganisationUser(remote),
            chaneStatusOrganisationUserUseCase = ChaneStatusOrganisationUserUseCase(
                remote
            ),
            invitationUserUseCase = InvitationUserUseCase(remote)
        )

    @Provides
    @ViewModelScoped
    fun provideOrganisationUserLocalDataSource(db: AppDatabase): OrganisationUserLocalDataSource =
        OrganisationUserLocalDataSourceImpl(db.organisationUserDao)

    @Provides
    @ViewModelScoped
    fun providerUserRepository(api: UserApiService): UserRemoteDataSource =
        UserRemoteDataSourceImpl(api)

    @Provides
    @ViewModelScoped
    fun provideUserLocalDataSource(db: AppDatabase): UserLocalDataSource =
        UserLocalDataSourceImpl(db.userDao)

    @Provides
    @ViewModelScoped
    fun provideUserUseCases(
        local: UserLocalDataSource,
        remote: UserRemoteDataSource
    ): UserUseCases =
        UserUseCases(
            createUserUseCase = CreateUserUseCase(local),
            getUserUseCase = GetUserUseCase(remote)
        )


    @Provides
    @ViewModelScoped
    fun providerProfileRepository(api: ProfileApiService): ProfileRemoteDataSource =
        ProfileRemoteDataSourceImpl(api)

    @Provides
    @ViewModelScoped
    fun provideProfileUseCases(
        userLocalDataSource: UserLocalDataSource,
        userRemoteDataSource: UserRemoteDataSource,
        organisationUserLocalDataSource: OrganisationUserLocalDataSource,
        organisationUserRemoteDataSource: OrganisationUserRemoteDataSource,
        profileRemoteDataSource: ProfileRemoteDataSource,
        organisationRemoteDataSource: OrganisationRemoteDataSource,
        dataStoreManager: DataStoreManager
    ): ProfileUseCases =
        ProfileUseCases(
            getProfile = GetProfileUseCase(
                userRemoteDataSource,
                userLocalDataSource,
                dataStoreManager
            ),
            getOrganisationsInviting = GetOrganisationsInviting(
                profileRemoteDataSource,
            ),
            acceptOrganisationInvitation = AcceptOrganisationInvitation(profileRemoteDataSource),
            declineOrganisationInvitation = DeclineOrganisationInvitation(profileRemoteDataSource)
        )

}