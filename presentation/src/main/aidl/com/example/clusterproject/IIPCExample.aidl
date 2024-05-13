// IIPCExample.aidl
package com.example.clusterproject;
import com.example.clusterproject.IBikeSpeedCallback;

/*
This interface has an inner abstract class named Stub that extends
Binder and implements methods from your AIDL interface.
*/
interface IIPCExample {
    /** Request the process ID of this service */
    int getPid();

    /** Count of received connection requests from clients */
    int getConnectionCount();

    /** Set displayed value of screen */
    void setDisplayedValue(String packageName, int pid, String data);

     /*
        In AIDL, the in keyword is used to specify that the data should be passed from the client to the service.
        It indicates that the parameter is an input parameter, meaning that the client is providing data to the service through this parameter.
      */
     void registerConnectionCountCallback(in IBikeSpeedCallback callback);//register callback
     void unregisterConnectionCountCallback(in IBikeSpeedCallback callback);//unregister callback
}