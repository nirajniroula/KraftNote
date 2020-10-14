package com.example.kraftnote.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DBHelper {
    private final Executor executor = Executors.newSingleThreadExecutor();

    public void get(DBQueryRunner runner, DBListener listener) {
        executor.execute(() -> {
            Object data = runner.run();

            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onSuccess(data);
            });
        });
    }

    interface DBQueryRunner {
        Object run();
    }

    interface DBListener {
        void onSuccess(Object data);
    }
}
