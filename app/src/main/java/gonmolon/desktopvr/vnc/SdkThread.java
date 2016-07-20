/*
Copyright (C) 2016 RealVNC Limited. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package gonmolon.desktopvr.vnc;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.realvnc.vncsdk.DataStore;
import com.realvnc.vncsdk.Library;
import com.realvnc.vncsdk.Logger;

/**
 * SdkThread is a singleton that will run the VNC SDK on its own thread. All calls to the SDK should
 * be posted on the SDK message queue using the post method.
 */
public class SdkThread {
    private static final SdkThread instance = new SdkThread();
    private static final String TAG = "SdkThread";
    private Thread mThread;
    private boolean initFailed = false;
    private Handler mHandler;

    private SdkThread() {}

    /**
     * Callback interface for displaying messages such as errors from the SDK.
     */
    public interface Callback {
        void displayMessage(final int msgId, String error);
    }

    public void init(final String configFile, final Callback callback) {
        synchronized (this) {
            if (initComplete()) {
                return;
            }

            try {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initSdk(configFile, callback);
                    }
                });
                mThread.start();
                synchronized (mThread) {
                    while (!initComplete() && !initFailed) {
                        mThread.wait();
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Connection error", e);
            }
        }
    }

    private void initSdk(final String configFile, final Callback callback) {
        try {
            // Prepare a looper on current thread.
            Looper.prepare();

            // The Handler will automatically bind to the current thread's Looper.
            mHandler = new Handler();

            // Initialise VNC SDK logging
            Logger.createAndroidLogger();

            try {
                // Create the filestore with the absolute path to Android's app files directory
                DataStore.createFileStore(configFile);
            } catch (Library.VncException e) {
                e.printStackTrace();
            }

            // Init the SDK
            Library.init(Library.EventLoopType.ANDROID);

            synchronized (mThread) {
                mThread.notifyAll();
            }
            // After the following line the thread will start running the message loop and will not
            // normally exit the loop unless a problem happens or you quit() the looper.
            Looper.loop();
        } catch (Library.VncException e) {
            initFailed = true;
            Log.e(TAG, "Initialisation error: ", e);
            e.printStackTrace();
            mHandler = null;
            synchronized (mThread) {
                mThread.notifyAll();
            }
        }
    }

    /**
     * Posts a runnable the SDK thread. {@link #init} must be called first.
     * @param runnable The Runnable to run on the SDK thread.
     */
    public void post(Runnable runnable) {
        if (!initComplete()) {
            throw new RuntimeException("init() must be called first");
        }
        mHandler.post(runnable);
    }

    /**
     * Helper method to determine whether or not we need to init the VNC SDK.
     * @return
     */
    public boolean initComplete() {
        return !initFailed && mHandler != null;
    }

    public static SdkThread getInstance() {
        return instance;
    }
}
