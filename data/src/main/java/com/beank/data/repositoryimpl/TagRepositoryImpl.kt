package com.beank.data.repositoryimpl

import com.beank.data.datasource.StorageDataSource
import com.beank.data.entity.WeekTag
import com.beank.data.mapper.toTagModel
import com.beank.data.mapper.toWeekTag
import com.beank.domain.model.Tag
import com.beank.domain.repository.TagRepository
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val storage : StorageDataSource
): TagRepository {
    override fun getTagInfo(): Flow<List<Tag>> =
        storage.store.document(storage.getUid()!!).collection(TAG).dataObjects<WeekTag>().map { tagList ->
            tagList.map { it.toTagModel() }
        }.flowOn(Dispatchers.IO)

    override suspend fun checkTagTitle(title: String): Boolean =
        storage.store.document(storage.getUid()!!).collection(TAG).whereEqualTo("title",title).get().await().documents.isEmpty()

    override fun insertTag(tag: Tag) : Unit =
        storage.save(TAG,tag.toWeekTag())

    override fun deleteTag(tag: Tag) : Unit =
        storage.delete(TAG,tag.id!!)

    companion object {
        const val TAG = "Tag"
    }
}