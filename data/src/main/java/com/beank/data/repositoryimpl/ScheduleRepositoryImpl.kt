package com.beank.data.repositoryimpl

import android.os.Build
import androidx.annotation.RequiresApi
import com.beank.data.datasource.StorageDataSource
import com.beank.data.entity.WeekSchedule
import com.beank.data.mapper.toInt
import com.beank.data.mapper.toWeekSchedule
import com.beank.data.utils.dataStateObjects
import com.beank.domain.model.FireStoreState
import com.beank.domain.model.Schedule
import com.beank.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject
@RequiresApi(Build.VERSION_CODES.O)
class ScheduleRepositoryImpl @Inject constructor(
    private val storage : StorageDataSource
) : ScheduleRepository {
    override fun getScheduleInfo(today: LocalDate): Flow<FireStoreState<List<Schedule>>> =
        storage.store.document(storage.getUid()!!).collection(SCHEDULE).whereEqualTo("date",today.toInt())
            .dataStateObjects<WeekSchedule,Schedule>()


    override fun insertSchedule(schedule: Schedule) : Unit =
        storage.save(SCHEDULE,schedule.toWeekSchedule())

    override fun updateSchedule(schedule: Schedule) : Unit =
        storage.replace(SCHEDULE,schedule.id!!,schedule.toWeekSchedule())

    override fun updateCheckSchedule(id : String, check: Boolean) : Unit =
        storage.update(SCHEDULE,id, mapOf("check" to check))

    override fun deleteSchedule(schedule: Schedule) : Unit =
        storage.delete(SCHEDULE,schedule.id!!)

    companion object {
        private const val SCHEDULE = "Schedule"
    }
}