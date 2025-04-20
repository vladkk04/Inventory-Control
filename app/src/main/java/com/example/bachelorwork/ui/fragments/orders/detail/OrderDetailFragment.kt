package com.example.bachelorwork.ui.fragments.orders.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.databinding.FragmentOrderDetailBinding
import com.example.bachelorwork.databinding.OrderAttachmentItemBinding
import com.example.bachelorwork.databinding.OrderDetailProductItemBinding
import com.example.bachelorwork.domain.model.order.Attachment
import com.example.bachelorwork.domain.model.order.OrderProductSubItem
import com.example.bachelorwork.downloader.AppDownloader
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.model.order.DiscountType
import com.example.bachelorwork.ui.utils.FileMimeType
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class OrderDetailFragment : Fragment(R.layout.fragment_order_detail) {
    private val binding by viewBinding(FragmentOrderDetailBinding::bind)

    private val viewModel: OrderDetailViewModel by viewModels()

    private lateinit var downloader: AppDownloader

    private val adapter = simpleAdapter<OrderProductSubItem, OrderDetailProductItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.textViewName.text = item.name
            this.textViewQuantity.text = String.format(Locale.getDefault(), "%.2f", item.quantity)
            this.textViewPrice.text = String.format(Locale.getDefault(), "%.2f", item.price)

            Glide.with(requireContext())
                .load("http://192.168.68.60:8080/${item.image}")
                .placeholder(R.drawable.ic_image)
                .fallback(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .centerCrop()
                .into(imageView)

        }
        listeners {
            root.setOnClickListener {
                //viewModel.navigateToProductDetail(item.productId)
            }
        }
    }

    private val attachmentsAdapter = simpleAdapter<Attachment, OrderAttachmentItemBinding> {
        /*areItemsSame = { oldItem, newItem -> oldItem.displayName == newItem.displayName }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }*/

        bind { item ->
            val attachmentFile = item.url.split('/').last().split('.')
            val attachmentName = attachmentFile.component1()
            val attachmentType = attachmentFile.component2()

            this.textViewAttachmentName.text = attachmentName
            this.textViewAttachmentSize.text = item.size

            val icon = when (FileMimeType.valueOf(attachmentType.uppercase())) {
                FileMimeType.PDF -> {
                    R.drawable.ic_file_pdf
                }

                FileMimeType.PNG -> {
                    R.drawable.ic_file_image
                }

                FileMimeType.JPG -> {
                    R.drawable.ic_file_image
                }

                FileMimeType.JPEG -> {
                    R.drawable.ic_file_image
                }

                FileMimeType.DOC -> {
                    R.drawable.ic_file_pdf
                }

                FileMimeType.DOCX -> {
                    R.drawable.ic_file_pdf
                }

                FileMimeType.TXT -> {
                    R.drawable.ic_file_pdf
                }
            }

            this.imageViewAttachment.setImageResource(icon)
            this.checkBoxDownload.isVisible = true
        }
        listeners {
            this.checkBoxDownload.onClick { item ->
                downloader.downloadFile(
                    "${AppConstants.BASE_URL}${item.url}",
                    item.url.split('/').last().split('.').component1()
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithMargin(binding.root)

        setupAttachmentRecyclerView()

        downloader = AppDownloader(requireContext())

        collectInLifecycle(viewModel.uiState) {
            binding.toolbar.title = "Order #${it.order?.id}"

            val formattedDate = it.order?.orderedAt?.let { it1 ->
                Instant.ofEpochMilli(it1)
                    .atZone(ZoneId.systemDefault())
                    .format(
                        DateTimeFormatter.ofPattern(AppConstants.ORDER_DATE_FORMAT)
                            .withLocale(Locale.getDefault())
                    )
            }

            binding.toolbar.subtitle = binding.root.context.getString(
                R.string.text_order_date_timestamp,
                formattedDate
            )

            binding.textViewTotalPrice.text =
                String.format(Locale.getDefault(), "%.2f", it.order?.total)


            when (it.order?.discount?.type) {
                DiscountType.PERCENTAGE -> binding.textViewDiscountPrice.text =
                    it.order.discount.value.toString()

                DiscountType.FIXED -> binding.textViewDiscountPrice.text =
                    it.order.discount.value.toString()

                null -> {}
            }

            binding.textViewSubtotalPrice.text =
                String.format(Locale.getDefault(), "%.2f", it.order?.subTotal)

            adapter.submitList(it.order?.products?.map { product ->
                OrderProductSubItem(
                    id = product.id,
                    image = product.imageUrl,
                    name = product.name,
                    unit = product.unit.toString(),
                    price = product.price,
                    quantity = product.quantity,
                )
            })

            attachmentsAdapter.submitList(it.order?.attachments)
        }

        setupToolbarMenu()
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@OrderDetailFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupAttachmentRecyclerView() {
        with(binding.recyclerViewAttachments) {
            adapter = this@OrderDetailFragment.attachmentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupToolbarMenu() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    AppDialogs.createDeleteDialog(
                        requireContext(),
                        deleteItemTitle = "Order #${viewModel.uiState.value.order?.id}",
                    ) { viewModel.deleteOrder() }.show()
                    true
                }

                R.id.edit -> {
                    viewModel.navigateToOrderEdit()
                    true
                }

                else -> false
            }
        }
    }
}