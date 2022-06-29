package land.kho.meta.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import land.kho.meta.presentation.main.MainActivity
import land.kho.meta.presentation.main.NavBarData

abstract class BaseFragment<T : ViewDataBinding, X : BaseVM> : Fragment() {

    lateinit var binding: T
    lateinit var viewModel: X

    @LayoutRes
    abstract fun layoutRes(): Int

    abstract fun viewModelClass(): X

    abstract fun bindViews()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes(), container, false)
        viewModel = viewModelClass()
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    fun showLoading() {
        getMainActivity()?.showLoading()
    }

    fun dismissLoading() {
        getMainActivity()?.dismissLoading()
    }

    fun getNavBarData(
        navBarData: (NavBarData) -> Unit
    ) {
        getMainActivity()?.getNavBarData {
            navBarData.invoke(it)
        }
    }

    fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity) {
            activity as MainActivity?
        } else {
            null
        }
    }

}