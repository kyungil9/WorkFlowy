package com.beank.data.repositoryimpl

import com.beank.data.datasource.StorageDataSource
import com.beank.data.entity.WeekSchedule
import com.beank.data.mapper.localDateToInt
import com.beank.data.mapper.toScheduleModel
import com.beank.data.mapper.toWeekSchedule
import com.beank.data.utils.dataStateObjects
import com.beank.domain.model.FireStoreState
import com.beank.domain.model.Schedule
import com.beank.domain.repository.ScheduleRepository
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val storage : StorageDataSource
) : ScheduleRepository {
    override fun getScheduleInfo(today: LocalDate): Flow<FireStoreState<List<Schedule>>> =
        storage.store.document(storage.getUid()!!).collection(SCHEDULE).whereEqualTo("date",today.localDateToInt())
            .dataStateObjects<WeekSchedule,Schedule>().flowOn(Dispatchers.IO)

    override fun insertSchedule(schedule: Schedule) : Unit =
        storage.save(SCHEDULE,schedule.toWeekSchedule())

    override fun updateSchedule(schedule: Schedule) : Unit =
        storage.replace(SCHEDULE,schedule.id!!,schedule.toWeekSchedule())

    override fun deleteSchedule(schedule: Schedule) : Unit =
        storage.delete(SCHEDULE,schedule.id!!)

    companion object {
        private const val SCHEDULE = "Schedule"
    }
}