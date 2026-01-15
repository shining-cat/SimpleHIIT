/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CheckIfAnotherUserUsesThatNameUseCase(
    private val usersRepository: UsersRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(user: User): Output<Boolean> =
        withContext(defaultDispatcher) {
            val existingUsers = usersRepository.getUsersList()
            when (existingUsers) {
                is Output.Success -> {
                    if (existingUsers.result.find { it.name == user.name && it.id != user.id } != null) {
                        Output.Success(true)
                    } else {
                        Output.Success(false)
                    }
                }
                is Output.Error -> {
                    existingUsers
                }
            }
        }
}
