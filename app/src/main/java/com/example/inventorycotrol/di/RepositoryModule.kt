package com.example.inventorycotrol.di

import android.content.Context
import com.example.inventorycotrol.data.local.AppDatabase
import com.example.inventorycotrol.data.local.repositories.OrderLocalDataSourceImpl
import com.example.inventorycotrol.data.local.repositories.OrganisationLocalDataSourceImpl
import com.example.inventorycotrol.data.local.repositories.OrganisationUserLocalDataSourceImpl
import com.example.inventorycotrol.data.local.repositories.ProductCategoryLocalDataSourceImpl
import com.example.inventorycotrol.data.local.repositories.ProductLocalDataSourceImpl
import com.example.inventorycotrol.data.local.repositories.UserLocalDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.FileRepositoryImpl
import com.example.inventorycotrol.data.remote.repositories.ProductUpdateStockRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.ProfileRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.UserRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.organisation.OrderRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.organisation.OrganisationRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.organisation.OrganisationSettingsRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.organisation.OrganisationUserRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.organisation.ProductCategoryRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.services.FileApiService
import com.example.inventorycotrol.data.remote.services.OrderApiService
import com.example.inventorycotrol.data.remote.services.OrganisationApiService
import com.example.inventorycotrol.data.remote.services.OrganisationSettingsApiService
import com.example.inventorycotrol.data.remote.services.OrganisationUserApiService
import com.example.inventorycotrol.data.remote.services.ProductCategoryApiService
import com.example.inventorycotrol.data.remote.services.ProductUpdateStockApiService
import com.example.inventorycotrol.data.remote.services.ProfileApiService
import com.example.inventorycotrol.data.remote.services.UserApiService
import com.example.inventorycotrol.data.repositories.BarcodeScannerRepositoryImpl
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.repository.local.BarcodeScannerRepository
import com.example.inventorycotrol.domain.repository.local.OrderLocalDataSource
import com.example.inventorycotrol.domain.repository.local.OrganisationLocalDataSource
import com.example.inventorycotrol.domain.repository.local.OrganisationUserLocalDataSource
import com.example.inventorycotrol.domain.repository.local.ProductCategoryLocalDataSource
import com.example.inventorycotrol.domain.repository.local.ProductLocalDataSource
import com.example.inventorycotrol.domain.repository.local.UserLocalDataSource
import com.example.inventorycotrol.domain.repository.remote.FileRepository
import com.example.inventorycotrol.domain.repository.remote.OrderRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationSettingsRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.OrganisationUserRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductCategoryRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductUpdateStockRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.ProfileRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.UserRemoteDataSource
import com.example.inventorycotrol.domain.usecase.file.FileUseCases
import com.example.inventorycotrol.domain.usecase.file.GetFilesUseCase
import com.example.inventorycotrol.domain.usecase.file.UploadFileUseCase
import com.example.inventorycotrol.domain.usecase.order.CreateOrderUseCase
import com.example.inventorycotrol.domain.usecase.order.DeleteOrderUseCase
import com.example.inventorycotrol.domain.usecase.order.GetOrdersUseCase
import com.example.inventorycotrol.domain.usecase.order.OrderUseCases
import com.example.inventorycotrol.domain.usecase.order.UpdateOrderUseCase
import com.example.inventorycotrol.domain.usecase.organisation.CreateOrganisationUseCase
import com.example.inventorycotrol.domain.usecase.organisation.DeleteOrganisationUseCase
import com.example.inventorycotrol.domain.usecase.organisation.GetOrganisationsUseCase
import com.example.inventorycotrol.domain.usecase.organisation.OrganisationUseCases
import com.example.inventorycotrol.domain.usecase.organisation.SwitchOrganisationUseCase
import com.example.inventorycotrol.domain.usecase.organisation.UpdateOrganisationUseCase
import com.example.inventorycotrol.domain.usecase.organisationUser.AssignRoleOrganisationUser
import com.example.inventorycotrol.domain.usecase.organisationUser.ChaneStatusOrganisationUserUseCase
import com.example.inventorycotrol.domain.usecase.organisationUser.DeleteOrganisationUser
import com.example.inventorycotrol.domain.usecase.organisationUser.GetOrganisationUsersUseCase
import com.example.inventorycotrol.domain.usecase.organisationUser.InvitationUserUseCase
import com.example.inventorycotrol.domain.usecase.organisationUser.OrganisationUserUseCases
import com.example.inventorycotrol.domain.usecase.organisationUser.UpdateOrganisationUser
import com.example.inventorycotrol.domain.usecase.organisatonSettings.GetOrganisationSettingsUseCase
import com.example.inventorycotrol.domain.usecase.organisatonSettings.OrganisationSettingsUseCases
import com.example.inventorycotrol.domain.usecase.organisatonSettings.UpdateOrganisationSettingsUseCase
import com.example.inventorycotrol.domain.usecase.product.CreateProductUseCase
import com.example.inventorycotrol.domain.usecase.product.DeleteProductUseCase
import com.example.inventorycotrol.domain.usecase.product.GetProductsUseCase
import com.example.inventorycotrol.domain.usecase.product.ProductUseCases
import com.example.inventorycotrol.domain.usecase.product.UpdateProductUseCase
import com.example.inventorycotrol.domain.usecase.productCategory.CreateProductCategoryUseCase
import com.example.inventorycotrol.domain.usecase.productCategory.DeleteProductCategoryUseCase
import com.example.inventorycotrol.domain.usecase.productCategory.GetProductCategoriesUseCase
import com.example.inventorycotrol.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.inventorycotrol.domain.usecase.productCategory.UpdateProductCategoryUseCase
import com.example.inventorycotrol.domain.usecase.productUpdateStock.GetStockUseCase
import com.example.inventorycotrol.domain.usecase.productUpdateStock.ProductUpdateStockUseCases
import com.example.inventorycotrol.domain.usecase.productUpdateStock.UpdateStockUseCase
import com.example.inventorycotrol.domain.usecase.profile.AcceptOrganisationInvitation
import com.example.inventorycotrol.domain.usecase.profile.ChangeEmailUseCase
import com.example.inventorycotrol.domain.usecase.profile.ChangePasswordUseCase
import com.example.inventorycotrol.domain.usecase.profile.ChangeUserInfoUseCase
import com.example.inventorycotrol.domain.usecase.profile.DeclineOrganisationInvitation
import com.example.inventorycotrol.domain.usecase.profile.GetOrganisationsInviting
import com.example.inventorycotrol.domain.usecase.profile.GetProfileUseCase
import com.example.inventorycotrol.domain.usecase.profile.ProfileUseCases
import com.example.inventorycotrol.domain.usecase.user.CreateUserUseCase
import com.example.inventorycotrol.domain.usecase.user.GetUserUseCase
import com.example.inventorycotrol.domain.usecase.user.UserUseCases
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
        local: OrganisationLocalDataSource,
        organisationRemote: OrganisationRemoteDataSource,

        userLocal: UserLocalDataSource,
        userRemote: UserRemoteDataSource,
        organisationUserLocal: OrganisationUserLocalDataSource,
        organisationUserRemote: OrganisationUserRemoteDataSource,
        da: DataStoreManager
    ): OrganisationUseCases =
        OrganisationUseCases(
            create = CreateOrganisationUseCase(
                organisationUserLocal,
                organisationUserRemote,
                organisationRemote,
                local
            ),
            delete = DeleteOrganisationUseCase(organisationRemote, local),
            get = GetOrganisationsUseCase(organisationRemote, local),
            update = UpdateOrganisationUseCase(organisationRemote, local),
            switch = SwitchOrganisationUseCase(
                userRemote,
                userLocal,
                organisationUserRemote,
                organisationUserLocal,
                organisationRemote,
                da
            )
        )

    @Provides
    @ViewModelScoped
    fun provideOrganisationSettingsRemoteSource(
        api: OrganisationSettingsApiService,
        dataStoreManager: DataStoreManager
    ): OrganisationSettingsRemoteDataSource =
        OrganisationSettingsRemoteDataSourceImpl(api, dataStoreManager)

    @Provides
    @ViewModelScoped
    fun provideOrganisationSettingsUseCases(remote: OrganisationSettingsRemoteDataSource) =
        OrganisationSettingsUseCases(
            get = GetOrganisationSettingsUseCase(remote),
            update = UpdateOrganisationSettingsUseCase(remote)
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
            getOrganisationUsersUseCase = GetOrganisationUsersUseCase(remote, local),
            deleteOrganisationUser = DeleteOrganisationUser(remote, local),
            assignRoleOrganisationUser = AssignRoleOrganisationUser(remote, local),
            updateOrganisationUser = UpdateOrganisationUser(remote, local),
            chaneStatusOrganisationUserUseCase = ChaneStatusOrganisationUserUseCase(remote),
            invitationUserUseCase = InvitationUserUseCase(remote, local)
        )

    @Provides
    @ViewModelScoped
    fun provideOrganisationUserLocalDataSource(db: AppDatabase): OrganisationUserLocalDataSource =
        OrganisationUserLocalDataSourceImpl(db.organisationUserDao)

    @Provides
    @ViewModelScoped
    fun providerUserRepository(
        api: UserApiService,
        dataStoreManager: DataStoreManager
    ): UserRemoteDataSource =
        UserRemoteDataSourceImpl(api, dataStoreManager)

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
            getUserUseCase = GetUserUseCase(remote, local)
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
        organisationUserLocal: OrganisationUserLocalDataSource,
        organisationUserRemote: OrganisationUserRemoteDataSource,
        profileRemoteDataSource: ProfileRemoteDataSource,
        organisationRemoteDataSource: OrganisationRemoteDataSource,
        dataStoreManager: DataStoreManager
    ): ProfileUseCases =
        ProfileUseCases(
            getProfile = GetProfileUseCase(
                userRemoteDataSource,
                userLocalDataSource,
            ),
            changePassword = ChangePasswordUseCase(profileRemoteDataSource),
            changeEmail = ChangeEmailUseCase(profileRemoteDataSource, userLocalDataSource),
            getOrganisationsInviting = GetOrganisationsInviting(
                profileRemoteDataSource,
            ),
            changeUserInfo = ChangeUserInfoUseCase(
                profileRemoteDataSource,
                userLocalDataSource
            ),
            acceptOrganisationInvitation = AcceptOrganisationInvitation(profileRemoteDataSource),
            declineOrganisationInvitation = DeclineOrganisationInvitation(profileRemoteDataSource)
        )

}