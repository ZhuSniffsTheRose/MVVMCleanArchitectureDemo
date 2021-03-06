/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.zhu.mvvmdemo.data

import com.google.gson.annotations.SerializedName


data class RedditPost(
        @SerializedName("name")
        val name: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("score")
        val score: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("subreddit") // technically mutable but fine for a demo
        val subreddit: String,
        @SerializedName("num_comments")
        val num_comments: Int,
        @SerializedName("created_utc")
        val created: Long,
        val thumbnail: String?,
        val url: String?) {
    // to be consistent w/ changing backend order, we need to keep a data like this
    var indexInResponse: Int = -1
}

/**
 * for Display
 */
data class RedditPostToDisplay(
        val name: String,
        val title: String,
        val score: Int,
        val author: String,
        val subreddit: String,
        val numComments: Int,
        val created: Long,
        val thumbnail: String?,
        val url: String?)

fun RedditPost.mapToDisplay() = RedditPostToDisplay(
        name = name,
        title = title,
        score = score,
        author = author,
        subreddit = subreddit,
        numComments = num_comments,
        created = created,
        thumbnail = thumbnail,
        url = url)
