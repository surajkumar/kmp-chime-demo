package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wannaverse.chimesdk.MeetingInformation
import org.example.project.composables.InMeetingScreen
import org.example.project.composables.JoinScreen
import org.example.project.composables.LoadingScreen

fun getMeetingInfo(platform: Platform) : MeetingInformation {
    val meetingId = "d0195d13-68c8-46a7-8e7b-82a595b93049"
    val externalMeetingId = "778889999"
    val audioHostURL = "32b682071ebbc8abadcc77aee115570a.k.m3.ew2.app.chime.aws:3478"
    val audioFallbackURL = "wss://wss.k.m3.ew2.app.chime.aws:443/calls/d0195d13-68c8-46a7-8e7b-82a595b93049"
    val turnControlURL = "https://3049.cell.eu-west-2.meetings.chime.aws/v2/turn_sessions"
    val signalingURL = "wss://signal.m3.ew2.app.chime.aws/control/d0195d13-68c8-46a7-8e7b-82a595b93049"
    val ingestionURL = "https://data.svc.ew2.ingest.chime.aws/v1/client-events"

    if(platform.name.contains("Android")) {
        return MeetingInformation(
            meetingId = meetingId,
            externalMeetingId = externalMeetingId,
            attendeeId = "062e1a5a-0878-5d54-bdc7-f60d46aa5361",
            externalUserId = "778889999",
            joinToken = "MDYyZTFhNWEtMDg3OC01ZDU0LWJkYzctZjYwZDQ2YWE1MzYxOjE5MjdiYTNlLWZjOGUtNDY4MS1hODJhLTg2ZjQwY2E0Njk5ZQ",
            audioHostURL = audioHostURL,
            audioFallbackURL = audioFallbackURL,
            turnControlURL = turnControlURL,
            signalingURL = signalingURL,
            ingestionURL = ingestionURL
        )
    } else {
        return MeetingInformation(
            meetingId = meetingId,
            externalMeetingId = externalMeetingId,
            attendeeId = "e058666b-f51a-e4b5-75ef-ead4d694e122", // postfixed with 2
            externalUserId = "123456789", // postfixed with 2
            joinToken = "ZTA1ODY2NmItZjUxYS1lNGI1LTc1ZWYtZWFkNGQ2OTRlMTIyOjUyYzRlNGJlLTY4M2QtNDM4Ni1hODlkLWQ1ZjFlNjM4NTU0OA", // postfixed with 2
            audioHostURL = audioHostURL,
            audioFallbackURL = audioFallbackURL,
            turnControlURL = turnControlURL,
            signalingURL = signalingURL,
            ingestionURL = ingestionURL
        )
    }
}

@Composable
fun App(
    viewModel: AppViewModel = viewModel { AppViewModel(getMeetingInfo(getPlatform())) }
) {
    val state by viewModel.callState.collectAsStateWithLifecycle()
    val info by viewModel.meetingInfo.collectAsStateWithLifecycle()
    val chatInput by viewModel.chatInput.collectAsStateWithLifecycle()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            when {
                state.isJoined -> InMeetingScreen(
                    state = state,
                    chatInput = chatInput,
                    viewModel = viewModel
                )
                state.isLoading -> LoadingScreen(
                    status = state.connectionStatus,
                    onCancel = { viewModel.leaveMeeting() }
                )
                else -> JoinScreen(
                    info = info,
                    state = state,
                    viewModel = viewModel
                )
            }
        }
    }
}
