package com.codecraft.restaurant.rxbus

import io.reactivex.subjects.PublishSubject

class RxHelper : MainBus {

    private var busInstance: PublishSubject<Any>? = null
    init {
        busInstance = PublishSubject.create()
    }

    override fun send(event: Any) {
        busInstance?.onNext(event)
    }

    override fun toObservable(): PublishSubject<Any>? {
        return busInstance
    }

    override fun hasObservers(): Boolean? {
        return busInstance?.hasObservers()
    }
}
