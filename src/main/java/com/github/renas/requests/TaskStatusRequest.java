package com.github.renas.requests;

import com.github.renas.requests.task.TaskStatus;

public record TaskStatusRequest (TaskStatus newStatus) {
}
