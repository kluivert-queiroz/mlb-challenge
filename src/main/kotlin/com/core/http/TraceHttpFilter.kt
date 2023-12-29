package com.core.http

import io.micronaut.core.async.publisher.Publishers
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.opentelemetry.api.trace.Span
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.reactivestreams.Publisher

@Filter("/*/**")
class TraceHttpFilter : HttpServerFilter {
    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> {
        return Flowable.fromPublisher(chain.proceed(request)).subscribeOn(Schedulers.io()).doOnNext { res ->
            res.headers.add("x-trace-id", Span.current().spanContext.traceId)
            res.headers.add("x-span-id", Span.current().spanContext.spanId)
        }
    }
}