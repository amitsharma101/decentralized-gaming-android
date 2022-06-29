package land.kho.meta.presentation.home.passes

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentPassesBinding
import land.kho.meta.utils.CommonAdapter
import land.kho.meta.utils.ItemClickListener
import land.kho.meta.utils.visibleOrGone

@AndroidEntryPoint
@SuppressLint("NotifyDataSetChanged")
class PassesFragment : BaseFragment<FragmentPassesBinding, PassesVM>(),
    ItemClickListener<PassesAdapterVM> {
    private val passesVM: PassesVM by viewModels()

    private lateinit var adapter: CommonAdapter<PassesAdapterVM>
    private var adapterList: MutableList<PassesAdapterVM> = ArrayList()

    override fun layoutRes() = R.layout.fragment_passes

    override fun viewModelClass() = passesVM

    override fun bindViews() {
        binding.viewModel = viewModel
        adapter = CommonAdapter(adapterList, this)
        binding.rvTransaction.adapter = adapter
        viewModel.getPasses()
        bindObservers()
    }

    override fun onItemClick(value: PassesAdapterVM) {

    }

    private fun bindObservers() {

        viewModel.passesList.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled().let { pass ->
                if (pass == null) {
                    binding.tvEmpty.text = resources.getString(R.string.empty_text, "passes")
                    binding.tvEmpty.visibleOrGone(isVisible = true)
                    binding.rvTransaction.visibleOrGone(isVisible = false)
                } else {
                    binding.rvTransaction.visibleOrGone(isVisible = true)
                    binding.tvEmpty.visibleOrGone(isVisible = false)
                    adapterList.clear()
                    adapterList.add(
                        PassesAdapterVM(
                            model = pass,
                            viewType = R.layout.item_pass
                        )
                    )
                    adapter.notifyDataSetChanged()
                }

            }
        }
    }

}