package com.yrc.ddstreamclient.pojo.ssl


data class ApplicationProperty (
    var serverPort: Int? = 8888,
    var isSslClientAuth: Boolean? = false,
    var keystorePath: String? = null,
    var keystorePassword: CharArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationProperty

        if (serverPort != other.serverPort) return false
        if (isSslClientAuth != other.isSslClientAuth) return false
        if (keystorePath != other.keystorePath) return false
        if (!keystorePassword.contentEquals(other.keystorePassword)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = serverPort ?: 0
        result = 31 * result + (isSslClientAuth?.hashCode() ?: 0)
        result = 31 * result + (keystorePath?.hashCode() ?: 0)
        result = 31 * result + keystorePassword.contentHashCode()
        return result
    }
}