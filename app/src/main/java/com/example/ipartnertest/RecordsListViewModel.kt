package com.example.ipartnertest

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.example.ipartnertest.data.Entry
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference

class RecordsListViewModel(private val refContext: WeakReference<Context>) : ViewModel() {

    var session: String = ""

    private val _sessionObtained = MutableLiveData<Boolean>()
    val sessionObtained: LiveData<Boolean>
        get() = _sessionObtained

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _entries = MutableLiveData<List<Entry>>()
    val entries: LiveData<List<Entry>>
        get() = _entries

    init {
        _sessionObtained.value = false
    }

    // Retrieve new session id at first launch.
    fun newSession() {
        if (isInternetAvailable()) {
            try {
                viewModelScope.launch {
                    val response = TestServerService.retrofitService.newSession()
                    if (response.status == 1) {
                        session = response.data?.session ?: ""
                        Log.d("___", "session : $session")
                        _sessionObtained.value = true
                        _error.value = ""
                        getEntries()
                    } else {
                        _error.value = response.error ?: "Unknown error"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message.toString()
                Log.d("___", "error: ${_error.value}")
            }
        } else {
            AlertDialog.Builder(refContext.get()!!)
                .setTitle("No Internet")
                .setMessage("Check your internet connection and try again")
                .setPositiveButton(
                    "Refresh"
                ) { _, _ -> newSession() }
                .show()
            Log.d("___", "Check your internet connection")
        }
    }

    // Retrieve the list of entries
    fun getEntries() {
        if (isInternetAvailable()) {
            try {
                viewModelScope.launch {
                    _isLoading.value = true
                    val response =
                        TestServerService.retrofitService.getEntries(session = session)
                    _entries.value = response.data?.get(0) as List<Entry>?
                    Log.d("___", "entries: ${_entries.value}")
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message.toString()
                Log.d("___", "error: ${_error.value}")
                _isLoading.value = false
            }
        } else {
            AlertDialog.Builder(refContext.get()!!)
                .setTitle("No Internet")
                .setMessage("Check your internet connection and try again")
                .setPositiveButton(
                    "Refresh"
                ) { _, _ -> getEntries() }
                .show()
            Log.d("___", "Check your internet connection")
        }
    }

    // Add new entry
    fun addEntry(newEntryData: String) {
        if (isInternetAvailable()) {
            try {
                viewModelScope.launch {
                    val response = TestServerService.retrofitService.addEntry(
                        session = session,
                        body = newEntryData
                    )
                    if (response.status == 1) {
                        Log.d("___", "entry added, id : ${response.data?.id}")
                        getEntries()
                    } else {
                        _error.value = response.error ?: "Unknown error"
                        Log.d("___", "error: ${_error.value}")
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message.toString()
                Log.d("___", "error: ${_error.value}")
            }
        } else {
            AlertDialog.Builder(refContext.get()!!)
                .setTitle("No Internet")
                .setMessage("Check your internet connection and try again")
                .setPositiveButton(
                    "Try again"
                ) { _, _ -> addEntry(newEntryData) }
                .show()
            Log.d("___", "Check your internet connection")
        }
    }

    // Check whether internet connection is present
    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            refContext.get()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }


}

class RecordsListViewModelFactory(private val refContext: WeakReference<Context>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordsListViewModel::class.java)) {
            return RecordsListViewModel(refContext) as T
        }
        throw IllegalArgumentException("Wrong model class")
    }

}