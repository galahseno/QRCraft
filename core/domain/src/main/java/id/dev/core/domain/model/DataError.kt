package id.dev.core.domain.model

sealed interface DataError : Error {
    enum class Network : DataError

    enum class Local : DataError {
        DISK_FULL
    }
}

interface Error