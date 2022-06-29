package land.kho.meta.presentation.bsLoading

import dagger.hilt.android.lifecycle.HiltViewModel
import land.kho.meta.base.BaseVM
import land.kho.meta.domain.usecases.PreferencesUseCase
import javax.inject.Inject

@HiltViewModel
class BsLoadingVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase
) : BaseVM() {

}