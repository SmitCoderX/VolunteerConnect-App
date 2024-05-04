package com.smitcoderx.volunteerconnect.Ui.Posts

import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.Model.Posts.PostsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject constructor(
    private val api: VolunteerConnectApi
) {

    suspend fun createPost(token: String, postsData: PostsData) = api.createPost(token, postsData)
    suspend fun updatePost(token: String, id: String, postsData: PostsData) =
        api.updatePost(token, id, postsData)

    suspend fun deletePost(token: String, id: String) = api.deletePost(token, id)
    suspend fun getPostsList(token: String, id: String) = api.getPostList(token, id)
}