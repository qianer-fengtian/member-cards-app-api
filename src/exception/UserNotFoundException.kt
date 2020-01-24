package jp.co.anyplus.anyplab.webapp.membercards.exception

import java.lang.RuntimeException

class UserNotFoundException(message: String) : RuntimeException(message) {
}