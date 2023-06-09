package com.nakkeez.frametempmonitor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nakkeez.frametempmonitor.data.FrameTempRepository

/**
 * ViewModel used to store and observe the frame rate and battery temperature
 * values from UI.
 */
class FrameTempViewModel(private val repository: FrameTempRepository) : ViewModel() {

    val frameRate: LiveData<Float>
        get() = repository.frameRate

    val batteryTemp: LiveData<Float>
        get() = repository.batteryTemp

    val cpuTemp: LiveData<Float>
        get() = repository.cpuTemp

    fun updateFrameRate(fps: Float) {
        repository.updateFrameRate(fps)
    }

    fun updateBatteryTemp(tempBattery: Float) {
        repository.updateBatteryTemp(tempBattery)
    }

    fun updateCpuTemp(tempCpu: Float) {
        repository.updateCpuTemp(tempCpu)
    }

    class FrameTempViewModelFactory(private val repository: FrameTempRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FrameTempViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FrameTempViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

