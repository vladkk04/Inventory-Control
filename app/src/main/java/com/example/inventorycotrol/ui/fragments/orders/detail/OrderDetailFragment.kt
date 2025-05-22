package com.example.inventorycotrol.ui.fragments.orders.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentOrderDetailBinding
import com.example.inventorycotrol.databinding.OrderAttachmentItemBinding
import com.example.inventorycotrol.databinding.OrderDetailProductItemBinding
import com.example.inventorycotrol.domain.model.order.Attachment
import com.example.inventorycotrol.domain.model.order.OrderProductSubItem
import com.example.inventorycotrol.data.download.AppDownloader
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.model.order.DiscountType
import com.example.inventorycotrol.domain.model.file.FileMimeType
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class OrderDetailFragment : Fragment(R.layout.fragment_order_detail) {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderDetailViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var downloader: AppDownloader

    private val adapter = simpleAdapter<OrderProductSubItem, OrderDetailProductItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.textViewName.text = item.name
            this.textViewQuantity.text = String.format(Locale.getDefault(), "%.2f\u00A0%s", item.price, item.unit)
            this.textViewPrice.text = String.format(Locale.getDefault(), "%.2f\u00A0%s", item.quantity, viewModel.uiState.value.currency)
        }
        listeners {
            root.setOnClickListener {
                //viewModel.navigateToProductDetail(item.productId)
            }
        }
    }

    private val attachmentsAdapter = simpleAdapter<Attachment, OrderAttachmentItemBinding> {
        /*areItemsSame = { oldItem, newItem -> oldItem == newItem.displayName }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }*/

        bind { item ->
            val attachmentFile = item.url.split('/').last().split('.')
            val attachmentName = attachmentFile.component1()
            val attachmentType = attachmentFile.component2()

            this.textViewAttachmentName.text = String.format(Locale.getDefault(), "%s.%s", attachmentName, attachmentType)
            this.textViewAttachmentSize.text = item.size

            val icon = try {
                when (FileMimeType.valueOf(attachmentType.uppercase())) {
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
                        R.drawable.ic_doc_file
                    }

                    FileMimeType.DOCX -> {
                        R.drawable.ic_doc_file
                    }

                    FileMimeType.TXT -> {
                        R.drawable.ic_txt_file
                    }

                    FileMimeType.XLS -> {
                        R.drawable.ic_excel_file
                    }
                    FileMimeType.XLSX -> {
                        R.drawable.ic_excel_file
                    }
                }
            } catch (e: Exception) {
                R.drawable.ic_outline_file
            }

            this.imageViewAttachment.setImageResource(icon)
            this.checkBoxDownload.isVisible = true
        }
        listeners {
            this.checkBoxDownload.onClick { item ->
                downloader.downloadFile(
                    "${AppConstants.BASE_URL_CLOUD_FRONT}${item.url}",
                    item.url.split('/').last().split('.').component1()
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        setupAttachmentRecyclerView()

        downloader = AppDownloader(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isConnected.collectLatest {
                binding.toolbar.menu.findItem(R.id.delete).apply {
                    isEnabled = it
                    icon?.mutate()?.alpha = if (it) 255 else 100
                }
            }
        }

        collectInLifecycle(viewModel.uiState) {
            binding.toolbar.title = "Order #${it.order?.id}"

            binding.progressBar.isVisible = it.isLoading

            binding.materialCardViewFiles.isGone = it.order?.attachments?.isEmpty() == true
            binding.textViewAttachments.isGone = it.order?.attachments?.isEmpty() == true

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
                String.format(Locale.getDefault(), "%.2f\u00A0%s", it.order?.total, it.currency)


            when (it.order?.discount?.type) {
                DiscountType.PERCENTAGE -> binding.textViewDiscountPrice.text =
                    it.order.discount.value.toString() + " %"

                DiscountType.FIXED -> binding.textViewDiscountPrice.text =
                    it.order.discount.value.toString() + "\u00A0" + it.currency + "\u00A0(fixed)"

                null -> {}
            }

            binding.textViewCommentsValue.text = it.order?.comment

            binding.dividerComments.isGone = it.order?.comment.isNullOrEmpty()
            binding.textViewComments.isGone = it.order?.comment.isNullOrEmpty()
            binding.textViewCommentsValue.isGone = it.order?.comment.isNullOrEmpty()

            binding.textViewSubtotalPrice.text =
                String.format(Locale.getDefault(), "%.2f\u00A0%s", it.order?.subTotal, it.currency)

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

        return binding.root
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