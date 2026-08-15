package com.aowen.predcompanion.feature.collections.impl.eternals

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.eternals.EternalRepository
import com.aowen.predcompanion.feature.collections.impl.R
import com.aowen.predcompanion.feature.collections.impl.ui.data.toDetailsUi
import com.aowen.predcompanion.feature.collections.impl.ui.model.EternalDetailsUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface EternalDetailsUiState {
    data object Loading: EternalDetailsUiState
    data class Loaded(val content: EternalDetailsUiModel) : EternalDetailsUiState
    data class Error(@param:StringRes val messageResId: Int) : EternalDetailsUiState
}

@HiltViewModel(assistedFactory = EternalDetailsViewModel.Factory::class)
class EternalDetailsViewModel @AssistedInject constructor(
    @Assisted private val eternalId: String,
    private val eternalRepository: EternalRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(eternalId: String): EternalDetailsViewModel
    }

    private var _uiState = MutableStateFlow<EternalDetailsUiState>(EternalDetailsUiState.Loading)
    val uiState: StateFlow<EternalDetailsUiState> = _uiState

    init {
        initViewModel()
    }

    fun reloadDetails() {
        initViewModel()
    }

    private fun initViewModel() {
        viewModelScope.launch {
            val eternalDetails =  eternalRepository.getEternalById(eternalId)
            if(eternalDetails != null) {
                _uiState.value = EternalDetailsUiState.Loaded(
                    content = eternalDetails.toDetailsUi()
                )
            } else {
                _uiState.value = EternalDetailsUiState.Error(
                    messageResId = R.string.feature_collections_eternal_details_error
                )
            }
        }
    }


}