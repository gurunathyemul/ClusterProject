package com.example.clusterproject

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.data.model.Client
import com.example.data.model.RecentClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//service for sending process id and package name
class AIDLService : Service() {
    /*
        RemoteCallbackList, which is a class provided by Android's AIDL framework for managing a
         list of callbacks in a thread-safe manner.
    */
    private val connectionCountCallbacks = RemoteCallbackList<IBikeSpeedCallback>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        updateSpeedCount()
        return super.onStartCommand(intent, flags, startId)
    }

    // Pass the binder object to clients so they can communicate with this service
    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind: service")
        Toast.makeText(this, "onBind: service", Toast.LENGTH_SHORT).show()
        connectionCount++
        // Choose which binder we need to return based on the type of IPC the client makes
        return when (intent?.action) {
            "aidlexample" -> aidlBinder
            else -> null
        }
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind: ")
        Toast.makeText(this, "onRebind", Toast.LENGTH_SHORT).show()
    }

    // A client has unbound from the service
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: service")
        Toast.makeText(this, "onUnbind: service", Toast.LENGTH_SHORT).show()
        RecentClient.client = null
        return super.onUnbind(intent)
    }

    //Implementing the IIPCExample interface this object will pass to client
    private val aidlBinder = object : IIPCExample.Stub() {
        override fun getPid(): Int = Process.myPid()

        override fun getConnectionCount(): Int = Companion.connectionCount

        override fun setDisplayedValue(packageName: String?, pid: Int, data: String?) {
            val clientData =
                if (data == null || TextUtils.isEmpty(data)) NOT_SENT
                else data

            // Get message from client. Save recent connected client info.
            RecentClient.client = Client(
                packageName ?: NOT_SENT,
                pid.toString(),
                clientData,
                "AIDL"
            )
        }

        override fun registerConnectionCountCallback(callback: IBikeSpeedCallback?) {
            Log.d(TAG, "registerConnectionCountCallback: ")
            connectionCountCallbacks.register(callback)
        }

        override fun unregisterConnectionCountCallback(callback: IBikeSpeedCallback?) {
            Log.d(TAG, "unregisterConnectionCountCallback: ")
            connectionCountCallbacks.unregister(callback)
        }
    }

    // Method to update connection count and notify clients
    private fun updateSpeedCount() {
        GlobalScope.launch {
            (25 downTo 0).forEach {
                Log.d(TAG, "updateConnectionCount: $it")
                sendBikeSpeedToClient(it)
                delay(1500)
            }
            updateSpeedCount()
        }
    }

    // Notify all registered callbacks
    private fun sendBikeSpeedToClient(count: Int) {
        //beginBroadcast() -It returns the number of registered callbacks in the list.
        val callbackCount = connectionCountCallbacks.beginBroadcast()
        for (i in 0 until callbackCount) {
            try {
                //getBroadcastItem(i) -retrieves the callback at the specified index i.
                //call the onBikeSpeedChanged() method on the retrieved callback.
                connectionCountCallbacks.getBroadcastItem(i)?.onBikeSpeedChanged(count)
            } catch (e: RemoteException) {
                Log.e(TAG, "Error occurred while notifying callback", e)
            }
        }
        connectionCountCallbacks.finishBroadcast()//It releases any resources associated with the broadcast operation
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: service")
        super.onDestroy()
    }

    companion object {
        // How many connection requests have been received since the service started
        var connectionCount: Int = 0

        // Client might have sent an empty data
        const val NOT_SENT = "Not sent!"
        private const val TAG = "AIDLService"
    }
}

