package com.finalexam.musicboxx.utils

// Class này dùng để báo trạng thái dữ liệu (Thành công, Lỗi, Đang tải)
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}