package com.fredrikbogg.movie_app.data.model.entity.plex

import com.google.gson.annotations.SerializedName
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "Video", strict = false)
data class Video @JvmOverloads constructor(

    @field:Attribute(name = "studio" , required = false)
    @param:Attribute(name = "studio", required = false)
    var studio: String? = null,

    @field:Attribute(name = "summary" , required = false)
    @param:Attribute(name = "summary", required = false)
    var summary: String? = null,

    @field:Attribute(name = "titleSort" , required = false)
    @param:Attribute(name = "titleSort", required = false)
    var titleSort: String? = null,

    @field:Attribute(name = "title" , required = false)
    @param:Attribute(name = "title", required = false)
    var title: String? = null,

    @field:Attribute(name = "viewCount" , required = false)
    @param:Attribute(name = "viewCount", required = false)
    var viewCount: Int = 0,

    @field:Attribute(name = "tagLine" , required = false)
    @param:Attribute(name = "tagLine", required = false)
    var tagLine: String? = null,

    @field:Attribute(name = "viewOffset" , required = false)
    @param:Attribute(name = "viewOffset", required = false)
    var viewOffset: Long = 0,

    @field:Attribute(name = "duration" , required = false)
    @param:Attribute(name = "duration", required = false)
    var duration: Long = 0,

    @field:Attribute(name = "timeAdded" , required = false)
    @param:Attribute(name = "timeAdded", required = false)
    var timeAdded: Long = 0,

    @field:Attribute(name = "timeUpdated" , required = false)
    @param:Attribute(name = "timeUpdated", required = false)
    var timeUpdated: Long = 0,

    @field:Attribute(name = "contentRating" , required = false)
    @param:Attribute(name = "contentRating", required = false)
    var contentRating: String? = null,

    @field:Attribute(name = "year" , required = false)
    @param:Attribute(name = "year", required = false)
    var year: String? = null,

    @field:Attribute(name = "ratingKey" , required = false)
    @param:Attribute(name = "ratingKey", required = false)
    var ratingKey: String? = null,

    @field:Attribute(name = "parentKey" , required = false)
    @param:Attribute(name = "parentKey", required = false)
    var parentKey: String? = null,

    @field:Attribute(name = "episode" , required = false)
    @param:Attribute(name = "episode", required = false)
    var episode: String? = null,

    @field:Attribute(name = "season" , required = false)
    @param:Attribute(name = "season", required = false)
    var season: String? = null,

    @field:Attribute(name = "rating" , required = false)
    @param:Attribute(name = "rating", required = false)
    var rating: Double = 0.0
)