/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.shining_cat.simplehiit.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import fr.shining_cat.simplehiit.data.DataItemTypeShivaRepository
import fr.shining_cat.simplehiit.data.DefaultDataItemTypeShivaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsDataItemTypeShivaRepository(
        dataItemTypeShivaRepository: DefaultDataItemTypeShivaRepository
    ): DataItemTypeShivaRepository
}

class FakeDataItemTypeShivaRepository @Inject constructor() : DataItemTypeShivaRepository {
    override val dataItemTypeShivas: Flow<List<String>> = flowOf(fakeDataItemTypeShivas)

    override suspend fun add(name: String) {
        throw NotImplementedError()
    }
}

val fakeDataItemTypeShivas = listOf("One", "Two", "Three")
