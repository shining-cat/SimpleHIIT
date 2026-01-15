/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.datainterfaces

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.domain.common.models.User

interface SessionsRepository {
    suspend fun insertSessionRecord(sessionRecord: SessionRecord): Output<Int>

    suspend fun getSessionRecordsForUser(user: User): Output<List<SessionRecord>>

    suspend fun deleteSessionRecordsForUser(userId: Long): Output<Int>
}
