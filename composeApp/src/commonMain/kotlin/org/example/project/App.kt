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

@Composable
fun App(
    initialMeetingInfo: MeetingInformation = MeetingInformation(
        meetingId = "8b181d85-240d-451d-abe7-c45414b13049",
        externalMeetingId = "778889999",
        attendeeId = "0b1c1beb-5c8c-c05e-5d24-934bfc1d762a",
        externalUserId = "778889999",
        joinToken = "MGIxYzFiZWItNWM4Yy1jMDVlLTVkMjQtOTM0YmZjMWQ3NjJhOjI5ZTg5NzQ4LTA0OTEtNDVkZS1hMTNlLTBhMWM1MjQwZjAwNA",
        audioHostURL = "fc3e8d86e85bfa35adf92e7ca413fa21.k.m3.ew2.app.chime.aws:3478",
        audioFallbackURL = "wss://wss.k.m3.ew2.app.chime.aws:443/calls/8b181d85-240d-451d-abe7-c45414b13049",
        turnControlURL = "https://3049.cell.eu-west-2.meetings.chime.aws/v2/turn_sessions",
        signalingURL = "wss://signal.m3.ew2.app.chime.aws/control/8b181d85-240d-451d-abe7-c45414b13049",
        ingestionURL = "https://data.svc.ew2.ingest.chime.aws/v1/client-events"
    ),
    viewModel: AppViewModel = viewModel { AppViewModel(initialMeetingInfo) }
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
