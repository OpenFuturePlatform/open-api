package io.openfuture.api.domain

import org.springframework.data.domain.AbstractPageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC
import javax.validation.constraints.Max
import javax.validation.constraints.Min

open class PageRequest(
        @field:Min(value = 0) private var offset: Long = 0,
        @field:Min(value = 1) @field:Max(100) private var limit: Int = 100
) : AbstractPageRequest(offset.toInt() / limit + 1, limit) {

    override fun next(): Pageable = PageRequest(offset + limit, limit)

    override fun getOffset(): Long = offset

    override fun getSort(): Sort = Sort.by(ASC, "id")

    override fun first(): Pageable = PageRequest(0, limit)
    override fun withPage(pageNumber: Int): Pageable = PageRequest(pageNumber.toLong(), limit)

    override fun previous(): PageRequest {
        return if (offset == 0L) this else {
            var newOffset = this.offset - limit
            if (newOffset < 0) newOffset = 0
            PageRequest(newOffset, limit)
        }
    }

}