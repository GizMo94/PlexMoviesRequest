package com.fredrikbogg.movie_app.ui.movie_details

import android.util.Log
import androidx.lifecycle.*
import com.fredrikbogg.movie_app.data.db.repository.MediaRepository
import com.fredrikbogg.movie_app.data.db.repository.MovieRepository
import com.fredrikbogg.movie_app.data.model.Event
import com.fredrikbogg.movie_app.data.model.GoToCast
import com.fredrikbogg.movie_app.data.model.GoToVideo
import com.fredrikbogg.movie_app.data.model.entity.*
import com.fredrikbogg.movie_app.data.model.entity.plex.MediaContainer
import com.fredrikbogg.movie_app.ui.BaseViewModel
import com.fredrikbogg.movie_app.util.extension.liveDataBlockScope

class MovieDetailsViewModelFactory(private val movieId: Int, private val movieTitle: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(movieId, movieTitle) as T
    }
}

class MovieDetailsViewModel(movieId: Int, private val movieTitle: String) : BaseViewModel(), GoToCast, GoToVideo {

    private val movieRepository = MovieRepository()
    private val mediaRepository = MediaRepository()
    val movie: LiveData<Movie>
    val videoList: LiveData<List<Video>>
    val castList: LiveData<List<Cast>>
    val media: LiveData<MediaContainer>
    val isAvailableOnPlex: MutableLiveData<Boolean> = MutableLiveData()


    override val goToCastDetailsEvent: MutableLiveData<Event<Cast>> = MutableLiveData()
    override val goToVideoEvent: MutableLiveData<Event<Video>> = MutableLiveData()

    init {
        movie = liveDataBlockScope {
            movieRepository.loadDetails(movieId) {
                mSnackBarText.postValue(Event(it))
            }
        }

        videoList = liveDataBlockScope {
            movieRepository.loadVideos(movieId) { mSnackBarText.postValue(Event(it)) }
        }

        castList = liveDataBlockScope {
            movieRepository.loadCredits(movieId) { mSnackBarText.postValue(Event(it)) }
        }

        media = liveDataBlockScope {
            mediaRepository.loadMedia(movieTitle) {
                mSnackBarText.postValue(Event(it))
            }
        }
    }
}
