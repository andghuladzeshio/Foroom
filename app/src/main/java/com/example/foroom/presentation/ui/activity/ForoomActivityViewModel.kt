package com.example.foroom.presentation.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.foroom.domain.model.User
import com.example.foroom.presentation.ui.delegate.saveuser.GetAndSaveUserDelegate
import com.example.foroom.presentation.ui.util.datastore.user.ForoomUserDataStore
import com.example.foroom.presentation.ui.util.exception.ForoomUnauthorizedUserException
import com.example.shared.util.runtime.user_token.UserTokenRuntimeHolder
import com.example.shared.model.ForoomLanguage
import com.example.shared.model.Result
import com.example.shared.ui.viewModel.BaseViewModel
import com.example.shared.util.runtime.user_language.UserLanguageRuntimeHolder
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.get

class ForoomActivityViewModel(
    private val userDataStore: ForoomUserDataStore,
    private val userTokenRuntimeHolder: UserTokenRuntimeHolder,
    getAndSaveUserDelegate: GetAndSaveUserDelegate
): BaseViewModel(), GetAndSaveUserDelegate by getAndSaveUserDelegate{
    private val _currentUserLiveData = MediatorLiveData<Result<User>>()
    val currentUserLiveData: LiveData<Result<User>> get() = _currentUserLiveData

    private val userLanguageRuntimeHolder = get<UserLanguageRuntimeHolder>()

    init {
        getAndSaveUserDelegate.init(viewModelScope)

        updateRuntimeLanguageHolder()
        getCurrentUser()

        _currentUserLiveData.addSource(getAndSaveUserResultLiveData) { result ->
            _currentUserLiveData.value = result
        }
    }

    private fun updateRuntimeLanguageHolder() {
        viewModelScope.launch {
            val language = userDataStore.getUserLanguage()?.let { langName ->
                ForoomLanguage.fromName(langName)
            } ?: ForoomLanguage.KA

            userLanguageRuntimeHolder.setUserLanguage(language)
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            userDataStore.getUserAuthToken().catch {
                _currentUserLiveData.postValue(Result.Error(ForoomUnauthorizedUserException()))
            }.collect { token ->
                userTokenRuntimeHolder.setUserToken(token)
                getAndSaveUserData()
                cancel()
            }
        }
    }

    suspend fun updateUserLanguage(language: ForoomLanguage) {
        userDataStore.saveUserLanguage(language.langName)
        userLanguageRuntimeHolder.setUserLanguage(language)
    }

    suspend fun getUserLanguage() = userDataStore.getUserLanguage()?.let { language ->
        ForoomLanguage.fromName(language)
    }
}