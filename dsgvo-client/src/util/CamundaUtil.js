export const getValueNullSafe = (variable) => {
    if (variable && variable.value) {
        return variable.value
    } else {
        return null
    }
}