package elieomatuku.cineast_android.business.model.data.broadReceiver

interface ConnectivitySink {
    fun updateNetworkConnected(isConnected: Boolean)
}