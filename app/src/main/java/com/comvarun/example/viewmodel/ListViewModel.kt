package com.comvarun.example.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.comvarun.example.model.User
import com.comvarun.example.network.UserService
import kotlinx.coroutines.*

class ListViewModel : ViewModel() {

    val userService = UserService().getUsersService()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val users = MutableLiveData<List<User>>()
    val usersLoadError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchUsers()
    }
    // Api call
    private fun fetchUsers() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = userService.getUsers()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    users.value = response.body()?.data
                    usersLoadError.value = null
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
        usersLoadError.value = ""
        loading.value = false
    }

    private fun onError(message: String) {
        usersLoadError.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}