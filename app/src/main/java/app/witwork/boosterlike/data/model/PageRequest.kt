package app.witwork.boosterlike.data.model

data class PageRequest(val page: Int = 1, val size: Int = 10) {
    companion object {
        val default: PageRequest by lazy {
            return@lazy PageRequest(1, 10)
        }
    }
}