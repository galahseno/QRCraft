package id.dev.core.domain.model

sealed interface DataError: Error {
    enum class Network: DataError

    enum class Local: DataError
}

interface Error