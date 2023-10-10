package elieomatuku.cineast_android.broadReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import elieomatuku.cineast_android.utils.NetUtils
import javax.inject.Inject

class NetworkConnectivityBroadcastReceiver @Inject constructor(private val connSink: ConnectivitySink) :
    BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            connSink.updateNetworkConnected(NetUtils.isOnline(context))
        }
    }
}
