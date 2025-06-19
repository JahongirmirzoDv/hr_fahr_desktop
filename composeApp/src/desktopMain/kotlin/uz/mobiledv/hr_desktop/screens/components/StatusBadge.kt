package uz.mobiledv.hr_desktop.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.mobiledv.ui.LocalStatusColors

@Composable
fun StatusBadge(
    text: String,
    status: String,
    modifier: Modifier = Modifier
) {
    val statusColors = LocalStatusColors.current
    
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "ACTIVE" -> statusColors.successContainer to statusColors.success
        "COMPLETED" -> statusColors.infoContainer to statusColors.info
        "PAUSED" -> statusColors.warningContainer to statusColors.warning
        "CANCELLED", "FAILED" -> statusColors.errorContainer to statusColors.error
        "PAID" -> statusColors.successContainer to statusColors.success
        "PENDING" -> statusColors.warningContainer to statusColors.warning
        else -> Color.Gray.copy(alpha = 0.1f) to Color.Gray
    }
    
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ErrorCard(
    title: String,
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val statusColors = LocalStatusColors.current
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = statusColors.errorContainer),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = statusColors.error
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = statusColors.error.copy(alpha = 0.8f),
                fontWeight = FontWeight.Normal
            )
            
            if (onRetry != null) {
                Spacer(Modifier.height(16.dp))
                
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Retry")
                }
            }
        }
    }
}