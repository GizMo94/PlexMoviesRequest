package com.fredrikbogg.movie_app.data.model.entity.plex

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import retrofit2.http.Field

@Root(name = "MediaContainer", strict = false)
data class MediaContainer @JvmOverloads constructor(

    @field:Attribute(name = "size" , required = true)
    @param:Attribute(name = "size", required = true)
    var size: Int = 0,

    @field:Attribute(name = "allowSync" , required = false)
    @param:Attribute(name = "allowSync", required = false)
    var allowSync: Int = 0,

    @field:Attribute(name = "art" , required = false)
    @param:Attribute(name = "art", required = false)
    var art: String? = null,


    @field:Attribute(name = "identifier" , required = false)
    @param:Attribute(name = "identifier", required = false)
    var identifier: String? = null,


    @field:Attribute(name = "mediaTagPrefix" , required = false)
    @param:Attribute(name = "mediaTagPrefix", required = false)
    var mediaTagPrefix: String? = null,


    @field:Attribute(name = "mediaTagVersion" , required = false)
    @param:Attribute(name = "mediaTagVersion", required = false)
    var mediaTagVersion: Long = 0,


    @field:Attribute(name = "title1" , required = false)
    @param:Attribute(name = "title1", required = false)
    var title1: String? = null,


    @field:Attribute(name = "title2" , required = false)
    @param:Attribute(name = "title2", required = false)
    var title2: String? = null,


    @field:Attribute(name = "sortAsc" , required = false)
    @param:Attribute(name = "sortAsc", required = false)
    var sortAsc: Int = 0,


    @field:Attribute(name = "content" , required = false)
    @param:Attribute(name = "content", required = false)
    var content: String? = null,


    @field:Attribute(name = "viewGroup" , required = false)
    @param:Attribute(name = "viewGroup", required = false)
    var viewGroup: String? = null,


    @field:Attribute(name = "viewMode" , required = false)
    @param:Attribute(name = "viewMode", required = false)
    var viewMode: Int = 0,


    @field:Attribute(name = "parentPosterURL" , required = false)
    @param:Attribute(name = "parentPosterURL", required = false)
    var parentPosterURL: String? = null,


    @field:Attribute(name = "parentIndex" , required = false)
    @param:Attribute(name = "parentIndex", required = false)
    var parentIndex: String? = null,

    @field:ElementList(name = "videos" , inline = true, required = false, type = Video::class)
    @param:ElementList(name = "videos", inline = true, required = false, type = Video::class)
    var videos: List<Video>? = null
)