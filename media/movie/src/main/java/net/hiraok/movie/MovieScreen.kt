package net.hiraok.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

@UnstableApi
@Composable
fun MovieScreen() {
    //TODO: playing indexがタブ移動で初期化されるので要修正
    //TODO: playing indexをここで持たない方がよいかも
    var playingIndex by rememberSaveable { mutableStateOf(0) }

    fun onMovieChange(index: Int) {
        playingIndex = index
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Black)
        ) {
            PlayerContent(
                currentPlaying = playingIndex,
                mediaSourceList = createMediaSourceList(urlList = urlList),
                onMovieChange = { newIndex ->
                    onMovieChange(newIndex)
                }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Gray)
        ) {
            MoviePlayList(
                urlList = urlList,
                currentPlaying = playingIndex,
                onMovieChange = { newIndex ->
                    onMovieChange(newIndex)
                }
            )
        }
    }
}

fun pullOutUrl(url: String): String {
    val beginIndex = url.indexOf("CDATA[") + 6
    val endIndex = url.lastIndexOf(']') - 1
    return url.substring(beginIndex, endIndex)
}

@UnstableApi
@Composable
fun createMediaSourceList(urlList: List<Url>): ArrayList<MediaSource> {
    val mediaSourceList = remember { arrayListOf<MediaSource>() }
    urlList.forEach { url ->
        val datasourceFactory = DefaultHttpDataSource.Factory()
        mediaSourceList.add(
            when (url.type) {
                URLType.PROGRESSIVE -> {
                    val pMediaSource = ProgressiveMediaSource.Factory(datasourceFactory)
                        .createMediaSource(
                            MediaItem.Builder().setUri(url.url)
                                .setMediaId(url.id.toString())
                                .setMediaMetadata(
                                    MediaMetadata.Builder()
                                        .setDisplayTitle(url.title)
                                        .build()
                                ).build()
                        )
                    pMediaSource
                }

                URLType.DASH -> {
                    if (!url.hasFileExtension) {
                        val dashMediaSource = DashMediaSource.Factory(datasourceFactory)
                            .createMediaSource(
                                MediaItem.Builder().setUri(pullOutUrl(url.url))
                                    .setMediaId(url.id.toString())
                                    .setMediaMetadata(
                                        MediaMetadata.Builder()
                                            .setDisplayTitle(url.title)
                                            .build()
                                    ).build()
                            )
                        //.setMimeType(MimeTypes.APPLICATION_MPD)
                        //.build()
                        dashMediaSource

                    } else {
                        val dashMediaSource = DashMediaSource.Factory(datasourceFactory)
                            .createMediaSource(
                                MediaItem.Builder().setUri(url.url)
                                    .setMediaId(url.id.toString())
                                    .setMediaMetadata(
                                        MediaMetadata.Builder()
                                            .setDisplayTitle(url.title)
                                            .build()
                                    ).build()
                            )
                        dashMediaSource
                    }
                }

                URLType.HLS -> {
                    val hlsMediaSource = HlsMediaSource.Factory(datasourceFactory)
                        .createMediaSource(
                            MediaItem.Builder().setUri(url.url)
                                .setMediaId(url.id.toString())
                                .setMediaMetadata(
                                    MediaMetadata.Builder()
                                        .setDisplayTitle(url.title)
                                        .build()
                                ).build()
                        )
                    hlsMediaSource
                }

                URLType.RTSP -> {
                    val rtspMediaSource = RtspMediaSource.Factory()
                        .createMediaSource(
                            MediaItem.Builder().setUri(url.url)
                                .setMediaId(url.id.toString())
                                .setMediaMetadata(
                                    MediaMetadata.Builder()
                                        .setDisplayTitle(url.title)
                                        .build()
                                ).build()
                        )
                    rtspMediaSource
                }

                URLType.SS -> {
                    val ssMediaSource = SsMediaSource.Factory(datasourceFactory)
                        .createMediaSource(
                            MediaItem.Builder().setUri(url.url)
                                .setMediaId(url.id.toString())
                                .setMediaMetadata(
                                    MediaMetadata.Builder()
                                        .setDisplayTitle(url.title)
                                        .build()
                                ).build()
                        )
                    ssMediaSource
                }
            }
        )
    }
    return mediaSourceList
}

@UnstableApi
@Composable
fun PlayerContent(
    currentPlaying: Int,
    mediaSourceList: ArrayList<MediaSource>,
    onMovieChange: (Int) -> Unit
) {
    val context = LocalContext.current
    val trackSelector = DefaultTrackSelector(context).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }

    var playWhenReady by rememberSaveable { mutableStateOf(false) }
    var playbackPosition by rememberSaveable { mutableStateOf(0L) }

    var mediaTitle by remember(currentPlaying) { mutableStateOf(urlList[currentPlaying].title) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                exoPlayer.setMediaSources(mediaSourceList)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.addListener(
                    object : Player.Listener {
                        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                            onMovieChange(exoPlayer.currentMediaItemIndex)
                            mediaTitle = mediaItem?.mediaMetadata?.displayTitle.toString()
                        }
                    }
                )
            }
    }

    exoPlayer.seekTo(currentPlaying, playbackPosition)

    fun updateState() {
        playbackPosition = exoPlayer.currentPosition
        playWhenReady = exoPlayer.playWhenReady
    }

    Column {
        Text(
            text = mediaTitle,
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                }
            }
        )
    }

    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.pause()
                }
                else -> {
                    /*no-op*/
                }
            }
        }
        val myLifecycle = lifecycleOwner.value.lifecycle
        myLifecycle.addObserver(observer)

        onDispose {
            updateState()
            exoPlayer.release()
        }
    }
}


@Composable
fun MoviePlayList(
    urlList: List<Url>,
    currentPlaying: Int,
    onMovieChange: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier) {
        itemsIndexed(
            items = urlList,
            key = { _, item -> item.id }
        ) { index, item ->
            MovieItem(
                index = index,
                movie = item,
                currentPlaying = currentPlaying,
                onMovieChange = onMovieChange
            )
        }
    }
}

@Composable
fun MovieItem(
    index: Int,
    movie: Url,
    currentPlaying: Int,
    onMovieChange: (Int) -> Unit
) {
    var currentPlaying1 by remember { mutableStateOf(false) }

    currentPlaying1 = index == currentPlaying

    ConstraintLayout(
        modifier = Modifier.clickable { onMovieChange(index) }
    ) {
        val (thumbnail, play, title, nowPlaying) = createRefs()

        Image(
            contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(model = movie.preview),
            contentDescription = "Thumbnail",
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
                .shadow(elevation = 20.dp)
                .constrainAs(thumbnail) {
                    top.linkTo(
                        parent.top,
                        margin = 8.dp
                    )
                    start.linkTo(
                        parent.start,
                        margin = 8.dp
                    )
                    bottom.linkTo(
                        parent.bottom,
                        margin = 8.dp
                    )
                }
        )

        Text(
            text = movie.title,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(
                    thumbnail.top,
                    margin = 8.dp
                )
                start.linkTo(
                    thumbnail.end,
                    margin = 8.dp
                )
                end.linkTo(
                    parent.end,
                    margin = 8.dp
                )
            },
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Divider(
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color(0xFFE0E0E0)
        )

        if (currentPlaying1) {
            Image(
                contentScale = ContentScale.Crop,
                imageVector = Icons.Filled.PlayArrow,
                colorFilter = ColorFilter.tint(Color.Red),
                contentDescription = "playing",
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .constrainAs(play) {
                        top.linkTo(thumbnail.top)
                        start.linkTo(thumbnail.start)
                        end.linkTo(thumbnail.end)
                        bottom.linkTo(thumbnail.bottom)
                    }
            )
            Text(
                text = "Now Playing",
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier =
                Modifier.constrainAs(nowPlaying) {
                    top.linkTo(
                        title.bottom,
                        margin = 8.dp
                    )
                    start.linkTo(
                        thumbnail.end,
                        margin = 8.dp
                    )
                    bottom.linkTo(
                        thumbnail.bottom,
                        margin = 8.dp
                    )
                    end.linkTo(
                        parent.end,
                        margin = 8.dp
                    )
                }
            )
        }
    }
}

//testUris
val urlList: List<Url> = listOf(
    Url(
        1,
        URLType.SS,
        "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/Manifest",
        "SmoothStreaming"
    ),
    Url(
        2,
        URLType.PROGRESSIVE,
        "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
        "Progressive1(Big Buck Bunny)"
    ),
    Url(
        3,
        URLType.PROGRESSIVE,
        "https://download.samplelib.com/mp4/sample-5s.mp4",
        "Progressive2"
    ),
    Url(
        4,
        URLType.DASH,
        "https://dash.akamaized.net/dash264/TestCasesHD/2b/qualcomm/1/MultiResMPEG2.mpd",
        "Dash"
    ),
    Url(
        5,
        URLType.DASH,
        "<![CDATA[https://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0]]>",
        "Dash without file extension",
        false
    ),
    Url(
        6,
        URLType.HLS,
        "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8",
        "Hls"
    ),
    Url(
        7,
        URLType.RTSP,
        "rtsp://rtsp.stream/pattern",
        "Rtsp"
    ),
)

data class Url(
    var id: Int,
    var type: URLType,
    var url: String,
    var title: String,
    var hasFileExtension: Boolean = true,
    var preview: Int = R.drawable.no_img
)

enum class URLType {
    PROGRESSIVE,
    HLS,
    DASH,
    RTSP,
    SS
}

object TestData {
    val list = listOf(
        Movie(
            1,
            "https://google.com"
        )
    )
}

interface MovieRepository {
    fun findMovie(): List<Movie>
}

class MovieMovieRepository : MovieRepository {
    override fun findMovie(): List<Movie> {
        return TestData.list
    }
}

@Composable
fun loadMovies(
    repository: MovieRepository
): State<Result<List<Movie>>> {
    return produceState(initialValue = Result.success(emptyList())) {
        val movies = repository.findMovie()
        value = if (movies.isEmpty()) {
            Result.failure(Throwable())
        } else {
            Result.success(movies)
        }
    }
}

data class Movie(
    val id: Int,
    val url: String
)