package id.dev.home.data

import id.dev.home.domain.repo.HistoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::HistoryRepositoryImpl) bind HistoryRepository::class
}