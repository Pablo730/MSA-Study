package study.msa.msauserservice

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun main() {
    println(BCryptPasswordEncoder().encode("password"))
}
