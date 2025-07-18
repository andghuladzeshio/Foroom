package com.example.shared.util.pagination

class PaginationHelperImpl<T>: PaginationHelper<T> {
    override var pageSize: Int = DEFAULT_PAGE_SIZE
    private val items = mutableListOf<T>()
    private var page = FIRST_PAGE

    override fun getItems(): List<T> = items

    override fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
    }

    override fun getPage(): Int = page

    override fun addPage(items: List<T>) {
        this.items.addAll(items)
        page++
    }

    override fun clear() {
        items.clear()
        page = FIRST_PAGE
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
        private const val FIRST_PAGE = 0
    }
}