package com.example.bachelorwork.ui.permission

class GalleryPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String =
        if (isPermanentlyDeclined) {
            "It seems you have permanently denied access to the gallery. To enable access, go to the app settings and allow the gallery permission."
        } else {
            "This app needs access to your gallery to let you upload and view images. Please grant the permission."
        }
}