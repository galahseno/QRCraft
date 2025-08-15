package id.dev.core.domain

sealed interface DataError: Error {
    enum class Network: DataError

    enum class Local: DataError
}

interface Error