package land.kho.meta.presentation.splash

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentSplashBinding
import land.kho.meta.utils.Constants
import land.kho.meta.utils.Utility


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding, SplashVM>() {

    private val splashVM: SplashVM by viewModels()

    override fun layoutRes() = R.layout.fragment_splash

    override fun viewModelClass() = splashVM

    override fun bindViews() {
        binding.viewModel = viewModel
        binding.tvVersion.text = Utility.getVersionCode()
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.userStatus.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let {
                viewModel.isLoading.value = false
                when (it) {
                    Constants.HOME ->
                        findNavController().navigate(
                            SplashFragmentDirections.actionSplashFragmentToMyGameFragment()
                        )
                    Constants.CREATE_WALLET ->
                        findNavController().navigate(
                            SplashFragmentDirections.actionSplashFragmentToCreateWalletFragment(
                                true
                            )
                        )
                    Constants.MNEMONIC -> {
                        findNavController().navigate(
                            SplashFragmentDirections.actionSplashFragmentToCreateWalletFragment(
                                false
                            )
                        )
                    }
                    Constants.LOGIN -> {
                        findNavController().navigate(
                            SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                        )
                    }

                }
            }
        }
    }
}