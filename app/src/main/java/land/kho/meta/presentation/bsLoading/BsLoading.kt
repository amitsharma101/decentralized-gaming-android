package land.kho.meta.presentation.bsLoading

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseBS
import land.kho.meta.databinding.FragmentBsLoadingBinding
import land.kho.meta.utils.visibleOrGone

@AndroidEntryPoint
class BsLoading : BaseBS<FragmentBsLoadingBinding, BsLoadingVM>() {

    private val bsLoadingVM: BsLoadingVM by viewModels()
    private lateinit var callback: (retry: Boolean) -> Unit
    private lateinit var message: String
    private var loading = true
    private var retry = false
    private var close = false

    companion object {
        fun getNewInstance(
            message: String,
            loading: Boolean,
            retry: Boolean,
            callback: (retry: Boolean) -> Unit
        ): BsLoading {
            return BsLoading().apply {
                this.message = message
                this.loading = loading
                this.retry = retry
                this.callback = callback
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_bs_loading

    override fun viewModelClass() = bsLoadingVM

    override fun bindViews() {
        binding.viewModel = viewModel
        binding.progressBar.visibleOrGone(isVisible = loading)
        binding.btnRetry.visibleOrGone(isVisible = retry || close)
        if(close){
            binding.btnRetry.text = "Close";
        }
        if(retry){
            binding.btnRetry.text = "Retry";
        }
        binding.tvMsg.text = message
        setUpClickListener()
    }

    private fun setUpClickListener() {

        binding.btnRetry.setOnClickListener {
            if(close){
                dismissLoading()
            }else if(retry){
                showRetry(retry = false)
                loading(loading = true)
                message(msg = "Retrying")
                callback.invoke(true)
            }
        }
    }

    fun showRetry(retry: Boolean) {
        this.close=retry
        binding.btnRetry.visibleOrGone(isVisible = retry)
        binding.btnRetry.text = "Retry"
        setUpClickListener()
    }

    fun showClose(close: Boolean) {
        this.close=close
        binding.btnRetry.visibleOrGone(isVisible = close)
        binding.btnRetry.text = "Close"
        setUpClickListener()
    }

    fun loading(loading: Boolean) {
        binding.progressBar.visibleOrGone(isVisible = loading)
    }

    fun message(msg: String) {
        binding.tvMsg.text = msg
    }

    fun dismissLoading() {
        dismiss()
    }

}

