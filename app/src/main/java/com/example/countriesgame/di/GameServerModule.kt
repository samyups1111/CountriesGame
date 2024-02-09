package com.example.countriesgame.di

import com.example.countriesgame.server.GameServer
import com.example.countriesgame.server.GameServerImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class GameServerModule {
    @Binds
    abstract fun bindGameServerImpl(
        gameServer: GameServerImp,
    ): GameServer
}