package unl.fct.smart_grow.security

import android.os.Build
import android.support.annotation.RequiresApi
import com.auth0.jwt.JWT
import java.util.*

object MockJwtStorage {

    var jwt: String? = null
    var isAdmin: Boolean = false
    var userName: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(jwt: String) {
        this.jwt = jwt
        val decodeJwt = JWT.decode(jwt)

        this.userName = decodeJwt.claims["username"]!!.asString()
        this.isAdmin = decodeJwt.claims["isAdmin"]!!.asBoolean()
    }

    fun isExpired (): Boolean {
        if (jwt == null) {
            return false
        }
        val decodeJwt = JWT.decode(jwt) ?: null

        return decodeJwt?.expiresAt?.before(Date()) ?: false
    }

    fun logout (){
        this.jwt = null;
        this.isAdmin = false;
        this.userName = null;
    }
}