package com.steve.utilities.core.extensions

import io.reactivex.CompletableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okio.Okio
import okio.buffer
import okio.sink
import java.io.File

fun Disposable.addToCompositeDisposable(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> observableTransformer(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun completableTransformer(): CompletableTransformer {
    return CompletableTransformer { observable ->
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Observable<ResponseBody>.saveFile(dirPath: String, fileName: String): Observable<File> {
    return this
        .flatMap { response: ResponseBody ->
            val dir = File(dirPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, fileName)
            val sink = file.sink().buffer()
            sink.writeAll(response.source())
            sink.close()
            return@flatMap Observable.just(file)
        }
}

fun <T> ObservableEmitter<T>.checkDisposed(): ObservableEmitter<T>? {
    return if (this.isDisposed) {
        null
    } else {
        this
    }
}