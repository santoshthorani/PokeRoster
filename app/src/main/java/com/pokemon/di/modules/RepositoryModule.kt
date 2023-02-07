package com.pokemon.di.modules

import android.app.Application
import com.pokemon.data.remote.PokemonApiService
import com.pokemon.data.repository.PokemonRepository
import com.pokemon.data.repository.PokemonRepositoryImpl
import com.pokemon.utils.StringUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideStringUtils(app: Application): StringUtils {
        return StringUtils(app)
    }

    @Singleton
    @Provides
    fun providePokemonRepository(
        stringUtils: StringUtils,
        apiService: PokemonApiService
    ): PokemonRepository {
        return PokemonRepositoryImpl(stringUtils, apiService)
    }
}
