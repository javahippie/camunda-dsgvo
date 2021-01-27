export const fetchTaskData = (taskId, successCallback, errorCallback) => {
    fetch(`/engine-rest/task/${taskId}/variables/`)
        .then(result => result.json())
        .then(result => successCallback(result))
        .catch(error => errorCallback(error))
}

export const completeTask = (taskId, variables, successCallback, errorCallback) => {
    fetch(`/engine-rest/task/${taskId}/complete`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(variables)
    }).then(response => {
            if(response.ok) {
                successCallback(response)
            } else {
                errorCallback(response)
            }
        }
    )
}