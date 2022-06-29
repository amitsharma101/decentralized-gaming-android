package land.kho.meta.presentation.bsInfo

import dagger.hilt.android.lifecycle.HiltViewModel
import land.kho.meta.base.BaseVM
import land.kho.meta.domain.usecases.PreferencesUseCase
import javax.inject.Inject

@HiltViewModel
class BsInfoVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase
) : BaseVM()