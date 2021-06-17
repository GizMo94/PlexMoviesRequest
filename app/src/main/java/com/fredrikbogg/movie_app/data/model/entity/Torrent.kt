package com.fredrikbogg.movie_app.data.model.entity

import pl.droidsonroids.jspoon.annotation.Selector

class Torrent {
    @Selector("td:nth-child(1)")
    lateinit var type: String

    @Selector("td:nth-child(2)")
    lateinit var name: String

    @Selector("td:nth-child(3) > a", attr = "target")
    lateinit var nfo: String

    @Selector("td:nth-child(4)")
    lateinit var com: String

    @Selector("td:nth-child(5)")
    lateinit var age: String

    @Selector("td:nth-child(6)")
    lateinit var size: String

    @Selector("td:nth-child(7)")
    lateinit var complete: String

    @Selector("td:nth-child(8)")
    lateinit var seed: String

    @Selector("td:nth-child(9)")
    lateinit var leech: String

    fun torrentToString(): String {
        return "Torrent(type='$type', name='$name', nfo='$nfo', com='$com', age='$age', size='$size', complete='$complete', seed='$seed', leech='$leech')"
    }


}
