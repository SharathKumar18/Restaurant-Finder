package com.codecraft.restaurant.rxbus

import io.reactivex.subjects.PublishSubject

class RxHelper : MainBus {

    private var mBus: PublishSubject<Any>? = null

    init {
        mBus = PublishSubject.create()
    }

    override fun send(event: Any) {
        mBus?.onNext(event)
    }

    override fun toObservable(): PublishSubject<Any>? {
        return mBus
    }

    override fun hasObservers(): Boolean? {
        return mBus?.hasObservers()
    }

    /*companion object {
        private var mBus: PublishSubject<Any>? = null
        private var mBusClass: RxHelper? = null
        fun getInstance(): RxHelper? {
            if (mBusClass == null) {
                mBusClass = RxHelper()
                mBus = PublishSubject.create()
            }
            return mBusClass
        }
    }*/
}
