package com.example.bachelorwork.ui.model.organisationRole.manage

sealed class OrganisationRoleManageUiEvent {
    data class RoleNameChanged(val roleName: String) : OrganisationRoleManageUiEvent()
    data class DescriptionChanged(val description: String) : OrganisationRoleManageUiEvent()

    /*data class ProductPermissionChanged(val productPermission: Set<AppRolePermissions.Product>) : OrganisationRoleManageUiEvent()
    data class OrderPermissionChanged(val orderPermission: Set<AppRolePermissions.Order>) : OrganisationRoleManageUiEvent()
    data class UserPermissionChanged(val userPermission: Set<AppRolePermissions.User>) : OrganisationRoleManageUiEvent()*/

    data object ManageOrganisationRole : OrganisationRoleManageUiEvent()
}