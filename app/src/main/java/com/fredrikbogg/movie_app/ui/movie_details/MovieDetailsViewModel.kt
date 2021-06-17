package com.fredrikbogg.movie_app.ui.movie_details

import android.util.Log
import androidx.lifecycle.*
import com.fredrikbogg.movie_app.data.db.repository.MediaRepository
import com.fredrikbogg.movie_app.data.db.repository.MovieRepository
import com.fredrikbogg.movie_app.data.db.repository.TorrentRepository
import com.fredrikbogg.movie_app.data.model.Event
import com.fredrikbogg.movie_app.data.model.GoToCast
import com.fredrikbogg.movie_app.data.model.GoToVideo
import com.fredrikbogg.movie_app.data.model.entity.*
import com.fredrikbogg.movie_app.data.model.entity.plex.MediaContainer
import com.fredrikbogg.movie_app.ui.BaseViewModel
import com.fredrikbogg.movie_app.util.extension.liveDataBlockScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class MovieDetailsViewModelFactory(private val movieId: Int, private val movieTitle: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(movieId, movieTitle) as T
    }
}

class MovieDetailsViewModel(movieId: Int, private val movieTitle: String) : BaseViewModel(),
    GoToCast, GoToVideo {

    private val movieRepository = MovieRepository()
    private val mediaRepository = MediaRepository()
    private val torrentRepository = TorrentRepository()
    val movie: LiveData<Movie>
    val videoList: LiveData<List<Video>>
    val castList: LiveData<List<Cast>>
    val media: LiveData<MediaContainer>
    val torrentList: LiveData<List<Torrent>>


    override val goToCastDetailsEvent: MutableLiveData<Event<Cast>> = MutableLiveData()
    override val goToVideoEvent: MutableLiveData<Event<Video>> = MutableLiveData()

    private val _isDownloadable = MutableLiveData(true)
    val isDownloadable: LiveData<Boolean> = _isDownloadable
    private val _hideOrShow = MutableLiveData(false)
    val hideOrShow: LiveData<Boolean> = _hideOrShow

    var torrentLinkResponse: MutableLiveData<ResponseBody> = MutableLiveData()
    val registerResponse: LiveData<ResponseBody>

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
        torrentList =
            liveDataBlockScope {
                torrentRepository.searchTorrent(movieTitle) {
                    mSnackBarText.postValue(Event(it))
                }
            }


        registerResponse =
            liveDataBlockScope {
                torrentRepository.register {
                    mSnackBarText.postValue(Event(it))
                }
            }
    }


    fun onDownloadClick() {
        _isDownloadable.value = !_isDownloadable.value!!
    }

    fun onHideOrShowClick() {
        _hideOrShow.value = !_hideOrShow.value!!
    }

    fun onDownloadClick(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                torrentLinkResponse = torrentRepository.downloadTorrent(id) {
                    mSnackBarText.postValue(Event(it))
                }
            }
        }
    }
}



