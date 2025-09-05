package id.dev.home.presentation.history.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import id.dev.home.presentation.history.DeleteUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionSheet(
    isVisible: Boolean,
    onDismissSheet: () -> Unit,
) {
    val context = LocalContext.current
//    val viewModel: NotesViewModel = hiltViewModel()
//    val deleteNoteState by viewModel.deleteNoteState.collectAsStateWithLifecycle()
//    val onDeleteClick = { viewModel.deleteNote(noteId = noteId) }

//    LaunchedEffect(deleteNoteState) {
//        when (deleteNoteState) {
//            is DeleteNoteUiState.Success -> {
//                delay(2_000L)
//                notes.refresh()
//                viewModel.resetDeleteState()
//                onDismissSheet()
//            }
//            is DeleteNoteUiState.Error -> {
//                Toast.makeText(
//                    context,
//                    (deleteNoteState as DeleteNoteUiState.Error).message,
//                    Toast.LENGTH_SHORT
//                ).show()
//                viewModel.resetDeleteState()
//            }
//            else -> {}
//        }
//    }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismissSheet()
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActionContent(
                    state = DeleteUiState(
                        smth = "smth",
                        isSuccess = false
                    ),
                    onDeleteClick = {
//                        onDeleteClick()
                    },
                    onShareClick = {
//                        onShareClick()
                    }
                )
            }
        }
    }
}
