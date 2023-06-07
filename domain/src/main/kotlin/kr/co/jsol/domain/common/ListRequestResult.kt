package kr.co.jsol.domain.common

data class ListRequestResult<T>(
    val success: MutableList<T> = mutableListOf(),
    val fail: MutableList<T> = mutableListOf(),
) {
    fun addSuccess(id: T) = success.add(id)
    fun addFail(id: T) = fail.add(id)
}
