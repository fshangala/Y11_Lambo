package com.fshangala.y11

import androidx.lifecycle.MutableLiveData

class SlaveViewModel : Y11ViewModel() {
    var jsResponse = MutableLiveData<String>("")
    var slaveStatus = MutableLiveData<String>("")
}