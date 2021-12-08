package top.ntutn.zerohelper

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestViewPagerActivityViewModel : ViewModel() {
    private val _currentPageNumber = MutableLiveData(0)
    val currentPageNumber: LiveData<Int>
        get() = _currentPageNumber
    private val _items = MutableLiveData<MutableList<String>>(mutableListOf())
    val items: LiveData<MutableList<String>>
        get() = _items

    @UiThread
    fun changeItemPosition(position: Int) {
        if (currentPageNumber.value != position) {
            _currentPageNumber.value = position
        }
    }

    fun previousPage() {
        if (_currentPageNumber.value!! > 0) {
            _currentPageNumber.value = _currentPageNumber.value!! - 1
        }
    }

    fun nextPage() {
        _currentPageNumber.value = _currentPageNumber.value!! + 1
    }

    fun loadMore() {
        viewModelScope.launch {
            delay((0L..5000L).random())
            repeat((1..10).random()) {
                _items.value?.add((0L..Long.MAX_VALUE).random().toString())
                _items.value = _items.value
            }
        }
    }
}