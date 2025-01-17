package com.example.bachelorwork.ui.utils.extensions

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.bachelorwork.ui.navigation.Destination
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified VM : ViewModel> Fragment.hiltViewModelNavigation(destination: Destination): Lazy<VM> {
    val backStackEntry by lazy { findNavController().getBackStackEntry(destination) }
    val storeProducer: () -> ViewModelStore = { backStackEntry.viewModelStore }
    return createViewModelLazy(
        VM::class, storeProducer,
        factoryProducer = { HiltViewModelFactory(requireActivity(), backStackEntry) },
        extrasProducer = { backStackEntry.defaultViewModelCreationExtras }
    )
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(crossinline factory: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) { factory(layoutInflater) }

fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: factory(requireView()).also {
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                    viewLifecycleOwner.lifecycle.addObserver(this)
                    binding = it
                }
            }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }